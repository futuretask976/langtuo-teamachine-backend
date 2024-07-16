package com.langtuo.teamachine.api.request;

import java.util.Map;

public class ToppingAccuracyTemplatePutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 状态，0：禁用，1：启用
     */
    private int state;

    /**
     * 溢出允许单位，0：固定值，1：百分比
     */
    private int overUnit;

    /**
     * 溢出数值
     */
    private int overAmount;

    /**
     * 不及允许单位，0：固定值，1：百分比
     */
    private int underUnit;

    /**
     * 不及数值
     */
    private int underAmount;

    /**
     * 应用物料编码
     */
    private String toppingCode;

    /**
     * 备注
     */
    private String comment;

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public Map<String, String> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, String> extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getOverUnit() {
        return overUnit;
    }

    public void setOverUnit(int overUnit) {
        this.overUnit = overUnit;
    }

    public int getOverAmount() {
        return overAmount;
    }

    public void setOverAmount(int overAmount) {
        this.overAmount = overAmount;
    }

    public int getUnderUnit() {
        return underUnit;
    }

    public void setUnderUnit(int underUnit) {
        this.underUnit = underUnit;
    }

    public int getUnderAmount() {
        return underAmount;
    }

    public void setUnderAmount(int underAmount) {
        this.underAmount = underAmount;
    }

    public String getToppingCode() {
        return toppingCode;
    }

    public void setToppingCode(String toppingCode) {
        this.toppingCode = toppingCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
