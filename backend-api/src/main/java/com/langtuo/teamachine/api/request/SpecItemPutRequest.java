package com.langtuo.teamachine.api.request;

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

    public String getSpecItemCode() {
        return specItemCode;
    }

    public void setSpecItemCode(String specItemCode) {
        this.specItemCode = specItemCode;
    }

    public String getSpecItemName() {
        return specItemName;
    }

    public void setSpecItemName(String specItemName) {
        this.specItemName = specItemName;
    }

    public String getOuterSpecItemCode() {
        return outerSpecItemCode;
    }

    public void setOuterSpecItemCode(String outerSpecItemCode) {
        this.outerSpecItemCode = outerSpecItemCode;
    }
}