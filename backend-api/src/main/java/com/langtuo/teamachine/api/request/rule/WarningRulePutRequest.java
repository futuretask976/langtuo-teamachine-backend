package com.langtuo.teamachine.api.request.rule;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Data
public class WarningRulePutRequest {
    /**
     * 预警规则编码
     */
    private String warningRuleCode;

    /**
     * 预警规则名称
     */
    private String warningRuleName;

    /**
     * 预警类型，0：强提醒，1：弱提醒
     */
    private int warningType;

    /**
     * 预警内容，0：报废预警，1：缺料预警，2：清洗预警
     */
    private int warningContent;

    /**
     * 阈值类型，0：绝对值，1：百分比
     */
    private int thresholdMode;

    /**
     * 阈值
     */
    private int threshold;

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
     *
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(warningRuleCode)
                || StringUtils.isBlank(warningRuleName)) {
            return false;
        }
        return true;
    }
}
