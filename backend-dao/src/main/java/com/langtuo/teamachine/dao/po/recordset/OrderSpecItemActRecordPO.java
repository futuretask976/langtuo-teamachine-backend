package com.langtuo.teamachine.dao.po.recordset;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class OrderSpecItemActRecordPO {
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
     * 幂等标记
     */
    private String idempotentMark;

    /**
     * 规格编码
     */
    private String specCode;

    /**
     * 规格项编码
     */
    private String specItemCode;
}
