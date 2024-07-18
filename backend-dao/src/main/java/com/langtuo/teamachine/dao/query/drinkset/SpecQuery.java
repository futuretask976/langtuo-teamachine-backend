package com.langtuo.teamachine.dao.query.drinkset;

import lombok.Data;

@Data
public class SpecQuery {
    /**
     * 规格编码
     */
    private String specCode;

    /**
     * 规格名称
     */
    private String specName;

    /**
     * 租户编码
     */
    private String tenantCode;
}
