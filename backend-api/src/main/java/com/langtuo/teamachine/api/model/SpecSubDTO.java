package com.langtuo.teamachine.api.model;

import java.util.Date;
import java.util.Map;

public class SpecSubDTO {
    /**
     * 数据表id
     */
    private long id;

    /**
     * 数据表记录插入时间
     */
    private Date gmtCreated;

    /**
     * 数据表记录最近修改时间
     */
    private Date gmtModified;

    /**
     * 规格编码
     */
    private String specCode;

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

    /**
     * 租户编码
     */
    private String tenantCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getSpecCode() {
        return specCode;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

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

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }
}
