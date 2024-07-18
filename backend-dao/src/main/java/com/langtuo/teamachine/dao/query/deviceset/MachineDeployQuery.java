package com.langtuo.teamachine.dao.query.deviceset;

import lombok.Data;

@Data
public class MachineDeployQuery {
    /**
     * 部署编码
     */
    private String deployCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 机器编码
     */
    private String machineCode;

    /**
     * 部署状态
     */
    private Integer state;

    /**
     * 租户编码
     */
    private String tenantCode;
}
