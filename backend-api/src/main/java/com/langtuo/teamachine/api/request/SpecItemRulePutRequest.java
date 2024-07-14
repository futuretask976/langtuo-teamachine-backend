package com.langtuo.teamachine.api.request;

public class SpecItemRulePutRequest {
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

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

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
