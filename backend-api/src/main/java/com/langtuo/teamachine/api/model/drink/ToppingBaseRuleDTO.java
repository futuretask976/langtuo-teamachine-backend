package com.langtuo.teamachine.api.model.drink;

import lombok.Data;

import java.util.Date;

@Data
public class ToppingBaseRuleDTO {
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

    /**
     * 物料名称
     */
    private String toppingName;

    /**
     * 计量单位，0：克，1：毫升
     */
    private Integer measureUnit;

    /**
     * 状态，0：禁用，1：启用
     */
    private Integer state;
}
