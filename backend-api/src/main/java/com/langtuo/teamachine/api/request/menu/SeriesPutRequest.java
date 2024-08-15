package com.langtuo.teamachine.api.request.menu;

import com.langtuo.teamachine.api.request.device.ModelPipelinePutRequest;
import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.api.utils.RegexUtils;
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
        if (RegexUtils.isValidStr(tenantCode, true)
                && RegexUtils.isValidStr(comment, false)
                && RegexUtils.isValidStr(seriesCode, true)
                && RegexUtils.isValidStr(seriesName, true)) {
            return true;
        }
        return false;
    }

    private boolean isValidPipelineList() {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(seriesTeaRelList)) {
            isValid = false;
        } else {
            for (SeriesTeaRelPutRequest m : seriesTeaRelList) {
                if (!m.isValid()) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }
}
