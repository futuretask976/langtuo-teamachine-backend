package com.langtuo.teamachine.biz.convert.drink;

import com.langtuo.teamachine.api.model.drink.ToppingDTO;
import com.langtuo.teamachine.api.request.drink.ToppingPutRequest;
import com.langtuo.teamachine.dao.po.drink.ToppingPO;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ToppingMgtConvertor {
    public static List<ToppingDTO> convertToToppingDTO(List<ToppingPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<ToppingDTO> list = poList.stream()
                .map(po -> convertToToppingDTO(po))
                .collect(Collectors.toList());
        return list;
    }

    public static ToppingDTO convertToToppingDTO(ToppingPO po) {
        if (po == null) {
            return null;
        }

        ToppingDTO dto = new ToppingDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setToppingTypeCode(po.getToppingTypeCode());
        dto.setToppingCode(po.getToppingCode());
        dto.setToppingName(po.getToppingName());
        dto.setState(po.getState());
        dto.setValidHourPeriod(po.getValidHourPeriod());
        dto.setCleanHourPeriod(po.getCleanHourPeriod());
        dto.setMeasureUnit(po.getMeasureUnit());
        dto.setConvertCoefficient(po.getConvertCoefficient());
        dto.setFlowSpeed(po.getFlowSpeed());
        dto.setComment(po.getComment());
        po.setExtraInfo(po.getExtraInfo());
        return dto;
    }

    public static ToppingPO convertToToppingDTO(ToppingPutRequest request) {
        if (request == null) {
            return null;
        }

        ToppingPO po = new ToppingPO();
        po.setToppingTypeCode(request.getToppingTypeCode());
        po.setToppingName(request.getToppingName());
        po.setToppingCode(request.getToppingCode());
        po.setState(request.getState());
        po.setValidHourPeriod(request.getValidHourPeriod());
        po.setCleanHourPeriod(request.getCleanHourPeriod());
        po.setMeasureUnit(request.getMeasureUnit());
        po.setConvertCoefficient(request.getConvertCoefficient());
        po.setFlowSpeed(request.getFlowSpeed());
        po.setTenantCode(request.getTenantCode());
        po.setComment(request.getComment());
        po.setExtraInfo(po.getExtraInfo());
        return po;
    }
}
