package com.langtuo.teamachine.api.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
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

    public void addPermitActDTO(PermitActDTO dto) {
        if (this.permitActList == null) {
            this.permitActList = new ArrayList<>();
        }
        this.permitActList.add(dto);
    }
}
