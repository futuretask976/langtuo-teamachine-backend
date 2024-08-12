package com.langtuo.teamachine.api.request.rule;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class CleanRuleStepPutRequest {
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

    /**
     * 清洗液类型，0：清水，1：消毒水，2：饮用水
     */
    private int cleanAgentType;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (stepIndex < 0) {
            return false;
        }
        if (needConfirm == 1 && (StringUtils.isBlank(remindTitle) || StringUtils.isBlank(remindContent))) {
            return false;
        }
        return true;
    }
}
