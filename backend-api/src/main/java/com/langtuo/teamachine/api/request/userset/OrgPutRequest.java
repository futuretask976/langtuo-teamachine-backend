package com.langtuo.teamachine.api.request.userset;

import lombok.Data;

import java.util.Map;

@Data
public class OrgPutRequest {
    /**
     * 同OrgStrucDTO
     */
    private String tenantCode;

    /**
     * 同OrgStrucDTO
     */
    private String orgName;

    /**
     * 同OrgStrucDTO
     */
    private String parentOrgName;
}
