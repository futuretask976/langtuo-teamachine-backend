package com.langtuo.teamachine.api.request.drinkset;

import lombok.Data;

import java.util.Map;

@Data
public class ToppingAccuracyTplPutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 状态，0：禁用，1：启用
     */
    private int state;

    /**
     * 溢出允许单位，0：固定值，1：百分比
     */
    private int overUnit;

    /**
     * 溢出数值
     */
    private int overAmount;

    /**
     * 不及允许单位，0：固定值，1：百分比
     */
    private int underUnit;

    /**
     * 不及数值
     */
    private int underAmount;

    /**
     * 应用物料编码
     */
    private String toppingCode;

    /**
     * 备注
     */
    private String comment;
}
