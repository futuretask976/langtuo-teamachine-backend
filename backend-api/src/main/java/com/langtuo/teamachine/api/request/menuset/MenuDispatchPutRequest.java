package com.langtuo.teamachine.api.request.menuset;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(menuCode)) {
            return false;
        }
        if (shopGroupCodeList == null || shopGroupCodeList.size() == 0) {
            return false;
        }
        for (String s : shopGroupCodeList) {
            if (StringUtils.isBlank(s)) {
                return false;
            }
        }
        return true;
    }
}
