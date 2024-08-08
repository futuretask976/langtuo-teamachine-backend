package com.langtuo.teamachine.api.model.rule;

import lombok.Data;

import java.util.Date;

@Data
public class CleanRuleStepDTO {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 清洁规则编码
     */
    private String cleanRuleCode;

    /**
     * 步骤序号
     */
    private int stepIndex;

    /**
     * 清洗内容，0：冲洗，1：浸泡
     */
    private int cleanContent;

    /**
     * 冲洗时间
     */
    private int washSec;

    /**
     * 浸泡时间
     */
    private int soakMin;

    /**
     * 浸泡冲洗间隔时间
     */
    private int flushIntervalMin;

    /**
     * 浸泡冲洗时间
     */
    private int flushSec;

    /**
     * 提醒标题
     */
    private String remindTitle;

    /**
     * 提醒内容
     */
    private String remindContent;

    /**
     * 是否需要再次确认
     */
    private int needConfirm;
}
