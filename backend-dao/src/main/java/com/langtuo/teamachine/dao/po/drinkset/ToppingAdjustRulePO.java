package com.langtuo.teamachine.dao.po.drinkset;

import lombok.Data;

import java.util.Date;

@Data
public class ToppingAdjustRulePO {
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
     * 调整模式，fix：固定值，percentage：百分比
     */
    private String adjustMode;

    /**
     * 计量单位，0：克，1：毫升
     */
    private String adjustUnit;

    /**
     * 调整用量
     */
    private int adjustAmount;

    /**
     * 实际用量
     */
    private int actualAmount;
}
