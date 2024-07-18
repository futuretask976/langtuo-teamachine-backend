package com.langtuo.teamachine.api.request.userset;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
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
}
