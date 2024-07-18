package com.langtuo.teamachine.api.request.drinkset;

import lombok.Data;

@Data
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
}
