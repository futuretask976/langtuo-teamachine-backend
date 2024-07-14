package com.langtuo.teamachine.api.request;

import java.util.List;

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

    public List<SpecItemRulePutRequest> getSpecItemRuleList() {
        return specItemRuleList;
    }

    public void setSpecItemRuleList(List<SpecItemRulePutRequest> specItemRuleList) {
        this.specItemRuleList = specItemRuleList;
    }

    public List<ToppingAdjustRulePutRequest> getToppingAdjustRuleList() {
        return toppingAdjustRuleList;
    }

    public void setToppingAdjustRuleList(List<ToppingAdjustRulePutRequest> toppingAdjustRuleList) {
        this.toppingAdjustRuleList = toppingAdjustRuleList;
    }

    public String getTeaUnitCode() {
        return teaUnitCode;
    }

    public void setTeaUnitCode(String teaUnitCode) {
        this.teaUnitCode = teaUnitCode;
    }

    public String getTeaUnitName() {
        return teaUnitName;
    }

    public void setTeaUnitName(String teaUnitName) {
        this.teaUnitName = teaUnitName;
    }
}
