package com.langtuo.teamachine.api.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MachineModelPipelineDTO {
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
     * 管道号码
     */
    private Integer pipelineNum;

    /**
     * 是否支持冷藏，0：不支持，1：支持
     */
    private Integer enableFreeze;

    /**
     * 是否支持加热，0：不支持，1：支持
     */
    private Integer enableWarm;

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

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public Integer getPipelineNum() {
        return pipelineNum;
    }

    public void setPipelineNum(Integer pipelineNum) {
        this.pipelineNum = pipelineNum;
    }

    public Integer getEnableFreeze() {
        return enableFreeze;
    }

    public void setEnableFreeze(Integer enableFreeze) {
        this.enableFreeze = enableFreeze;
    }

    public Integer getEnableWarm() {
        return enableWarm;
    }

    public void setEnableWarm(Integer enableWarm) {
        this.enableWarm = enableWarm;
    }
}
