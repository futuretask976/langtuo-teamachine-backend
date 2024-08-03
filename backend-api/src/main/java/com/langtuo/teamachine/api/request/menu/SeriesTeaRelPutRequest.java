package com.langtuo.teamachine.api.request.menu;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class SeriesTeaRelPutRequest {
    /**
     * 系列编码
     */
    private String seriesCode;

    /**
     * 茶品编码
     */
    private String teaCode;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(seriesCode)
                || StringUtils.isBlank(teaCode)) {
            return false;
        }
        return true;
    }
}
