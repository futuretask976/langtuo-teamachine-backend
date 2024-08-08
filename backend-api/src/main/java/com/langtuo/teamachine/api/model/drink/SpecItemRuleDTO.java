package com.langtuo.teamachine.api.model.drink;

import lombok.Data;

import java.util.Date;

@Data
public class SpecItemRuleDTO {
    /**
     * 规格编码
     */
    private String specCode;

    /**
     * 规格项编码
     */
    private String specItemCode;

    /**
     * 规格项名称
     */
    private String specItemName;

    /**
     * 外部规格项编码
     */
    private String outerSpecItemCode;

    /**
     * 是否选中
     */
    private int selected;
}
