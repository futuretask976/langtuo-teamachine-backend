package com.langtuo.teamachine.api.request.menu;

import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

import java.util.List;

@Data
public class MenuDispatchPutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 菜单编码
     */
    private String menuCode;

    /**
     * 店铺编码
     */
    private List<String> shopGroupCodeList;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (RegexUtils.isValidCode(tenantCode, true)
                && RegexUtils.isValidCode(menuCode, true)
                && isValidShopGroupList()) {
            return true;
        }
        return false;
    }

    private boolean isValidShopGroupList() {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(shopGroupCodeList)) {
            isValid = false;
        } else {
            for (String m : shopGroupCodeList) {
                if (!RegexUtils.isValidCode(m, true)) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }
}
