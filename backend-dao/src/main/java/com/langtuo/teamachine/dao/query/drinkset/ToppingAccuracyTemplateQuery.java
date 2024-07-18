package com.langtuo.teamachine.dao.query.drinkset;

import lombok.Data;

@Data
public class ToppingAccuracyTemplateQuery {
    /**
     * 规格编码
     */
    private String templateCode;

    /**
     * 规格名称
     */
    private String templateName;

    /**
     * 租户编码
     */
    private String tenantCode;
}
