package com.langtuo.teamachine.dao.query;

import lombok.Data;

@Data
public class ShopGroupQuery {
    /**
     * 店铺组名称
     */
    private String shopGroupName;

    /**
     * 租户编码
     */
    private String tenantCode;
}
