package com.langtuo.teamachine.api.request;

import java.util.Map;

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

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }
}
