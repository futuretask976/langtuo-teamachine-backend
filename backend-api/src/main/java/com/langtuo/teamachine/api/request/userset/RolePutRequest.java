package com.langtuo.teamachine.api.request.userset;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Data
public class RolePutRequest {
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
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 系统预留，0：不是，1：是
     */
    private int sysReserved;

    /**
     * 选中关联的权限点列表
     */
    private List<String> permitActCodeList;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(roleCode)
                || StringUtils.isBlank(roleName)) {
            return false;
        }
        if (permitActCodeList == null || permitActCodeList.size() == 0) {
            return false;
        }
        for (String s : permitActCodeList) {
            if (StringUtils.isBlank(s)) {
                return false;
            }
        }
        return true;
    }
}
