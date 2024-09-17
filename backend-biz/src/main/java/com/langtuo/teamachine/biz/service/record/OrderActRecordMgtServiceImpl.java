package com.langtuo.teamachine.biz.service.record;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.OrderActRecordDTO;
import com.langtuo.teamachine.api.model.record.OrderSpecItemActRecordDTO;
import com.langtuo.teamachine.api.model.record.OrderToppingActRecordDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.OrderActRecordMgtService;
import com.langtuo.teamachine.api.service.shop.ShopGroupMgtService;
import com.langtuo.teamachine.dao.accessor.drink.*;
import com.langtuo.teamachine.dao.accessor.record.OrderActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.record.OrderSpecItemActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.record.OrderToppingActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.po.drink.*;
import com.langtuo.teamachine.dao.po.record.OrderActRecordPO;
import com.langtuo.teamachine.dao.po.record.OrderSpecItemActRecordPO;
import com.langtuo.teamachine.dao.po.record.OrderToppingActRecordPO;
import com.langtuo.teamachine.dao.po.shop.ShopGroupPO;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderActRecordMgtServiceImpl implements OrderActRecordMgtService {
    @Resource
    private OrderActRecordAccessor orderActRecordAccessor;

    @Resource
    private OrderSpecItemActRecordAccessor orderSpecItemActRecordAccessor;

    @Resource
    private OrderToppingActRecordAccessor orderToppingActRecordAccessor;

    @Resource
    private ShopGroupAccessor shopGroupAccessor;

    @Resource
    private ShopAccessor shopAccessor;

    @Resource
    private ToppingAccessor toppingAccessor;

    @Resource
    private SpecAccessor specAccessor;

    @Resource
    private SpecItemAccessor specItemAccessor;

    @Resource
    private ShopGroupMgtService shopGroupMgtService;

    @Resource
    private TeaTypeAccessor teaTypeAccessor;

    @Resource
    private TeaAccessor teaAccessor;

    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<OrderActRecordDTO> get(String tenantCode, String idempotentMark) {
        TeaMachineResult<OrderActRecordDTO> teaMachineResult;
        try {
            OrderActRecordPO po = orderActRecordAccessor.getByIdempotentMark(tenantCode, idempotentMark);
            OrderActRecordDTO dto = convert(po, true);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("orderActRecordMgtService|get|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<OrderActRecordDTO>> search(String tenantCode, String shopGroupCode,
            String shopCode, int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<OrderActRecordDTO>> teaMachineResult;
        try {
            PageInfo<OrderActRecordPO> pageInfo = null;
            if (!StringUtils.isBlank(shopCode)) {
                pageInfo = orderActRecordAccessor.searchByShopCode(tenantCode, Lists.newArrayList(shopCode),
                        pageNum, pageSize);
            } else if (!StringUtils.isBlank(shopGroupCode)) {
                pageInfo = orderActRecordAccessor.searchByShopGroupCode(tenantCode, Lists.newArrayList(shopGroupCode),
                        pageNum, pageSize);
            } else {
                List<String> shopGroupCodeList = DaoUtils.getShopGroupCodeListByAdmin(tenantCode);
                pageInfo = orderActRecordAccessor.searchByShopGroupCode(tenantCode, shopGroupCodeList,
                        pageNum, pageSize);
            }

            if (pageInfo == null) {
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        null, 0, pageNum, pageSize));
            } else {
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        convert(pageInfo.getList(), false), pageInfo.getTotal(), pageNum, pageSize));
            }
        } catch (Exception e) {
            log.error("orderActRecordMgtService|search|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String warningRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = orderActRecordAccessor.delete(tenantCode, warningRuleCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("orderActRecordMgtService|delete|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    private List<OrderActRecordDTO> convert(List<OrderActRecordPO> poList, boolean fillDetail) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<OrderActRecordDTO> list = poList.stream()
                .map(po -> convert(po, fillDetail))
                .collect(Collectors.toList());
        return list;
    }

    private OrderActRecordDTO convert(OrderActRecordPO po, boolean fillDetail) {
        if (po == null) {
            return null;
        }

        OrderActRecordDTO dto = new OrderActRecordDTO();
        dto.setExtraInfo(po.getExtraInfo());
        dto.setIdempotentMark(po.getIdempotentMark());
        dto.setMachineCode(po.getMachineCode());
        dto.setShopCode(po.getShopCode());
        dto.setShopGroupCode(po.getShopGroupCode());
        dto.setOrderGmtCreated(new Date());
        dto.setOuterOrderId(po.getOuterOrderId());
        dto.setState(po.getState());

        TeaPO teaPO = teaAccessor.getByTeaCode(po.getTenantCode(), po.getTeaCode());
        if (teaPO != null) {
            dto.setTeaName(teaPO.getTeaName());
        }

        if (fillDetail) {
            TeaTypePO teaTypePO = teaTypeAccessor.getByTeaTypeCode(po.getTenantCode(), po.getTeaTypeCode());
            if (teaTypePO != null) {
                dto.setTeaTypeName(teaTypePO.getTeaTypeName());
            }
            ShopGroupPO shopGroupPO = shopGroupAccessor.getByShopGroupCode(
                    po.getTenantCode(), po.getShopGroupCode());
            if (shopGroupPO != null) {
                dto.setShopGroupName(shopGroupPO.getShopGroupName());
            }
            ShopPO shopPO = shopAccessor.getByShopCode(po.getTenantCode(), po.getShopCode());
            if (shopPO != null) {
                dto.setShopName(shopPO.getShopName());
            }

            List<OrderSpecItemActRecordPO> specItemActRecordList = orderSpecItemActRecordAccessor.listByIdempotentMark(
                    po.getTenantCode(), po.getIdempotentMark());
            dto.setSpecItemList(convertToOrderSpecItemActRecordDTO(specItemActRecordList));

            List<OrderToppingActRecordPO> toppingActRecordList = orderToppingActRecordAccessor.listByIdempotentMark(
                    po.getTenantCode(), po.getIdempotentMark());
            dto.setToppingList(convertToOrderToppingActRecordDTO((toppingActRecordList)));
        }
        return dto;
    }

    private List<OrderSpecItemActRecordDTO> convertToOrderSpecItemActRecordDTO(List<OrderSpecItemActRecordPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        return poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
    }

    private OrderSpecItemActRecordDTO convert(OrderSpecItemActRecordPO po) {
        if (po == null) {
            return null;
        }

        OrderSpecItemActRecordDTO dto = new OrderSpecItemActRecordDTO();
        dto.setSpecCode(po.getSpecCode());
        dto.setSpecItemCode(po.getSpecItemCode());

        SpecPO specPO = specAccessor.getBySpecCode(po.getTenantCode(), po.getSpecCode());
        dto.setSpecName(specPO.getSpecName());
        SpecItemPO specItemPO = specItemAccessor.getBySpecItemCode(po.getTenantCode(), po.getSpecItemCode());
        dto.setSpecItemName(specItemPO.getSpecItemName());
        return dto;
    }

    private List<OrderToppingActRecordDTO> convertToOrderToppingActRecordDTO(List<OrderToppingActRecordPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        return poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
    }

    private OrderToppingActRecordDTO convert(OrderToppingActRecordPO po) {
        if (po == null) {
            return null;
        }

        OrderToppingActRecordDTO dto = new OrderToppingActRecordDTO();
        dto.setStepIndex(po.getStepIndex());
        dto.setToppingCode(po.getToppingCode());
        dto.setActualAmount(po.getActualAmount());

        ToppingPO toppingPO = toppingAccessor.getByToppingCode(po.getTenantCode(), po.getToppingCode());
        dto.setToppingName(toppingPO.getToppingName());
        return dto;
    }
}
