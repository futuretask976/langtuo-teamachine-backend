package com.langtuo.teamachine.dao.query.record;

import lombok.Data;

@Data
public class CleanActRecordQuery {
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
