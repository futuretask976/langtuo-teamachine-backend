package com.langtuo.teamachine.api.model.rule;

import lombok.Data;

import java.util.Date;

@Data
public class OpenRuleToppingDTO {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 清洁规则编码
     */
    private String openRuleCode;

    /**
     * 清洁排除物料编码
     */
    private String toppingCode;

    /**
     * 清洁排除物料编码
     */
    private String toppingName;

    /**
     * 排空时间（单位：秒）
     */
    private int flushSec;

    /**
     * 排空重量
     */
    private int flushWeight;
}
