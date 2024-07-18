package com.langtuo.teamachine.api.request;

import lombok.Data;

import java.util.Map;

@Data
public class TeaTypePutRequest {
    /**
     * 茶类型编码
     */
    private String teaTypeCode;

    /**
     * 茶类型名称
     */
    private String teaTypeName;

    /**
     * 店铺类型，0：禁用，1：启用
     */
    private Integer state;

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
