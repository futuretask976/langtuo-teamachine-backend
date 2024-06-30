package com.langtuo.teamachine.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PermitActGroupDTO {
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
    private String permitActGroupCode;

    /**
     * 租户名称
     */
    private String permitActGroupName;

    /**
     * 归属当前权限点组下的权限点列表
     */
    private List<PermitActDTO> permitActList;

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

    public String getPermitActGroupCode() {
        return permitActGroupCode;
    }

    public void setPermitActGroupCode(String permitActGroupCode) {
        this.permitActGroupCode = permitActGroupCode;
    }

    public String getPermitActGroupName() {
        return permitActGroupName;
    }

    public void setPermitActGroupName(String permitActGroupName) {
        this.permitActGroupName = permitActGroupName;
    }

    public List<PermitActDTO> getPermitActList() {
        return permitActList;
    }

    public void setPermitActList(List<PermitActDTO> permitActList) {
        this.permitActList = permitActList;
    }

    public void addPermitActDTO(PermitActDTO dto) {
        if (this.permitActList == null) {
            this.permitActList = new ArrayList<>();
        }
        this.permitActList.add(dto);
    }
}
