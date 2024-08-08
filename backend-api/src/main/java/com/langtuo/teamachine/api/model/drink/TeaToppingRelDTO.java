package com.langtuo.teamachine.api.model.drink;

import lombok.Data;

import java.util.Date;

@Data
public class TeaToppingRelDTO {
    /**
     * 茶编码
     */
    private String teaCode;

    /**
     * 步骤序号
     */
    private int stepIdx;

    /**
     * 物料编码
     */
    private String toppingCode;

    /**
     * 数量
     */
    private Integer amount;
}
