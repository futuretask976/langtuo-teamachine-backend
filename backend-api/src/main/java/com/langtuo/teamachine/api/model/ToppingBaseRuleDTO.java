package com.langtuo.teamachine.api.model;

import java.util.Date;

public class ToppingBaseRuleDTO {
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
     *
     */
    private int stepIndex;

    /**
     * 物料编码
     */
    private String toppingCode;

    /**
     *
     */
    private int baseAmount;

    /**
     * 物料名称
     */
    private String toppingName;

    /**
     * 物料类型编码
     */
    private String toppingTypeCode;

    /**
     * 计量单位，0：克，1：毫升
     */
    private Integer measureUnit;

    /**
     * 状态，0：禁用，1：启用
     */
    private Integer state;

    public String getToppingName() {
        return toppingName;
    }

    public void setToppingName(String toppingName) {
        this.toppingName = toppingName;
    }

    public String getToppingTypeCode() {
        return toppingTypeCode;
    }

    public void setToppingTypeCode(String toppingTypeCode) {
        this.toppingTypeCode = toppingTypeCode;
    }

    public Integer getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(Integer measureUnit) {
        this.measureUnit = measureUnit;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

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

    public int getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    public String getToppingCode() {
        return toppingCode;
    }

    public void setToppingCode(String toppingCode) {
        this.toppingCode = toppingCode;
    }

    public int getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(int baseAmount) {
        this.baseAmount = baseAmount;
    }
}
