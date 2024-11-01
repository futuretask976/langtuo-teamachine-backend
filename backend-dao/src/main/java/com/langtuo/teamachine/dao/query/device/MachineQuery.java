package com.langtuo.teamachine.dao.query.device;

import lombok.Data;

@Data
public class MachineQuery {
    /**
     * 屏幕编码
     */
    private String screenCode;

    /**
     * 机器编码
     */
    private String machineCode;

    /**
     * 电控板编码
     */
    private String elecBoardCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 租户编码
     */
    private String tenantCode;
}
