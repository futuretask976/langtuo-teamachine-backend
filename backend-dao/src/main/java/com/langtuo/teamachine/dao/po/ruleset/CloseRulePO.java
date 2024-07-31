package com.langtuo.teamachine.dao.po.ruleset;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class CloseRulePO {
    /**
     * 数据表id
     */
    private long id;

    /**
     * 数据表记录插入时间
     */
    private Date gmtCreated;

    /**
     * 数据表记录最近修改时间
     */
    private Date gmtModified;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 打烊规则编码
     */
    private String closeRuleCode;

    /**
     * 打烊规则名称
     */
    private String closeRuleName;

    /**
     * 是否默认规则，0：不是，1：是
     */
    private int defaultRule;

    /**
     * 清洗时间（单位：秒）
     */
    private int washSec;

    /**
     * 浸泡时间（单位：分钟）
     */
    private int soakMin;

    /**
     * 浸泡期间冲洗间隔（单位：分钟）
     */
    private int flushIntervalMin;

    /**
     * 浸泡期间冲洗时间（单位：秒）
     */
    private int flushSec;
}
