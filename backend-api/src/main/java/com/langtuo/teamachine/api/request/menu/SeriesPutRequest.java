package com.langtuo.teamachine.api.request.menu;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Data
public class SeriesPutRequest {
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
     * 系列编码
     */
    private String seriesCode;

    /**
     * 系列名称
     */
    private String seriesName;

    /**
     * 系列-茶品关系
     */
    private List<SeriesTeaRelPutRequest> seriesTeaRelList;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(seriesCode)
                || StringUtils.isBlank(seriesName)) {
            return false;
        }
        if (seriesTeaRelList == null || seriesTeaRelList.size() == 0) {
            return false;
        }
        for (SeriesTeaRelPutRequest s : seriesTeaRelList) {
            if (!s.isValid()) {
                return false;
            }
        }
        return true;
    }
}
