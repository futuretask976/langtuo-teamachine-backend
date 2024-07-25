package com.langtuo.teamachine.api.request.ruleset;

import lombok.Data;

@Data
public class CleanRuleStepPutRequest {
    /**
     * 步骤序号
     */
    private Integer stepIndex;

    /**
     * 清洗内容，0：冲洗，1：浸泡
     */
    private Integer cleanContent;

    /**
     * 冲洗时间
     */
    private Integer washSec;

    /**
     * 浸泡时间
     */
    private Integer soakMin;

    /**
     * 浸泡冲洗间隔时间
     */
    private Integer flushIntervalMin;

    /**
     * 浸泡冲洗时间
     */
    private Integer flushSec;

    /**
     * 提醒标题
     */
    private String remindTitle;

    /**
     * 提醒内容
     */
    private String remindContent;

    /**
     *
     */
    private int needConfirm;
}
