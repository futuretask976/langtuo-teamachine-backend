package com.langtuo.teamachine.api.request.userset;

import lombok.Data;

import java.util.Map;

@Data
public class OrgStrucPutRequest {
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
