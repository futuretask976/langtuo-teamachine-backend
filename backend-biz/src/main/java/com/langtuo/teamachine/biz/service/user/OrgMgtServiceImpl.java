package com.langtuo.teamachine.biz.service.user;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.user.OrgDTO;
import com.langtuo.teamachine.api.request.user.OrgPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.user.OrgMgtService;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.accessor.user.AdminAccessor;
import com.langtuo.teamachine.dao.accessor.user.OrgAccessor;
import com.langtuo.teamachine.dao.node.user.OrgNode;
import com.langtuo.teamachine.dao.po.user.AdminPO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.LocaleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.langtuo.teamachine.biz.convert.user.OrgMgtConvertor.convertToOrgDTO;
import static com.langtuo.teamachine.biz.convert.user.OrgMgtConvertor.convertToOrgNode;

@Component
@Slf4j
public class OrgMgtServiceImpl implements OrgMgtService {
    @Resource
    private OrgAccessor orgAccessor;

    @Resource
    private AdminAccessor adminAccessor;

    @Resource
    private ShopGroupAccessor shopGroupAccessor;

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<OrgDTO> getTop(String tenantCode) {
        TeaMachineResult<OrgDTO> teaMachineResult;
        try {
            AdminPO adminPO = DaoUtils.getAdminPOByLoginSession(tenantCode);
            OrgNode orgNode = orgAccessor.getByOrgName(tenantCode, adminPO.getOrgName());
            teaMachineResult = TeaMachineResult.success(convertToOrgDTO(orgNode));
        } catch (Exception e) {
            log.error("|orgMgtService|getTop|fatal|" + e.getMessage() + "|", e);
            teaMachineResult = TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<List<OrgDTO>> listByParentOrgName(String tenantCode, String orgName) {
        TeaMachineResult<List<OrgDTO>> teaMachineResult;
        try {
            List<OrgNode> orgNodeList = orgAccessor.listByParentOrgName(tenantCode, orgName);
            teaMachineResult = TeaMachineResult.success(convertToOrgDTO(orgNodeList));
        } catch (Exception e) {
            log.error("|orgMgtService|listByParent|fatal|" + e.getMessage() + "|", e);
            teaMachineResult = TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<List<OrgDTO>> list(String tenantCode) {
        try {
            AdminPO adminPO = DaoUtils.getAdminPOByLoginSession(tenantCode);
            List<OrgNode> nodeList = orgAccessor.listByParentOrgName(tenantCode, adminPO.getOrgName());
            List<OrgDTO> dtoList = convertToOrgDTO(nodeList);
            return TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("|orgMgtService|list|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<PageDTO<OrgDTO>> search(String tenantCode, String orgName,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<OrgNode> pageInfo = orgAccessor.search(tenantCode, orgName, pageNum, pageSize);
            List<OrgDTO> dtoList = convertToOrgDTO(pageInfo.getList());
            return TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("|orgMgtService|search|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<OrgDTO> getByOrgName(String tenantCode, String orgName) {
        TeaMachineResult<OrgDTO> teaMachineResult;
        try {
            OrgNode orgNode = orgAccessor.getByOrgName(tenantCode, orgName);
            OrgDTO orgDTO = convertToOrgDTO(orgNode);
            teaMachineResult = TeaMachineResult.success(orgDTO);
        } catch (Exception e) {
            log.error("|orgMgtService|get|fatal|" + e.getMessage() + "|", e);
            teaMachineResult = TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(OrgPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        OrgNode orgNode = convertToOrgNode(request);
        try {
            if (request.isPutNew()) {
                return putNew(orgNode);
            } else {
                return putUpdate(orgNode);
            }
        } catch (Exception e) {
            log.error("|orgMgtService|put|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> putNew(OrgNode po) {
        OrgNode exist = orgAccessor.getByOrgName(po.getTenantCode(), po.getOrgName());
        if (exist != null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
        }

        int inserted = orgAccessor.insert(po);
        if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
            log.error("|orgMgtService|putNew|error|" + inserted + "|");
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return TeaMachineResult.success();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> putUpdate(OrgNode po) {
        OrgNode exist = orgAccessor.getByOrgName(po.getTenantCode(), po.getOrgName());
        if (exist == null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
        }

        int updated = orgAccessor.update(po);
        if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
            log.error("|orgMgtService|putUpdate|error|" + updated + "|");
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
        return TeaMachineResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public TeaMachineResult<Void> deleteByOrgName(String tenantCode, String orgName) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            int count4Admin = adminAccessor.countByOrgName(tenantCode, orgName);
            if (count4Admin > CommonConsts.DB_COUNT_RESULT_ZERO) {
                log.error("|orgMgtService|delete|errorByAdmin|" + count4Admin + "|");
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
            }

            int count4ShopGroup = shopGroupAccessor.countByOrgName(tenantCode, orgName);
            if (count4ShopGroup > CommonConsts.DB_COUNT_RESULT_ZERO) {
                log.error("|orgMgtService|delete|errorByShopGroup|" + count4ShopGroup + "|");
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
            }

            int deleted = orgAccessor.delete(tenantCode, orgName);
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("|orgMgtService|delete|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }
}
