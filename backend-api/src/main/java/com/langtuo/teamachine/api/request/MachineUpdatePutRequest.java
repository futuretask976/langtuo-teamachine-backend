package com.langtuo.teamachine.api.request;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class MachineUpdatePutRequest {
    /**
     * 机器编码
     */
    private String machineCode;

    /**
     * 机器名称
     */
    private String machineName;

    /**
     * 屏幕编码
     */
    private String screenCode;

    /**
     * 电控板编码
     */
    private String elecBoardCode;

    /**
     * 机器状态，0：禁用，1：启用
     */
    private Integer state;

    /**
     * 有效期
     */
    private Date validUntil;

    /**
     * 保修期
     */
    private Date maintainUntil;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;
}
