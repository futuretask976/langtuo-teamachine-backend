package com.langtuo.teamachine.api.request;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ShopPutRequest {
    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺类型，0：直营，1：加盟
     */
    private Integer shopType;

    /**
     * 店铺组编码
     */
    private String shopGroupCode;

    /**
     * 归属的组织架构
     */
    private String orgName;

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
