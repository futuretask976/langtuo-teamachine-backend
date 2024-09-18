package com.langtuo.teamachine.biz.service.user;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.user.OrgDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.user.OrgPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.user.OrgMgtService;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.accessor.user.AdminAccessor;
import com.langtuo.teamachine.dao.accessor.user.OrgAccessor;
import com.langtuo.teamachine.dao.node.user.OrgNode;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
    public TeaMachineResult<OrgDTO> getTop(String tenantCode) {
        TeaMachineResult<OrgDTO> teaMachineResult;
        try {
            OrgNode orgNode = orgAccessor.findTopOrgNode(tenantCode);
            teaMachineResult = TeaMachineResult.success(convert(orgNode));
        } catch (Exception e) {
            log.error("orgMgtService|getTop|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<OrgDTO>> listByParent(String tenantCode, String orgName) {
        TeaMachineResult<List<OrgDTO>> teaMachineResult;
        try {
            List<OrgNode> orgNodeList = orgAccessor.listByParentOrgName(tenantCode, orgName);
            teaMachineResult = TeaMachineResult.success(convert(orgNodeList));
        } catch (Exception e) {
            log.error("orgMgtService|listByParent|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<OrgDTO>> list(String tenantCode) {
        TeaMachineResult<List<OrgDTO>> teaMachineResult;
        try {
            List<OrgNode> nodeList = orgAccessor.list(tenantCode);
            List<OrgDTO> dtoList = convert(nodeList);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("orgMgtService|list|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<OrgDTO>> search(String tenantCode, String orgName,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<OrgDTO>> teaMachineResult;
        try {
            PageInfo<OrgNode> pageInfo = orgAccessor.search(tenantCode, orgName, pageNum, pageSize);
            List<OrgDTO> dtoList = convert(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("orgMgtService|search|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<OrgDTO> get(String tenantCode, String orgName) {
        TeaMachineResult<OrgDTO> teaMachineResult;
        try {
            OrgNode orgNode = orgAccessor.getByOrgName(tenantCode, orgName);
            OrgDTO orgDTO = convert(orgNode);
            teaMachineResult = TeaMachineResult.success(orgDTO);
        } catch (Exception e) {
            log.error("orgMgtService|get|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(OrgPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        OrgNode orgNode = convert(request);
        if (request.isPutNew()) {
            return putNew(orgNode);
        } else {
            return putUpdate(orgNode);
        }
    }

    private TeaMachineResult<Void> putNew(OrgNode po) {
        TeaMachineResult<Void> teaMachineResult;
        try {
            OrgNode exist = orgAccessor.getByOrgName(po.getTenantCode(), po.getOrgName());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = orgAccessor.insert(po);
            if (inserted != CommonConsts.NUM_ONE) {
                log.error("orgMgtService|putNew|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("orgMgtService|putNew|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    private TeaMachineResult<Void> putUpdate(OrgNode po) {
        try {
            OrgNode exist = orgAccessor.getByOrgName(po.getTenantCode(), po.getOrgName());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = orgAccessor.update(po);
            if (updated != CommonConsts.NUM_ONE) {
                log.error("orgMgtService|putUpdate|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("orgMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String orgName) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            int count4Admin = adminAccessor.countByOrgName(tenantCode, orgName);
            if (count4Admin > CommonConsts.DB_COUNT_RESULT_ZERO) {
                log.error("orgMgtService|delete|errorByAdmin|" + count4Admin);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
            }

            int count4ShopGroup = shopGroupAccessor.countByOrgName(tenantCode, orgName);
            if (count4ShopGroup > CommonConsts.DB_COUNT_RESULT_ZERO) {
                log.error("orgMgtService|delete|errorByShopGroup|" + count4ShopGroup);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
            }

            int deleted = orgAccessor.delete(tenantCode, orgName);
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("orgMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private List<OrgDTO> convert(List<OrgNode> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<OrgDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private OrgDTO convert(OrgNode node) {
        if (node == null) {
            return null;
        }

        OrgDTO dto = new OrgDTO();
        dto.setGmtCreated(node.getGmtCreated());
        dto.setGmtModified(node.getGmtModified());
        dto.setOrgName(node.getOrgName());
        dto.setParentOrgName(node.getParentOrgName());

        if (!CollectionUtils.isEmpty(node.getChildren())) {
            List<OrgDTO> children = Lists.newArrayList();
            for (OrgNode orgNode : node.getChildren()) {
                children.add(convert(orgNode));
            }
            dto.setChildren(children);
        }
        return dto;
    }

    private OrgNode convert(OrgPutRequest request) {
        if (request == null) {
            return null;
        }

        OrgNode node = new OrgNode();
        node.setTenantCode(request.getTenantCode());
        node.setOrgName(request.getOrgName());
        node.setParentOrgName(request.getParentOrgName());
        return node;
    }
}
