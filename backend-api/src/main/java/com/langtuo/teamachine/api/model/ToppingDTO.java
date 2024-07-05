package com.langtuo.teamachine.api.model;

import java.util.Date;
import java.util.Map;

public class ToppingDTO {
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
     * 物料编码
     */
    private String toppingCode;

    /**
     * 物料名称
     */
    private String toppingName;

    /**
     * 物料类型编码
     */
    private String toppingTypeCode;

    /**
     * 物料类型名称
     */
    private String toppingTypeName;

    /**
     * 计量单位，0：克，1：毫升
     */
    private Integer measureUnit;

    /**
     * 状态，0：禁用，1：启用
     */
    private Integer state;

    /**
     * 有效周期
     */
    private Integer validHourPeriod;

    /**
     * 清洗周期
     */
    private Integer cleanHourPeriod;

    /**
     * 转换系数
     */
    private Double convertCoefficient;

    /**
     * 流速
     */
    private Integer flowSpeed;

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

    public String getToppingTypeName() {
        return toppingTypeName;
    }

    public void setToppingTypeName(String toppingTypeName) {
        this.toppingTypeName = toppingTypeName;
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

    public String getToppingCode() {
        return toppingCode;
    }

    public void setToppingCode(String toppingCode) {
        this.toppingCode = toppingCode;
    }

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

    public Integer getValidHourPeriod() {
        return validHourPeriod;
    }

    public void setValidHourPeriod(Integer validHourPeriod) {
        this.validHourPeriod = validHourPeriod;
    }

    public Integer getCleanHourPeriod() {
        return cleanHourPeriod;
    }

    public void setCleanHourPeriod(Integer cleanHourPeriod) {
        this.cleanHourPeriod = cleanHourPeriod;
    }

    public Double getConvertCoefficient() {
        return convertCoefficient;
    }

    public void setConvertCoefficient(Double convertCoefficient) {
        this.convertCoefficient = convertCoefficient;
    }

    public Integer getFlowSpeed() {
        return flowSpeed;
    }

    public void setFlowSpeed(Integer flowSpeed) {
        this.flowSpeed = flowSpeed;
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
