package com.langtuo.teamachine.dao.po.drink;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ToppingPO {
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
     * 物料编码
     */
    private String toppingCode;

    /**
     * 物料名称
     */
    private String toppingName;

    /**
     * 物料类型编码
     */
    private String toppingTypeCode;

    /**
     * 计量单位，0：克，1：毫升
     */
    private int measureUnit;

    /**
     * 状态，0：禁用，1：启用
     */
    private int state;

    /**
     * 有效周期（单位：小时）
     */
    private int validHourPeriod;

    /**
     * 清洗周期（单位：小时）
     */
    private int cleanHourPeriod;

    /**
     * 转换系数
     */
    private double convertCoefficient;

    /**
     * 流速
     */
    private int flowSpeed;

    /**
     * 阈值类型，0：绝对值，1：百分比
     */
    private int thresholdMode;

    /**
     * 阈值
     */
    private int threshold;
}
