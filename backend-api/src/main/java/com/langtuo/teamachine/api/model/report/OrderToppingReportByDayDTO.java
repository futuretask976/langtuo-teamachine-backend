package com.langtuo.teamachine.api.model.report;

import lombok.Data;

@Data
public class OrderToppingReportByDayDTO {
    /**
     * 订单创建时间，例如 2024-09-20
     */
    private String orderCreatedDay;

    /**
     * 物料编码
     */
    private String toppingCode;

    /**
     * 数量
     */
    private int amount;
}
