package com.langtuo.teamachine.biz.service.shop;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.shop.ShopGroupDTO;
import com.langtuo.teamachine.api.request.shop.ShopGroupPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.shop.ShopGroupMgtService;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.po.shop.ShopGroupPO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.LocaleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.langtuo.teamachine.biz.convert.shop.ShopGroupMgtConvertor.convert;

@Component
@Slf4j
public class ShopGroupMgtServiceImpl implements ShopGroupMgtService {
    @Resource
    private ShopGroupAccessor shopGroupAccessor;

    @Resource
    private ShopAccessor shopAccessor;

    @Override
    public TeaMachineResult<ShopGroupDTO> getByShopGroupCode(String tenantCode, String shopGroupCode) {
        ShopGroupPO shopGroupPO = shopGroupAccessor.getByShopGroupCode(tenantCode, shopGroupCode);
        ShopGroupDTO shopGroupDTO = convert(shopGroupPO);
        return TeaMachineResult.success(shopGroupDTO);
    }

    @Override
    public TeaMachineResult<PageDTO<ShopGroupDTO>> search(String tenantCode, String shopGroupName,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            List<String> orgNameList = DaoUtils.getOrgNameListByLoginSession(tenantCode);
            PageInfo<ShopGroupPO> pageInfo = shopGroupAccessor.search(tenantCode, shopGroupName,
                    pageNum, pageSize, orgNameList);

            return TeaMachineResult.success(new PageDTO<>(
                    convert(pageInfo.getList()), pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("shopGroupMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<List<ShopGroupDTO>> list(String tenantCode) {
        try {
            List<String> orgNameList = DaoUtils.getOrgNameListByLoginSession(tenantCode);
            List<ShopGroupPO> list = shopGroupAccessor.list(tenantCode, orgNameList);
            return TeaMachineResult.success(convert(list));
        } catch (Exception e) {
            log.error("shopGroupMgtService|list|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(ShopGroupPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        ShopGroupPO shopGroupPO = convert(request);
        if (request.isPutNew()) {
            return putNew(shopGroupPO);
        } else {
            return putUpdate(shopGroupPO);
        }
    }

    private TeaMachineResult<Void> putNew(ShopGroupPO po) {
        try {
            ShopGroupPO exist = shopGroupAccessor.getByShopGroupCode(po.getTenantCode(), po.getShopGroupCode());
            if (exist != null) {
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = shopGroupAccessor.insert(po);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                log.error("shopGroupMgtService|putNew|error|" + inserted);
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("shopGroupMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(ShopGroupPO po) {
        try {
            ShopGroupPO exist = shopGroupAccessor.getByShopGroupCode(po.getTenantCode(), po.getShopGroupCode());
            if (exist == null) {
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = shopGroupAccessor.update(po);
            if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
                log.error("shopGroupMgtService|putUpdate|error|" + updated);
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("shopGroupMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> deleteByShopGroupCode(String tenantCode, String shopGroupCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(shopGroupCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            int count = shopAccessor.countByShopGroupCode(tenantCode, shopGroupCode);
            if (count == CommonConsts.DB_SELECT_ZERO_ROW) {
                int deleted = shopGroupAccessor.deleteByShopGroupCode(tenantCode, shopGroupCode);
                return TeaMachineResult.success();
            } else {
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
            }
        } catch (Exception e) {
            log.error("shopGroupMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }
}
