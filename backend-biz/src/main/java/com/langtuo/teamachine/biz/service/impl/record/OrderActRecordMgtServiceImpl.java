package com.langtuo.teamachine.biz.service.impl.record;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.OrderActRecordDTO;
import com.langtuo.teamachine.api.model.record.OrderSpecItemActRecordDTO;
import com.langtuo.teamachine.api.model.record.OrderToppingActRecordDTO;
import com.langtuo.teamachine.api.model.shop.ShopGroupDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.OrderActRecordMgtService;
import com.langtuo.teamachine.api.service.shop.ShopGroupMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
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
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.TeaMachineResult.getListModel;

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

    @Override
    public TeaMachineResult<OrderActRecordDTO> get(String tenantCode, String idempotentMark) {
        TeaMachineResult<OrderActRecordDTO> teaMachineResult;
        try {
            OrderActRecordPO po = orderActRecordAccessor.selectOne(tenantCode, idempotentMark);
            OrderActRecordDTO dto = convert(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
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
            if (CollectionUtils.isEmpty(shopGroupCodeList) && CollectionUtils.isEmpty(shopCodeList)) {
                List<ShopGroupDTO> shopGroupDTOList = getListModel(shopGroupMgtService.listByAdminOrg(tenantCode));
                shopGroupCodeList = shopGroupDTOList.stream()
                        .map(shopGroupDTO -> shopGroupDTO.getShopGroupCode())
                        .collect(Collectors.toList());
            }

            PageInfo<OrderActRecordPO> pageInfo = orderActRecordAccessor.search(tenantCode, shopGroupCodeList,
                    shopCodeList, pageNum, pageSize);
            List<OrderActRecordDTO> dtoList = convert(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String warningRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = orderActRecordAccessor.delete(tenantCode, warningRuleCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
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

        ShopGroupPO shopGroupPO = shopGroupAccessor.selectOneByCode(
                po.getTenantCode(), po.getShopGroupCode());
        if (shopGroupPO != null) {
            dto.setShopGroupName(shopGroupPO.getShopGroupName());
        }
        ShopPO shopPO = shopAccessor.selectOneByCode(po.getTenantCode(), po.getShopCode());
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
        SpecItemPO specItemPO = specItemAccessor.selectOneBySpecItemCode(
                po.getTenantCode(), po.getSpecCode(), po.getSpecItemCode());
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
}
