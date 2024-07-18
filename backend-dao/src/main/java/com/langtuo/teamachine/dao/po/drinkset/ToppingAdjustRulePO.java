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
     * 茶饮PDU编码
     */
    private String teaUnitCode;

    /**
     *
     */
    private int stepIndex;

    /**
     *
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
