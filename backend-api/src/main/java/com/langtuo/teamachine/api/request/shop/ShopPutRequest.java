package com.langtuo.teamachine.api.request.shop;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Data
public class ShopPutRequest {
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
    private int shopType;

    /**
     * 店铺组编码
     */
    private String shopGroupCode;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(shopGroupCode)
                || StringUtils.isBlank(shopCode)
                || StringUtils.isBlank(shopName)) {
            return false;
        }
        return true;
    }
}
