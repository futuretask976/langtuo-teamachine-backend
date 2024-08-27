package com.langtuo.teamachine.biz.service.excel.export;

import lombok.Data;

@Data
public class ToppingAdjustRulePart {
    /**
     * 步骤序号
     */
    private int stepIndex;

    /**
     * 物料编码
     */
    private String toppingCode;

    /**
     * 物料名称
     */
    private String toppingName;

    /**
     * 调整类型，0：减少，1：添加
     */
    private int adjustType;

    /**
     * 调整模式，0：固定值，1：百分比
     */
    private int adjustMode;

    /**
     * 调整用量
     */
    private int adjustAmount;
}
