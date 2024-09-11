package com.langtuo.teamachine.api.model.report;

import lombok.Data;

import java.util.Date;

@Data
public class OrderTeaReportByShopDTO {
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
     * 订单创建时间，例如 2024-09-20
     */
    private String orderCreatedDay;

    /**
     * 店铺组编码
     */
    private String shopGroupCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 茶品编码
     */
    private String teaCode;

    /**
     * 数量
     */
    private int amount;
}
