package com.langtuo.teamachine.api.request;

import java.util.Map;

public class ToppingAdjustRulePutRequest {
    /**
     *
     */
    private int stepIndex;

    /**
     * 物料编码
     */
    private String toppingCode;

    /**
     *
     */
    private int baseAmount;

    /**
     *
     */
    private String adjustMode;

    /**
     *
     */
    private String adjustUnit;

    /**
     *
     */
    private int adjustAmount;

    /**
     *
     */
    private int actualAmount;

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    public void setBaseAmount(int baseAmount) {
        this.baseAmount = baseAmount;
    }

    public void setAdjustAmount(int adjustAmount) {
        this.adjustAmount = adjustAmount;
    }

    public int getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(int actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getToppingCode() {
        return toppingCode;
    }

    public void setToppingCode(String toppingCode) {
        this.toppingCode = toppingCode;
    }

    public Integer getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(Integer baseAmount) {
        this.baseAmount = baseAmount;
    }

    public Integer getAdjustAmount() {
        return adjustAmount;
    }

    public void setAdjustAmount(Integer adjustAmount) {
        this.adjustAmount = adjustAmount;
    }

    public String getAdjustMode() {
        return adjustMode;
    }

    public void setAdjustMode(String adjustMode) {
        this.adjustMode = adjustMode;
    }

    public String getAdjustUnit() {
        return adjustUnit;
    }

    public void setAdjustUnit(String adjustUnit) {
        this.adjustUnit = adjustUnit;
    }

    public Integer getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(Integer stepIndex) {
        this.stepIndex = stepIndex;
    }
}
