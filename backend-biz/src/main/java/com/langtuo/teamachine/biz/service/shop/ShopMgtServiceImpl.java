package com.langtuo.teamachine.biz.service.shop;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.request.shop.ShopPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.accessor.user.AdminAccessor;
import com.langtuo.teamachine.dao.accessor.user.OrgAccessor;
import com.langtuo.teamachine.dao.po.shop.ShopGroupPO;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
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
public class ShopMgtServiceImpl implements ShopMgtService {
    @Resource
    private ShopAccessor shopAccessor;

    @Resource
    private AdminAccessor adminAccessor;

    @Resource
    private OrgAccessor orgAccessor;

    @Resource
    private ShopGroupAccessor shopGroupAccessor;

    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<ShopDTO> getByCode(String tenantCode, String shopCode) {
        ShopPO shopPO = shopAccessor.getByShopCode(tenantCode, shopCode);
        ShopDTO shopDTO = convert(shopPO);
        return TeaMachineResult.success(shopDTO);
    }

    @Override
    public TeaMachineResult<PageDTO<ShopDTO>> search(String tenantCode, String shopName, String shopGroupCode,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<ShopPO> pageInfo = shopAccessor.search(tenantCode, shopName, shopGroupCode,
                    pageNum, pageSize);
            return TeaMachineResult.success(new PageDTO<>(
                    convert(pageInfo.getList()), pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("shopMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<List<ShopDTO>> listByShopGroupCode(String tenantCode, String shopGroupCode) {
        try {
            List<ShopPO> list = shopAccessor.listByShopGroupCode(tenantCode, shopGroupCode);
            return TeaMachineResult.success(convert(list));
        } catch (Exception e) {
            log.error("shopMgtService|listByShopGroupCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<List<ShopDTO>> listByAdminOrg(String tenantCode) {
        try {
            List<ShopGroupPO> shopGroupPOList = shopGroupAccessor.listByOrgName(
                    tenantCode, DaoUtils.getOrgNameListByAdmin(tenantCode));
            if (CollectionUtils.isEmpty(shopGroupPOList)) {
                return TeaMachineResult.success(null);
            } else {
                List<String> shopGroupCodeList = shopGroupPOList.stream()
                        .map(shopGroupDTO -> shopGroupDTO.getShopGroupCode())
                        .collect(Collectors.toList());
                List<ShopPO> list = shopAccessor.listByShopGroupCode(tenantCode, shopGroupCodeList);
                return TeaMachineResult.success(convert(list));
            }
        } catch (Exception e) {
            log.error("shopMgtService|listByAdminOrg|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(ShopPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        ShopPO shopPO = convert(request);
        if (request.isPutNew()) {
            return putNew(shopPO);
        } else {
            return putUpdate(shopPO);
        }
    }

    private TeaMachineResult<Void> putNew(ShopPO po) {
        try {
            ShopPO exist = shopAccessor.getByShopCode(po.getTenantCode(), po.getShopCode());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = shopAccessor.insert(po);
            if (CommonConsts.NUM_ONE != inserted) {
                log.error("shopMgtService|putNew|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("shopMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(ShopPO po) {
        try {
            ShopPO exist = shopAccessor.getByShopCode(po.getTenantCode(), po.getShopCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = shopAccessor.update(po);
            if (CommonConsts.NUM_ONE != updated) {
                log.error("shopMgtService|putUpdate|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("shopMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String shopGroupCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(shopGroupCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            int deleted = shopAccessor.delete(tenantCode, shopGroupCode);
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("shopMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Integer> countByShopGroupCode(String tenantCode, String shopGroupCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(shopGroupCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            int cnt = shopAccessor.countByShopGroupCode(tenantCode, shopGroupCode);
            return TeaMachineResult.success(cnt);
        } catch (Exception e) {
            log.error("shopMgtService|countByShopGroupCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private List<ShopDTO> convert(List<ShopPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<ShopDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private ShopDTO convert(ShopPO po) {
        if (po == null) {
            return null;
        }

        ShopDTO dto = new ShopDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setShopCode(po.getShopCode());
        dto.setShopName(po.getShopName());
        dto.setShopType(po.getShopType());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());

        ShopGroupPO shopGroupPO = shopGroupAccessor.getByShopGroupCode(po.getTenantCode(), po.getShopGroupCode());
        if (shopGroupPO != null) {
            dto.setShopGroupCode(shopGroupPO.getShopGroupCode());
            dto.setShopGroupName(shopGroupPO.getShopGroupName());
        }

        return dto;
    }

    private ShopPO convert(ShopPutRequest request) {
        if (request == null) {
            return null;
        }

        ShopPO po = new ShopPO();
        po.setShopName(request.getShopName());
        po.setShopCode(request.getShopCode());
        po.setShopGroupCode(request.getShopGroupCode());
        po.setShopType(request.getShopType());
        po.setComment(request.getComment());
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        return po;
    }
}
