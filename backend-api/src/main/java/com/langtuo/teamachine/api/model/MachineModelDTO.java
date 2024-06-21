package com.langtuo.teamachine.api.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MachineModelDTO {
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
     * 型号编码
     */
    private String modelCode;

    /**
     * 是否支持同时出料，0：不支持，1：支持
     */
    private Integer enableFlowAll;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public Integer getEnableFlowAll() {
        return enableFlowAll;
    }

    public void setEnableFlowAll(Integer enableFlowAll) {
        this.enableFlowAll = enableFlowAll;
    }

    public Map<String, String> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, String> extraInfo) {
        if (extraInfo == null) {
            return;
        }
        if (this.extraInfo == null) {
            this.extraInfo = new HashMap<>();
        }
        this.extraInfo.putAll(extraInfo);
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
}
