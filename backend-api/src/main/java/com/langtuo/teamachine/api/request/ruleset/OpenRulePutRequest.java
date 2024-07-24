package com.langtuo.teamachine.api.request.ruleset;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OpenRulePutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 清洁规则编码
     */
    private String openRuleCode;

    /**
     * 清洁规则名称
     */
    private String openRuleName;

    /**
     * 是否允许提醒，0：不允许，1：允许
     */
    private Integer defaultRule;

    /**
     *
     */
    private List<OpenRuleToppingPutRequest> toppingRuleList;
}
