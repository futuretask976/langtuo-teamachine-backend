package com.langtuo.teamachine.api.request.ruleset;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Data
public class CloseRulePutRequest {
    /**
     *
     */
    private String tenantCode;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 清洁规则编码
     */
    private String closeRuleCode;

    /**
     * 清洁规则名称
     */
    private String closeRuleName;

    /**
     * 是否允许提醒，0：不允许，1：允许
     */
    private int defaultRule;

    /**
     * 是否允许批量，0：不允许，1：允许
     */
    private int washSec;

    /**
     * 租户编码
     */
    private int soakMin;

    /**
     *
     */
    private int flushIntervalMin;

    /**
     *
     */
    private int flushSec;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(closeRuleCode)
                || StringUtils.isBlank(closeRuleName)) {
            return false;
        }
        return true;
    }
}
