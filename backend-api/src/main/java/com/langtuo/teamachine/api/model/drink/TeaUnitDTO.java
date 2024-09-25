package com.langtuo.teamachine.api.model.drink;

import lombok.Data;

import java.util.List;

@Data
public class TeaUnitDTO {
    /**
     * 茶品单位编码
     */
    private String teaUnitCode;

    /**
     * 茶品单位名称
     */
    private String teaUnitName;

    /**
     *
     */
    private List<ToppingAdjustRuleDTO> toppingAdjustRuleList;
}
