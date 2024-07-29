package com.langtuo.teamachine.api.model.drinkset;

import lombok.Data;

import java.util.Date;

@Data
public class ToppingAdjustRuleDTO {
    /**
     * 数据表id
     */
    private long id;

    /**
     * 数据表记录插入时间
     */
    private Date gmtCreated;

    /**
     * 数据表记录最近修改时间
     */
    private Date gmtModified;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 茶饮编码
     */
    private String teaCode;

    /**
     * 茶品unit编码
     */
    private String teaUnitCode;

    /**
     * 步骤，从1开始
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
     * 调整模式，0：减少，1：添加
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

    /**
     * 物料名称
     */
    private String toppingName;

    /**
     * 计量单位，0：克，1：毫升
     */
    private int measureUnit;
}
