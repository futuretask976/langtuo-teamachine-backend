package com.langtuo.teamachine.dao.po.report;

import lombok.Data;

import java.util.Date;

@Data
public class OrderAmtReportPO {
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
     * 订单创建时间，例如 2024-09-20
     */
    private String orderCreatedDay;

    /**
     * 数量
     */
    private int amount;
}
