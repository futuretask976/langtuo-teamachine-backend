package com.langtuo.teamachine.api.model;

import java.util.Date;
import java.util.List;

public class TeaUnitDTO {
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
     * 茶品单位编码
     */
    private String teaUnitCode;

    /**
     * 茶品单位名称
     */
    private String teaUnitName;

    /**
     *
     */
    private List<SpecItemRuleDTO> specItemRuleList;

    /**
     *
     */
    private List<ToppingAdjustRuleDTO> toppingAdjustRuleList;

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

    public String getTeaUnitCode() {
        return teaUnitCode;
    }

    public void setTeaUnitCode(String teaUnitCode) {
        this.teaUnitCode = teaUnitCode;
    }

    public String getTeaUnitName() {
        return teaUnitName;
    }

    public void setTeaUnitName(String teaUnitName) {
        this.teaUnitName = teaUnitName;
    }

    public List<SpecItemRuleDTO> getSpecItemRuleList() {
        return specItemRuleList;
    }

    public void setSpecItemRuleList(List<SpecItemRuleDTO> specItemRuleList) {
        this.specItemRuleList = specItemRuleList;
    }

    public List<ToppingAdjustRuleDTO> getToppingAdjustRuleList() {
        return toppingAdjustRuleList;
    }

    public void setToppingAdjustRuleList(List<ToppingAdjustRuleDTO> toppingAdjustRuleList) {
        this.toppingAdjustRuleList = toppingAdjustRuleList;
    }
}
