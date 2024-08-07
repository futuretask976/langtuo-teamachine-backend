package com.langtuo.teamachine.api.model.rule;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class CloseRuleDTO {
    /**
     * 数据表记录插入时间
     */
    private Date gmtCreated;

    /**
     * 数据表记录最近修改时间
     */
    private Date gmtModified;

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
     *  浸泡期间冲洗间隔（单位：分钟）
     */
    private int flushIntervalMin;

    /**
     * 浸泡期间冲洗时间（单位：秒）
     */
    private int flushSec;
}
