package com.langtuo.teamachine.biz.service.shop;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.shop.ShopGroupDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.shop.ShopGroupPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.shop.ShopGroupMgtService;
import com.langtuo.teamachine.biz.util.BizUtils;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.accessor.user.AdminAccessor;
import com.langtuo.teamachine.dao.accessor.user.OrgAccessor;
import com.langtuo.teamachine.dao.po.rule.DrainRulePO;
import com.langtuo.teamachine.dao.po.rule.DrainRuleToppingPO;
import com.langtuo.teamachine.dao.po.shop.ShopGroupPO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ShopGroupMgtServiceImpl implements ShopGroupMgtService {
    @Resource
    private ShopGroupAccessor shopGroupAccessor;

    @Resource
    private ShopAccessor shopAccessor;

    @Resource
    private AdminAccessor adminAccessor;

    @Resource
    private OrgAccessor orgAccessor;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<ShopGroupDTO> getByCode(String tenantCode, String shopGroupCode) {
        ShopGroupPO shopGroupPO = shopGroupAccessor.selectOneByShopGroupCode(tenantCode, shopGroupCode);
        ShopGroupDTO shopGroupDTO = convert(shopGroupPO);
        return TeaMachineResult.success(shopGroupDTO);
    }

    @Override
    public TeaMachineResult<ShopGroupDTO> getByName(String tenantCode, String shopGroupName) {
        ShopGroupPO shopGroupPO = shopGroupAccessor.selectOneByShopGroupName(tenantCode, shopGroupName);
        ShopGroupDTO shopGroupDTO = convert(shopGroupPO);
        return TeaMachineResult.success(shopGroupDTO);
    }

    @Override
    public TeaMachineResult<PageDTO<ShopGroupDTO>> search(String tenantCode, String shopGroupName,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<ShopGroupDTO>> teaMachineResult;
        try {
            PageInfo<ShopGroupPO> pageInfo = shopGroupAccessor.search(tenantCode, shopGroupName,
                    pageNum, pageSize);
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                    convert(pageInfo.getList()), pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<ShopGroupDTO>> list(String tenantCode) {
        TeaMachineResult<List<ShopGroupDTO>> teaMachineResult;
        try {
            List<ShopGroupPO> list = shopGroupAccessor.selectList(tenantCode);
            teaMachineResult = TeaMachineResult.success(convert(list));
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<ShopGroupDTO>> listByAdminOrg(String tenantCode) {
        TeaMachineResult<List<ShopGroupDTO>> teaMachineResult;
        try {
            List<ShopGroupPO> list = shopGroupAccessor.selectListByOrgName(
                    tenantCode, DaoUtils.getOrgNameListByAdmin(tenantCode));
            teaMachineResult = TeaMachineResult.success(convert(list));
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(ShopGroupPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        ShopGroupPO shopGroupPO = convert(request);
        if (request.isNewPut()) {
            return putNew(shopGroupPO);
        } else {
            return putUpdate(shopGroupPO);
        }
    }

    private TeaMachineResult<Void> putNew(ShopGroupPO po) {
        try {
            ShopGroupPO exist = shopGroupAccessor.selectOneByShopGroupCode(po.getTenantCode(), po.getShopGroupCode());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = shopGroupAccessor.insert(po);
            if (CommonConsts.NUM_ONE != inserted) {
                log.error("shopGroupMgtService|putNew|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("shopGroupMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(ShopGroupPO po) {
        try {
            ShopGroupPO exist = shopGroupAccessor.selectOneByShopGroupCode(po.getTenantCode(), po.getShopGroupCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = shopGroupAccessor.insert(po);
            if (CommonConsts.NUM_ONE != updated) {
                log.error("shopGroupMgtService|putUpdate|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("shopGroupMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String shopGroupCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(shopGroupCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int count = shopAccessor.countByShopGroupCode(tenantCode, shopGroupCode);
            if (count == CommonConsts.DB_SELECT_RESULT_EMPTY) {
                int deleted = shopGroupAccessor.deleteByShopGroupCode(tenantCode, shopGroupCode);
                teaMachineResult = TeaMachineResult.success();
            } else {
                teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
            }
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    private List<ShopGroupDTO> convert(List<ShopGroupPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<ShopGroupDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private ShopGroupDTO convert(ShopGroupPO po) {
        if (po == null) {
            return null;
        }

        ShopGroupDTO dto = new ShopGroupDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setShopGroupCode(po.getShopGroupCode());
        dto.setShopGroupName(po.getShopGroupName());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setOrgName(po.getOrgName());

        int shopCount = shopAccessor.countByShopGroupCode(po.getTenantCode(), po.getShopGroupCode());
        dto.setShopCount(shopCount);

        return dto;
    }

    private ShopGroupPO convert(ShopGroupPutRequest request) {
        if (request == null) {
            return null;
        }

        ShopGroupPO po = new ShopGroupPO();
        po.setShopGroupName(request.getShopGroupName());
        po.setShopGroupCode(request.getShopGroupCode());
        po.setComment(request.getComment());
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setOrgName(request.getOrgName());
        return po;
    }
}
