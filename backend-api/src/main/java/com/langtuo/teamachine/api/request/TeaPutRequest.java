package com.langtuo.teamachine.api.request;

import java.util.List;
import java.util.Map;

public class TeaPutRequest {
    /**
     * 茶编码
     */
    private String teaCode;

    /**
     * 茶名称
     */
    private String teaName;

    /**
     * 外部茶编码
     */
    private String outerTeaCode;

    /**
     * 状态，0：禁用，1：启用
     */
    private Integer state;

    /**
     * 茶类型编码
     */
    private String teaTypeCode;

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

    /**
     * 茶-物料关系列表
     */
    private List<TeaUnitPutRequest> teaUnitList;

    public List<TeaUnitPutRequest> getTeaUnitList() {
        return teaUnitList;
    }

    public void setTeaUnitList(List<TeaUnitPutRequest> teaUnitList) {
        this.teaUnitList = teaUnitList;
    }

    public String getTeaCode() {
        return teaCode;
    }

    public void setTeaCode(String teaCode) {
        this.teaCode = teaCode;
    }

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }

    public String getOuterTeaCode() {
        return outerTeaCode;
    }

    public void setOuterTeaCode(String outerTeaCode) {
        this.outerTeaCode = outerTeaCode;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getTeaTypeCode() {
        return teaTypeCode;
    }

    public void setTeaTypeCode(String teaTypeCode) {
        this.teaTypeCode = teaTypeCode;
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