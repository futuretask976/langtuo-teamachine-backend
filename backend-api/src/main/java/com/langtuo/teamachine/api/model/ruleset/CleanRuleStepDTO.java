package com.langtuo.teamachine.api.model.ruleset;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class CleanRuleStepDTO {
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
     * 清洁规则编码
     */
    private String cleanRuleCode;

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
    private Integer washTime;

    /**
     * 浸泡时间
     */
    private Integer soakTime;

    /**
     * 浸泡冲洗间隔时间
     */
    private Integer soakWashInterval;

    /**
     * 浸泡冲洗时间
     */
    private Integer soakWashTime;

    /**
     * 提醒标题
     */
    private String remindTitle;

    /**
     * 提醒内容
     */
    private String remindContent;
}
