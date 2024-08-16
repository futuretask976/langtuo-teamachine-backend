package com.langtuo.teamachine.dao.query.device;

import lombok.Data;

@Data
public class MachineDeployQuery {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 部署编码
     */
    private String deployCode;

    /**
     * 机器编码
     */
    private String machineCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 部署状态
     */
    private Integer state;
}
