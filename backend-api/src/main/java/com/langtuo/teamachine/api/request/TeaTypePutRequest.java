package com.langtuo.teamachine.api.request;

import java.util.Map;

public class TeaTypePutRequest {
    /**
     * 茶类型编码
     */
    private String teaTypeCode;

    /**
     * 茶类型名称
     */
    private String teaTypeName;

    /**
     * 店铺类型，0：禁用，1：启用
     */
    private Integer state;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 备注
     */
    private String comment;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    public String getTeaTypeCode() {
        return teaTypeCode;
    }

    public void setTeaTypeCode(String teaTypeCode) {
        this.teaTypeCode = teaTypeCode;
    }

    public String getTeaTypeName() {
        return teaTypeName;
    }

    public void setTeaTypeName(String teaTypeName) {
        this.teaTypeName = teaTypeName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Map<String, String> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, String> extraInfo) {
        this.extraInfo = extraInfo;
    }
}
