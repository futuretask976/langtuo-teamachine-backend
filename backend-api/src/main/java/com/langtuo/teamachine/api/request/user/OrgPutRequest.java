package com.langtuo.teamachine.api.request.user;

import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
        if (RegexUtils.isValidStr(tenantCode, true)
                && RegexUtils.isValidStr(parentOrgName, true)
                && RegexUtils.isValidStr(orgName, true)) {
            return true;
        }
        return false;
    }
}
