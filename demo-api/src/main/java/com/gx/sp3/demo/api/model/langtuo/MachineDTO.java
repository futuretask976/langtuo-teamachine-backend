package com.gx.sp3.demo.api.model.langtuo;

import java.util.Date;
import java.util.Map;

public class MachineDTO {
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
    private String machineCode;

    /**
     *
     */
    private String machineName;

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

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Map<String, String> getExtraInfoMap() {
        return extraInfoMap;
    }

    public void setExtraInfoMap(Map<String, String> extraInfoMap) {
        this.extraInfoMap = extraInfoMap;
    }
}
