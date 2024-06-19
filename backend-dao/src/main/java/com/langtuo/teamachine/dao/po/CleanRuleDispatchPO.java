package com.langtuo.teamachine.dao.po;

import lombok.Data;

import java.util.Date;

@Data
public class CleanRuleDispatchPO {
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
     * 清洁规则编码
     */
    private String cleanRuleCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 租户编码
     */
    private String tenantCode;
}
