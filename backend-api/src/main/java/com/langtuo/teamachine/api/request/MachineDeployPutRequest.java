package com.langtuo.teamachine.api.request;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class MachineDeployPutRequest {
    /**
     * 部署编码
     */
    private String deployCode;

    /**
     * 型号编码
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
     * 部署状态，0：未部署，1：已部署
     */
    private Integer state;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;
}
