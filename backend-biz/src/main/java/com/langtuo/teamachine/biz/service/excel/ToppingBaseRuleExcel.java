package com.langtuo.teamachine.biz.service.excel;

import lombok.Data;

@Data
public class ToppingBaseRuleExcel {
    /**
     * 步骤序号
     */
    private int stepIndex;

    /**
     * 物料编码
     */
    private String toppingCode;

    /**
     * 基础用量
     */
    private int baseAmount;
}
