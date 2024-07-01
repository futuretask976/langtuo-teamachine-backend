package com.langtuo.teamachine.api.request;

import java.util.Date;
import java.util.Map;

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

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getShopType() {
        return shopType;
    }

    public void setShopType(Integer shopType) {
        this.shopType = shopType;
    }

    public String getShopGroupCode() {
        return shopGroupCode;
    }

    public void setShopGroupCode(String shopGroupCode) {
        this.shopGroupCode = shopGroupCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
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
