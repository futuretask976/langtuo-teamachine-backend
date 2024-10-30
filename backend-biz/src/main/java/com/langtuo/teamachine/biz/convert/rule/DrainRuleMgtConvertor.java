package com.langtuo.teamachine.biz.convert.rule;

import com.langtuo.teamachine.api.model.rule.DrainRuleDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleToppingDTO;
import com.langtuo.teamachine.api.request.rule.DrainRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.DrainRulePutRequest;
import com.langtuo.teamachine.dao.accessor.drink.ToppingAccessor;
import com.langtuo.teamachine.dao.accessor.rule.DrainRuleToppingAccessor;
import com.langtuo.teamachine.dao.po.drink.ToppingPO;
import com.langtuo.teamachine.dao.po.rule.*;
import com.langtuo.teamachine.dao.util.SpringAccessorUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DrainRuleMgtConvertor {
    public static List<DrainRuleDTO> convertToDrainRuleDTO(List<DrainRulePO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<DrainRuleDTO> list = poList.stream()
                .map(po -> convertToDrainRuleDTO(po))
                .collect(Collectors.toList());
        return list;
    }

    public static DrainRuleDTO convertToDrainRuleDTO(DrainRulePO po) {
        if (po == null) {
            return null;
        }

        DrainRuleDTO dto = new DrainRuleDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTenantCode(po.getTenantCode());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setDrainRuleCode(po.getDrainRuleCode());
        dto.setDrainRuleName(po.getDrainRuleName());

        DrainRuleToppingAccessor drainRuleToppingAccessor = SpringAccessorUtils.getDrainRuleToppingAccessor();
        List<DrainRuleToppingPO> poList = drainRuleToppingAccessor.listByDrainRuleCode(
                po.getTenantCode(), po.getDrainRuleCode());
        if (!CollectionUtils.isEmpty(poList)) {
            dto.setToppingRuleList(convertToDrainRuleToppingDTO(poList));
        }
        return dto;
    }

    public static List<DrainRuleToppingDTO> convertToDrainRuleToppingDTO(List<DrainRuleToppingPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<DrainRuleToppingDTO> list = poList.stream()
                .map(po -> {
                    DrainRuleToppingDTO dto = new DrainRuleToppingDTO();
                    dto.setTenantCode(po.getTenantCode());
                    dto.setDrainRuleCode(po.getDrainRuleCode());
                    dto.setToppingCode(po.getToppingCode());
                    dto.setFlushSec(po.getFlushSec());

                    ToppingAccessor toppingAccessor = SpringAccessorUtils.getToppingAccessor();
                    ToppingPO toppingPO = toppingAccessor.getByToppingCode(
                            po.getTenantCode(), po.getToppingCode());
                    if (toppingPO != null) {
                        dto.setToppingName(toppingPO.getToppingName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
        return list;
    }

    public static DrainRulePO convertToDrainRulePO(DrainRulePutRequest request) {
        if (request == null) {
            return null;
        }

        DrainRulePO po = new DrainRulePO();
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setDrainRuleCode(request.getDrainRuleCode());
        po.setDrainRuleName(request.getDrainRuleName());
        return po;
    }

    public static List<DrainRuleToppingPO> convertToDrainRuleIncludePO(DrainRulePutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getToppingRuleList())) {
            return null;
        }

        List<DrainRuleToppingPO> list = request.getToppingRuleList().stream()
                .filter(Objects::nonNull)
                .map(openRuleToppingPutRequest -> {
                    DrainRuleToppingPO po = new DrainRuleToppingPO();
                    po.setTenantCode(request.getTenantCode());
                    po.setDrainRuleCode(request.getDrainRuleCode());
                    po.setToppingCode(openRuleToppingPutRequest.getToppingCode());
                    po.setFlushSec(openRuleToppingPutRequest.getFlushSec());
                    return po;
                }).collect(Collectors.toList());
        return list;
    }

    public static List<DrainRuleDispatchPO> convertToDrainRuleDTO(DrainRuleDispatchPutRequest request) {
        String tenantCode = request.getTenantCode();
        String openRuleCode = request.getDrainRuleCode();

        return request.getShopGroupCodeList().stream()
                .map(shopGroupCode -> {
                    DrainRuleDispatchPO po = new DrainRuleDispatchPO();
                    po.setTenantCode(tenantCode);
                    po.setDrainRuleCode(openRuleCode);
                    po.setShopGroupCode(shopGroupCode);
                    return po;
                }).collect(Collectors.toList());
    }
}
