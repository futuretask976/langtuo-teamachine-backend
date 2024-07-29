package com.langtuo.teamachine.api.request.userset;

import lombok.Data;

import java.util.Map;

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
}
