package com.langtuo.teamachine.api.request.drinkset;

import lombok.Data;

import java.util.List;

@Data
public class TeaUnitPutRequest {
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
    private List<SpecItemRulePutRequest> specItemRuleList;

    /**
     *
     */
    private List<ToppingAdjustRulePutRequest> toppingAdjustRuleList;
}
