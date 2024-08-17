package com.langtuo.teamachine.dao.po.rule;

import lombok.Data;

import java.util.Date;

@Data
public class DrainRuleToppingPO {
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
     * 开业规则编码
     */
    private String drainRuleCode;

    /**
     * 物料编码
     */
    private String toppingCode;

    /**
     * 排空时间（单位：秒）
     */
    private int flushSec;

    /**
     * 排空重量
     */
    private int flushWeight;
}
