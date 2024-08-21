package com.langtuo.teamachine.biz.service.excel;

import lombok.Data;

@Data
public class ToppingBaseRuleExcel {
    /**
     * 步骤序号
     */
    private int stepIndex;

    /**
     * 物料名称
     */
    private String toppingName;

    /**
     * 基础用量
     */
    private int baseAmount;
}
