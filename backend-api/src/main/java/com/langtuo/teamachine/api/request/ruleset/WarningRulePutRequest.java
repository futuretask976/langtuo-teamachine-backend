package com.langtuo.teamachine.api.request.ruleset;

import lombok.Data;

import java.util.List;
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
    private Integer warningType;

    /**
     * 预警内容
     */
    private String warningContent;

    /**
     * 阈值类型，0：绝对值，1：百分比
     */
    private Integer thresholdType;

    /**
     * 阈值
     */
    private Integer threshold;

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
}
