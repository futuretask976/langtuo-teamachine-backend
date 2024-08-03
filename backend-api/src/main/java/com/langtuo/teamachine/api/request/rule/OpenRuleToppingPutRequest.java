package com.langtuo.teamachine.api.request.rule;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class OpenRuleToppingPutRequest {
    /**
     * 物料名称
     */
    private String toppingCode;

    /**
     * 排空时间（单位：秒）
     */
    private int flushSec;

    /**
     * 排空重量
     */
    private int flushWeight;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(toppingCode)
                || flushSec <= 0
                || flushWeight <= 0) {
            return false;
        }
        return true;
    }
}
