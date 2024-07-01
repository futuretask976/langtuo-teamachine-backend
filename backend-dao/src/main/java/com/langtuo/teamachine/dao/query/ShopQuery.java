package com.langtuo.teamachine.dao.query;

import lombok.Data;

@Data
public class ShopQuery {
    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺组编码
     */
    private String shopGroupCode;

    /**
     * 租户编码
     */
    private String tenantCode;
}
