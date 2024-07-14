package com.langtuo.teamachine.dao.po;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class TeaUnitPO {
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
     * 茶编码
     */
    private String teaCode;

    /**
     * 茶饮PDU编码
     */
    private String teaUnitCode;

    /**
     *
     */
    private String teaUnitName;

    /**
     * 规格子项编码
     */
    private String specItemCode;
}
