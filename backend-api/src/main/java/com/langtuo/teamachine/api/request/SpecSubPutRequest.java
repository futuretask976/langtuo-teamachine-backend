package com.langtuo.teamachine.api.request;

public class SpecSubPutRequest {
    /**
     * 子规格编码
     */
    private String specSubCode;

    /**
     * 子规格名称
     */
    private String specSubName;

    /**
     * 外部子规格编码
     */
    private String outerSpecSubCode;

    public String getSpecSubCode() {
        return specSubCode;
    }

    public void setSpecSubCode(String specSubCode) {
        this.specSubCode = specSubCode;
    }

    public String getSpecSubName() {
        return specSubName;
    }

    public void setSpecSubName(String specSubName) {
        this.specSubName = specSubName;
    }

    public String getOuterSpecSubCode() {
        return outerSpecSubCode;
    }

    public void setOuterSpecSubCode(String outerSpecSubCode) {
        this.outerSpecSubCode = outerSpecSubCode;
    }
}
