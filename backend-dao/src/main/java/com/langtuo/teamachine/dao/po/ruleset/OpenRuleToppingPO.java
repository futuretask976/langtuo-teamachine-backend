package com.langtuo.teamachine.dao.po.ruleset;

import lombok.Data;

import java.util.Date;

@Data
public class OpenRuleToppingPO {
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
    private String openRuleCode;

    /**
     * 清洁物料编码
     */
    private String toppingCode;

    /**
     * 冲洗时间
     */
    private int flushTime;

    /**
     * 冲洗重量
     */
    private int flushWeight;
}
