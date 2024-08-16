package com.langtuo.teamachine.api.request.rule;

import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

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
        if (RegexUtils.isValidCode(toppingCode, true)
                && flushSec > 0
                && flushWeight > 0) {
            return true;
        }
        return false;
    }
}
