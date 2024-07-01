package com.langtuo.teamachine.api.request;

import java.util.Date;
import java.util.Map;

public class ShopGroupPutRequest {
    /**
     * 店铺组编码
     */
    private String shopGroupCode;

    /**
     * 店铺组名称
     */
    private String shopGroupName;

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

    public String getShopGroupCode() {
        return shopGroupCode;
    }

    public void setShopGroupCode(String shopGroupCode) {
        this.shopGroupCode = shopGroupCode;
    }

    public String getShopGroupName() {
        return shopGroupName;
    }

    public void setShopGroupName(String shopGroupName) {
        this.shopGroupName = shopGroupName;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Map<String, String> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, String> extraInfo) {
        this.extraInfo = extraInfo;
    }
}
