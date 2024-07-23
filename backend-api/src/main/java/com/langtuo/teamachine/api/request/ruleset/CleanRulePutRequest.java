package com.langtuo.teamachine.api.request.ruleset;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CleanRulePutRequest {
    /**
     * 清洁规则编码
     */
    private String cleanRuleCode;

    /**
     * 清洁规则名称
     */
    private String cleanRuleName;

    /**
     * 是否允许提醒，0：不允许，1：允许
     */
    private Integer permitRemind;

    /**
     * 是否允许批量，0：不允许，1：允许
     */
    private Integer permitBatch;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 
     */
    private List<CleanRuleStepPutRequest> cleanRuleStepList;
}
