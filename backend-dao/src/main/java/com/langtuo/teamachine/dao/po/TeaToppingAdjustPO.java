package com.langtuo.teamachine.dao.po;

import lombok.Data;

import java.util.Date;

@Data
public class TeaToppingAdjustPO {
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
     * 茶饮PDU编码
     */
    private String teaUnitCode;

    /**
     * 茶饮编码
     */
    private String teaCode;

    /**
     * 物料编码
     */
    private String toppingCode;

    /**
     * 调整数量
     */
    private Integer adjustAmount;

    /**
     * 租户编码
     */
    private String tenantCode;
}
