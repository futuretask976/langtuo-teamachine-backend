package com.langtuo.teamachine.api.request;

public class TeaToppingRelPutRequest {
    /**
     * 物料编码
     */
    private String toppingCode;

    /**
     * 数量
     */
    private Integer amount;

    public String getToppingCode() {
        return toppingCode;
    }

    public void setToppingCode(String toppingCode) {
        this.toppingCode = toppingCode;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
