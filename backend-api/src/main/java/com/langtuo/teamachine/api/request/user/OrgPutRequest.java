package com.langtuo.teamachine.api.request.user;

import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

@Data
public class OrgPutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 父组织名称
     */
    private String parentOrgName;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (!RegexUtils.isValidCode(tenantCode, true)) {
            return false;
        }
        if (!RegexUtils.isValidName(parentOrgName, false)) {
            return false;
        }
        if (!RegexUtils.isValidName(orgName, true)) {
            return false;
        }
        return true;
    }
}
