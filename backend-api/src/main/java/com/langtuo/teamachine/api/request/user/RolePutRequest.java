package com.langtuo.teamachine.api.request.user;

import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

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
        if (RegexUtils.isValidCode(tenantCode, true)
                && RegexUtils.isValidCode(roleCode, true)
                && RegexUtils.isValidName(roleName, true)) {
            return true;
        }
        return false;
    }

    private boolean isValidPermitActCodeList() {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(permitActCodeList)) {
            isValid = false;
        } else {
            for (String m : permitActCodeList) {
                if (!RegexUtils.isValidCode(m, true)) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }
}
