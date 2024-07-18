package com.langtuo.teamachine.api.model;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ToppingDTO {
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
     * 物料类型名称
     */
    private String toppingTypeName;

    /**
     * 计量单位，0：克，1：毫升
     */
    private Integer measureUnit;

    /**
     * 状态，0：禁用，1：启用
     */
    private Integer state;

    /**
     * 有效周期
     */
    private Integer validHourPeriod;

    /**
     * 清洗周期
     */
    private Integer cleanHourPeriod;

    /**
     * 转换系数
     */
    private Double convertCoefficient;

    /**
     * 流速
     */
    private Integer flowSpeed;
}
