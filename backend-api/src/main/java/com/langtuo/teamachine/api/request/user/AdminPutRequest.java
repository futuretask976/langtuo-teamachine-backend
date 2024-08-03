package com.langtuo.teamachine.api.request.user;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Data
public class AdminPutRequest {
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

    /**
     * 管理员登录名
     */
    private String loginName;

    /**
     * 管理员登录密码
     */
    private String loginPass;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(loginName)
                || StringUtils.isBlank(loginPass)
                || StringUtils.isBlank(roleCode)
                || StringUtils.isBlank(orgName)) {
            return false;
        }
        return true;
    }
}
