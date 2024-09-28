package com.langtuo.teamachine.api.model.report;

import lombok.Data;

@Data
public class OrderTeaReportByDayDTO {
    /**
     * 订单创建时间，例如 2024-09-20
     */
    private String orderCreatedDay;

    /**
     * 茶品编码
     */
    private String teaCode;

    /**
     * 数量
     */
    private int amount;
}
