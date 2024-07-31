package com.langtuo.teamachine.api.request.deviceset;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class DeployPutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

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
    private int state;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(deployCode)
                || StringUtils.isBlank(modelCode)
                || StringUtils.isBlank(machineCode)
                || StringUtils.isBlank(shopCode)) {
            return false;
        }
        return true;
    }
}
