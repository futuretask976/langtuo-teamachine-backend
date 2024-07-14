package com.langtuo.teamachine.api.model;

import com.langtuo.teamachine.api.request.SpecItemRulePutRequest;
import com.langtuo.teamachine.api.request.ToppingAdjustRulePutRequest;

import java.util.Date;
import java.util.List;

public class SpecItemRuleDTO {
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
     * 规格项编码
     */
    private String specItemCode;

    /**
     * 规格项名称
     */
    private String specItemName;

    /**
     * 外部规格项编码
     */
    private String outerSpecItemCode;

    /**
     * 是否选中
     */
    private int selected;

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

    public String getSpecItemCode() {
        return specItemCode;
    }

    public void setSpecItemCode(String specItemCode) {
        this.specItemCode = specItemCode;
    }

    public String getSpecItemName() {
        return specItemName;
    }

    public void setSpecItemName(String specItemName) {
        this.specItemName = specItemName;
    }

    public String getOuterSpecItemCode() {
        return outerSpecItemCode;
    }

    public void setOuterSpecItemCode(String outerSpecItemCode) {
        this.outerSpecItemCode = outerSpecItemCode;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
