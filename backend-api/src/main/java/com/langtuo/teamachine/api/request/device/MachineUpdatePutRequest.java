package com.langtuo.teamachine.api.request.device;

import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class MachineUpdatePutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

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
    private int state;

    /**
     * 有效期
     */
    private Date validUntil;

    /**
     * 保修期
     */
    private Date maintainUntil;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (RegexUtils.isValidCode(tenantCode, true)
                && RegexUtils.isValidCode(machineCode, true)
                && RegexUtils.isValidName(machineName, true)
                && RegexUtils.isValidCode(screenCode, true)
                && RegexUtils.isValidCode(elecBoardCode, true)
                && RegexUtils.isValidCode(shopCode, true)
                && validUntil != null
                && maintainUntil != null) {
            return true;
        }
        return false;
    }
}
