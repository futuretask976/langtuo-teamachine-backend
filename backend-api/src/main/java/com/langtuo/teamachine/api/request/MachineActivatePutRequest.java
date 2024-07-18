package com.langtuo.teamachine.api.request;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class MachineActivatePutRequest {
    /**
     * 部署码激活
     */
    private String deployCode;

    /**
     * 机型编码
     */
    private String modelCode;

    /**
     * 机器编码
     */
    private String machineCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

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
     * 有效期
     */
    private Date validUntil;

    /**
     * 保修期
     */
    private Date maintainUntil;
}
