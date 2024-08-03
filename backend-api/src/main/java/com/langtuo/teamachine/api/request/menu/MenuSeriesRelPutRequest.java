package com.langtuo.teamachine.api.request.menu;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class MenuSeriesRelPutRequest {
    /**
     * 菜单编码
     */
    private String menuCode;

    /**
     * 系列编码
     */
    private String seriesCode;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(menuCode)
                || StringUtils.isBlank(seriesCode)) {
            return false;
        }
        return true;
    }
}
