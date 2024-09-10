package com.langtuo.teamachine.biz.service.impl.record;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.OrderActRecordDTO;
import com.langtuo.teamachine.api.model.record.OrderSpecItemActRecordDTO;
import com.langtuo.teamachine.api.model.record.OrderToppingActRecordDTO;
import com.langtuo.teamachine.api.request.record.OrderActRecordPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.OrderActRecordMgtService;
import com.langtuo.teamachine.api.service.shop.ShopGroupMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.util.ApiUtils;
import com.langtuo.teamachine.biz.service.util.BizUtils;
import com.langtuo.teamachine.dao.accessor.drink.SpecAccessor;
import com.langtuo.teamachine.dao.accessor.drink.SpecItemAccessor;
import com.langtuo.teamachine.dao.accessor.drink.ToppingAccessor;
import com.langtuo.teamachine.dao.accessor.record.OrderActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.record.OrderSpecItemActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.record.OrderToppingActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.po.drink.SpecItemPO;
import com.langtuo.teamachine.dao.po.drink.SpecPO;
import com.langtuo.teamachine.dao.po.drink.ToppingPO;
import com.langtuo.teamachine.dao.po.record.OrderActRecordPO;
import com.langtuo.teamachine.dao.po.record.OrderSpecItemActRecordPO;
import com.langtuo.teamachine.dao.po.record.OrderToppingActRecordPO;
import com.langtuo.teamachine.dao.po.shop.ShopGroupPO;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<OrderActRecordDTO> get(String tenantCode, String idempotentMark) {
        TeaMachineResult<OrderActRecordDTO> teaMachineResult;
        try {
            OrderActRecordPO po = orderActRecordAccessor.selectOne(tenantCode, idempotentMark);
            OrderActRecordDTO dto = convert(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<OrderActRecordDTO>> search(String tenantCode, List<String> shopGroupCodeList,
            List<String> shopCodeList, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<OrderActRecordDTO>> teaMachineResult;
        try {
            if (CollectionUtils.isEmpty(shopCodeList)) {
                if (CollectionUtils.isEmpty(shopGroupCodeList)) {
                    shopGroupCodeList = BizUtils.getShopGroupCodeListByAdmin(tenantCode);
                }
                shopCodeList = BizUtils.getShopCodeListByShopGroupCode(tenantCode, shopGroupCodeList);
            }

            if (CollectionUtils.isEmpty(shopCodeList)) {
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        null, 0, pageNum, pageSize));
            } else {
                PageInfo<OrderActRecordPO> pageInfo = orderActRecordAccessor.search(
                        tenantCode, shopCodeList, pageNum, pageSize);
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        convert(pageInfo.getList()), pageInfo.getTotal(), pageNum, pageSize));
            }
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String warningRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = orderActRecordAccessor.delete(tenantCode, warningRuleCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(OrderActRecordPutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        OrderActRecordPO po = convertToOrderActRecordPO(request);
        List<OrderSpecItemActRecordPO> orderSpecItemActRecordPOList = convertToSpecItemActRecordPO(request);
        List<OrderToppingActRecordPO> orderToppingActRecordPOList = convertToOrderToppingActRecordPO(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            OrderActRecordPO exist = orderActRecordAccessor.selectOne(po.getTenantCode(),
                    po.getIdempotentMark());
            if (exist != null) {
                int updated = orderActRecordAccessor.delete(po.getTenantCode(), po.getIdempotentMark());
            }
            int inserted = orderActRecordAccessor.insert(po);

            int deleted4SpecItemActRec = orderSpecItemActRecordAccessor.delete(po.getTenantCode(),
                    po.getIdempotentMark());
            for (OrderSpecItemActRecordPO orderSpecItemActRecordPO : orderSpecItemActRecordPOList) {
                int inserted4SpecItemActRec = orderSpecItemActRecordAccessor.insert(orderSpecItemActRecordPO);
            }

            int deleted4ToppingActRec = orderToppingActRecordAccessor.delete(po.getTenantCode(),
                    po.getIdempotentMark());
            for (OrderToppingActRecordPO orderToppingActRecordPO : orderToppingActRecordPOList) {
                int inserted4ToppingActRec = orderToppingActRecordAccessor.insert(orderToppingActRecordPO);
            }

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    private List<OrderActRecordDTO> convert(List<OrderActRecordPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<OrderActRecordDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private OrderActRecordDTO convert(OrderActRecordPO po) {
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

        ShopGroupPO shopGroupPO = shopGroupAccessor.selectOneByShopGroupCode(
                po.getTenantCode(), po.getShopGroupCode());
        if (shopGroupPO != null) {
            dto.setShopGroupName(shopGroupPO.getShopGroupName());
        }
        ShopPO shopPO = shopAccessor.selectOneByShopCode(po.getTenantCode(), po.getShopCode());
        if (shopPO != null) {
            dto.setShopName(shopPO.getShopName());
        }

        List<OrderSpecItemActRecordPO> specItemActRecordList = orderSpecItemActRecordAccessor.selectList(
                po.getTenantCode(), po.getIdempotentMark());
        dto.setSpecItemList(convertToOrderSpecItemActRecordDTO(specItemActRecordList));

        List<OrderToppingActRecordPO> toppingActRecordList = orderToppingActRecordAccessor.selectList(
                po.getTenantCode(), po.getIdempotentMark());
        dto.setToppingList(convertToOrderToppingActRecordDTO((toppingActRecordList)));

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

        SpecPO specPO = specAccessor.selectOneBySpecCode(po.getTenantCode(), po.getSpecCode());
        dto.setSpecName(specPO.getSpecName());
        SpecItemPO specItemPO = specItemAccessor.selectOneBySpecItemCode(po.getTenantCode(), po.getSpecItemCode());
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

        ToppingPO toppingPO = toppingAccessor.selectOneByToppingCode(po.getTenantCode(), po.getToppingCode());
        dto.setToppingName(toppingPO.getToppingName());
        return dto;
    }

    private OrderActRecordPO convertToOrderActRecordPO(OrderActRecordPutRequest request) {
        if (request == null) {
            return null;
        }

        OrderActRecordPO po = new OrderActRecordPO();
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setIdempotentMark(request.getIdempotentMark());
        po.setMachineCode(request.getMachineCode());
        po.setShopCode(request.getShopCode());
        po.setShopGroupCode(request.getShopGroupCode());
        po.setOrderGmtCreated(request.getOrderGmtCreated());
        po.setTeaTypeCode(request.getTeaTypeCode());
        po.setTeaCode(request.getTeaCode());
        po.setOuterOrderId(request.getOuterOrderId());
        po.setState(request.getState());
        return po;
    }

    private List<OrderSpecItemActRecordPO> convertToSpecItemActRecordPO(OrderActRecordPutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getSpecItemList())) {
            return null;
        }

        List<OrderSpecItemActRecordPO> specItemList = request.getSpecItemList().stream()
                .map(orderSpecItemActRecordPutRequest -> {
                    OrderSpecItemActRecordPO po = new OrderSpecItemActRecordPO();
                    po.setTenantCode(request.getTenantCode());
                    po.setIdempotentMark(request.getIdempotentMark());
                    po.setSpecCode(orderSpecItemActRecordPutRequest.getSpecCode());
                    po.setSpecItemCode(orderSpecItemActRecordPutRequest.getSpecItemCode());
                    return po;
                }).collect(Collectors.toList());
        return specItemList;
    }

    private static List<OrderToppingActRecordPO> convertToOrderToppingActRecordPO(OrderActRecordPutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getSpecItemList())) {
            return null;
        }

        List<OrderToppingActRecordPO> specItemList = request.getToppingList().stream()
                .map(orderToppingActRecordPutRequest -> {
                    OrderToppingActRecordPO po = new OrderToppingActRecordPO();
                    po.setTenantCode(request.getTenantCode());
                    po.setIdempotentMark(request.getIdempotentMark());
                    po.setStepIndex(orderToppingActRecordPutRequest.getStepIndex());
                    po.setToppingCode(orderToppingActRecordPutRequest.getToppingCode());
                    po.setActualAmount(orderToppingActRecordPutRequest.getActualAmount());
                    return po;
                }).collect(Collectors.toList());
        return specItemList;
    }
}
