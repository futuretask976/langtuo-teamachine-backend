package com.langtuo.teamachine.api.request.device;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Data
public class MachineActivatePutRequest {
    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 部署码激活
     */
    private String deployCode;

    /**
     * 机器编码
     */
    private String machineCode;

    /**
     * 屏幕编码
     */
    private String screenCode;

    /**
     * 电控板编码
     */
    private String elecBoardCode;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(deployCode)
                || StringUtils.isBlank(machineCode)
                || StringUtils.isBlank(screenCode)
                || StringUtils.isBlank(elecBoardCode)) {
            return false;
        }
        return true;
    }
}
