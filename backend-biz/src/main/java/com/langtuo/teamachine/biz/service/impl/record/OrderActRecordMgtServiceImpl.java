package com.langtuo.teamachine.biz.service.impl.record;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.SpecDTO;
import com.langtuo.teamachine.api.model.drink.SpecItemDTO;
import com.langtuo.teamachine.api.model.drink.ToppingDTO;
import com.langtuo.teamachine.api.model.record.OrderActRecordDTO;
import com.langtuo.teamachine.api.model.record.OrderSpecItemActRecordDTO;
import com.langtuo.teamachine.api.model.record.OrderToppingActRecordDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.model.shop.ShopGroupDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drink.SpecMgtService;
import com.langtuo.teamachine.api.service.drink.ToppingMgtService;
import com.langtuo.teamachine.api.service.record.OrderActRecordMgtService;
import com.langtuo.teamachine.api.service.shop.ShopGroupMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.dao.accessor.record.OrderActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.record.OrderSpecItemActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.record.OrderToppingActRecordAccessor;
import com.langtuo.teamachine.dao.po.record.OrderActRecordPO;
import com.langtuo.teamachine.dao.po.record.OrderSpecItemActRecordPO;
import com.langtuo.teamachine.dao.po.record.OrderToppingActRecordPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.LangTuoResult.getModel;

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
    private ShopGroupMgtService shopGroupMgtService;

    @Resource
    private ShopMgtService shopMgtService;

    @Resource
    private ToppingMgtService toppingMgtService;

    @Resource
    private SpecMgtService specMgtService;

    @Override
    public LangTuoResult<OrderActRecordDTO> get(String tenantCode, String idempotentMark) {
        LangTuoResult<OrderActRecordDTO> langTuoResult = null;
        try {
            OrderActRecordPO po = orderActRecordAccessor.selectOne(tenantCode, idempotentMark);
            OrderActRecordDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<OrderActRecordDTO>> list(String tenantCode) {
        LangTuoResult<List<OrderActRecordDTO>> langTuoResult = null;
        try {
            List<OrderActRecordPO> poList = orderActRecordAccessor.selectList(tenantCode);
            List<OrderActRecordDTO> dtoList = convert(poList);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<OrderActRecordDTO>> search(String tenantCode, String shopGroupCode,
            String shopCode, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<OrderActRecordDTO>> langTuoResult = null;
        try {
            PageInfo<OrderActRecordPO> pageInfo = orderActRecordAccessor.search(tenantCode, shopGroupCode,
                    shopCode, pageNum, pageSize);
            List<OrderActRecordDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String warningRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = orderActRecordAccessor.delete(tenantCode, warningRuleCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
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

        ShopGroupDTO shopGroupDTO = getModel(shopGroupMgtService.getByCode(
                po.getTenantCode(), po.getShopGroupCode()));
        if (shopGroupDTO != null) {
            dto.setShopGroupName(shopGroupDTO.getShopGroupName());
        }
        ShopDTO shopDTO = getModel(shopMgtService.getByCode(po.getTenantCode(), po.getShopCode()));
        if (shopDTO != null) {
            dto.setShopName(shopDTO.getShopName());
        }

        List<OrderSpecItemActRecordPO> specItemActRecordList = orderSpecItemActRecordAccessor.selectList(
                po.getTenantCode(), po.getIdempotentMark());
        dto.setOrderSpecItemActRecordList(convertToOrderSpecItemActRecordDTO(specItemActRecordList));

        List<OrderToppingActRecordPO> toppingActRecordList = orderToppingActRecordAccessor.selectList(
                po.getTenantCode(), po.getIdempotentMark());
        dto.setOrderToppingActRecordList(convertToOrderToppingActRecordDTO((toppingActRecordList)));

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

        SpecDTO specDTO = getModel(specMgtService.getByCode(po.getTenantCode(), po.getSpecCode()));
        dto.setSpecName(specDTO.getSpecName());
        SpecItemDTO specItemDTO = getModel(specMgtService.getSpecItemByCode(
                po.getTenantCode(), po.getSpecCode(), po.getSpecItemCode()));
        dto.setSpecItemName(specItemDTO.getSpecItemName());
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

        ToppingDTO toppingDTO = getModel(toppingMgtService.getByCode(po.getTenantCode(), po.getToppingCode()));
        dto.setToppingName(toppingDTO.getToppingName());
        return dto;
    }
}
