package com.langtuo.teamachine.api.request.drinkset;

import lombok.Data;

import java.util.Map;

@Data
public class ToppingPutRequest {
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
     * 计量单位
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
}
