package com.gx.sp3.demo.api.model.langtuo;

import java.util.Date;
import java.util.Map;


public class MachineTeaOrderDTO {
    /**
     *
     */
    private long id;

    /**
     *
     */
    private Date gmtCreated;

    /**
     *
     */
    private Date gmtModified;

    /**
     *
     */
    private String orderId;

    /**
     *
     */
    private String machineCode;

    /**
     *
     */
    private String teaCode;

    /**
     *
     */
    private Map<String, String> extraInfoMap;

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getTeaCode() {
        return teaCode;
    }

    public void setTeaCode(String teaCode) {
        this.teaCode = teaCode;
    }

    public Map<String, String> getExtraInfoMap() {
        return extraInfoMap;
    }

    public void setExtraInfoMap(Map<String, String> extraInfoMap) {
        this.extraInfoMap = extraInfoMap;
    }
}
