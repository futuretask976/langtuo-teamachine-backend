package com.langtuo.teamachine.dao.query;

import lombok.Data;

@Data
public class TeaToppingTypeQuery {
    /**
     * 物料类型
     */
    private String toppingTypeCode;

    /**
     * 物料名称
     */
    private String toppingTypeName;

    /**
     * 租户编码
     */
    private String tenantCode;
}
