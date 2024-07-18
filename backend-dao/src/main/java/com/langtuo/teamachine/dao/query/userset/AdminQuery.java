package com.langtuo.teamachine.dao.query.userset;

import lombok.Data;

@Data
public class AdminQuery {
    /**
     * 管理员登录名称
     */
    private String loginName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 租户编码
     */
    private String tenantCode;
}
