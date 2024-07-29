package com.langtuo.teamachine.dao.query.recordset;

import lombok.Data;

@Data
public class SupplyActRecordQuery {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 店铺组编码
     */
    private String shopGroupCode;

    /**
     * 店铺编码
     */
    private String shopCode;
}
