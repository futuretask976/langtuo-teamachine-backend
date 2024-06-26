package com.langtuo.teamachine.api.request;

import java.util.List;
import java.util.Map;

public class AdminRolePutRequest {
    /**
     * 菜单编码
     */
    private String roleCode;

    /**
     * 菜单名称
     */
    private String roleName;

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
     * 选中关联的权限点列表
     */
    private List<String> permitActCodeList;

    public List<String> getPermitActCodeList() {
        return permitActCodeList;
    }

    public void setPermitActCodeList(List<String> permitActCodeList) {
        this.permitActCodeList = permitActCodeList;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Map<String, String> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, String> extraInfo) {
        this.extraInfo = extraInfo;
    }
}
