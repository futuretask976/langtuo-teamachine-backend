package com.langtuo.teamachine.api.request.menu;

import com.langtuo.teamachine.api.utils.RegexUtils;
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
        if (RegexUtils.isValidStr(menuCode, true)
                && RegexUtils.isValidStr(seriesCode, true)) {
            return true;
        }
        return false;
    }
}
