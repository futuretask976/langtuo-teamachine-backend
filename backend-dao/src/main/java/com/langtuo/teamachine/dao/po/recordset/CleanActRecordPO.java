package com.langtuo.teamachine.dao.po.recordset;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class CleanActRecordPO {
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
    private Map<String,String> extraInfo;

    /**
     * 幂等标记
     */
    private String idempotentMark;

    /**
     * 机器编码
     */
    private String machineCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 店铺组编码
     */
    private String shopGroupCode;

    /**
     * 清洗开始时间
     */
    private Date cleanStartTime;

    /**
     * 清洗结束时间
     */
    private Date cleanEndTime;

    /**
     * 物料名称
     */
    private String toppingCode;

    /**
     * 管道序号
     */
    private int pipelineNum;

    /**
     * 清洗方式，0：清洗规则清洗，1：手动清洗，2：营业准备规则，3：打烊规则
     */
    private int cleanType;

    /**
     * 清洗规则
     */
    private String cleanRuleCode;

    /**
     * 开业规则编码
     */
    private String openRuleCode;

    /**
     * 打烊规则编码
     */
    private String closeRuleCode;

    /**
     * 清洗内容，0：冲洗，1：浸泡
     */
    private int cleanContent;

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
