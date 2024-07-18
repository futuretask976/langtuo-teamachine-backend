package com.langtuo.teamachine.api.request;

import lombok.Data;

@Data
public class SpecItemPutRequest {
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
}
