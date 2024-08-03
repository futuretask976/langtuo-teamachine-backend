package com.langtuo.teamachine.api.model.record;

import lombok.Data;

@Data
public class OrderToppingActRecordDTO {
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
