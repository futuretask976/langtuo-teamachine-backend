package com.langtuo.teamachine.api.model.ruleset;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class WarningRuleDTO {
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
     * 备注
     */
    private String comment;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 预警规则编码
     */
    private String warningRuleCode;

    /**
     * 预警规则名称
     */
    private String warningRuleName;

    /**
     * 预警类型，0：弱提醒，1：强提醒
     */
    private Integer warningType;

    /**
     * 预警内容，0：报废预警，1：清洗预警
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
}