package com.langtuo.teamachine.api.request.device;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(deployCode)
                || StringUtils.isBlank(modelCode)
                || StringUtils.isBlank(machineCode)
                || StringUtils.isBlank(shopCode)
                || StringUtils.isBlank(machineName)
                || StringUtils.isBlank(screenCode)
                || StringUtils.isBlank(elecBoardCode)
                || validUntil == null) {
            return false;
        }
        return true;
    }
}
