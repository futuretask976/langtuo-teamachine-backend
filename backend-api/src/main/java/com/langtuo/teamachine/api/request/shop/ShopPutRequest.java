package com.langtuo.teamachine.api.request.shop;

import com.langtuo.teamachine.api.utils.RegexUtils;
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
        if (RegexUtils.isValidStr(tenantCode, true)
                && RegexUtils.isValidStr(comment, false)
                && RegexUtils.isValidStr(shopGroupCode, true)
                && RegexUtils.isValidStr(shopCode, true)
                && RegexUtils.isValidStr(shopName, true)) {
            return true;
        }
        return false;
    }
}
