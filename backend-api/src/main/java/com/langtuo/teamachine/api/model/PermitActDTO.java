package com.langtuo.teamachine.api.model;

import java.util.Date;

public class PermitActDTO {
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
     * 租户编码
     */
    private String permitActCode;

    /**
     * 租户名称
     */
    private String permitActName;

    /**
     * 联系人
     */
    private String permitActGroupCode;

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

    public String getPermitActCode() {
        return permitActCode;
    }

    public void setPermitActCode(String permitActCode) {
        this.permitActCode = permitActCode;
    }

    public String getPermitActName() {
        return permitActName;
    }

    public void setPermitActName(String permitActName) {
        this.permitActName = permitActName;
    }

    public String getPermitActGroupCode() {
        return permitActGroupCode;
    }

    public void setPermitActGroupCode(String permitActGroupCode) {
        this.permitActGroupCode = permitActGroupCode;
    }
}
