package com.langtuo.teamachine.api.model.rule;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class OpenRuleDTO {
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
     * 开业规则编码
     */
    private String openRuleCode;

    /**
     * 清洁规则名称
     */
    private String openRuleName;

    /**
     * 是否允许提醒，0：不允许，1：允许
     */
    private int defaultRule;

    /**
     * 排除时间
     */
    private int flushTime;

    /**
     * 排出重量
     */
    private int flushWeight;

    /**
     * 包括物料列表
     */
    private List<OpenRuleToppingDTO> toppingRuleList;
}
