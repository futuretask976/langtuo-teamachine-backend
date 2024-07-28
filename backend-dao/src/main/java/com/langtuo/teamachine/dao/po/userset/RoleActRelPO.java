package com.langtuo.teamachine.dao.po.userset;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class RoleActRelPO {
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
     * 角色编码
     */
    private String roleCode;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 权限编码
     */
    private String permitActCode;
}
