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
     * 机器编码
     */
    private String machineCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 清洗开始时间，这里用String的原因是为了做索引幂等
     */
    private String cleanStartTime;

    /**
     * 清洗结束时间，这里用String的原因是为了做索引幂等
     */
    private String cleanEndTime;

    /**
     * 清洗方式，0：手动清洗，1：固定清洗
     */
    private int cleanType;

    /**
     * 清洗内容，0：冲洗，1：浸泡
     */
    private int cleanContent;

    /**
     * 物料名称
     */
    private String toppingCode;

    /**
     * 管道号
     */
    private int pipelineNum;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息
     */
    private Map<String,String> extraInfo;
}
