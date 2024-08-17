package com.langtuo.teamachine.api.request.user;

import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

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
        if (RegexUtils.isValidCode(tenantCode, true)
                && RegexUtils.isValidComment(comment, false)
                && RegexUtils.isValidName(loginName, true)
                && RegexUtils.isValidCode(loginPass, true)
                && RegexUtils.isValidCode(roleCode, true)
                && RegexUtils.isValidName(orgName, true)) {
            return true;
        }
        return false;
    }
}
