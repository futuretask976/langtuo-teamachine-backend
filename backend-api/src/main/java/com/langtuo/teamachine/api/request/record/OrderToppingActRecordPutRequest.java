package com.langtuo.teamachine.api.request.record;

import lombok.Data;

@Data
public class OrderToppingActRecordPutRequest {
    /**
     * 步骤序号
     */
    private int stepIndex;

    /**
     * 设备编码
     */
    private String toppingCode;

    /**
     * 设备名称
     */
    private String toppingName;

    /**
     * 实际数量
     */
    private int actualAmount;
}
