package com.langtuo.teamachine.dao.po.ruleset;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class OpenRulePO {
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
     * 清洁规则编码
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
     *
     */
    private List<OpenRuleToppingPO> toppingRuleList;
}