package com.langtuo.teamachine.dao.query;

import lombok.Data;

@Data
public class MachineDeployQuery {
    /**
     * 部署编码
     */
    private String deplyCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 部署状态
     */
    private Integer state;

    /**
     * 租户编码
     */
    private String tenantCode;
}
