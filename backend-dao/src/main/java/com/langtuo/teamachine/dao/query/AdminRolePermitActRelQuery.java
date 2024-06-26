package com.langtuo.teamachine.dao.query;

import lombok.Data;

@Data
public class AdminRolePermitActRelQuery {
    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 权限点编码
     */
    private String permitActCode;

    /**
     * 租户编码
     */
    private String tenantCode;
}
