package com.langtuo.teamachine.mqtt.request.record;

import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

@Data
public class OrderSpecItemActRecordPutRequest {
    /**
     * 规格编码
     */
    private String specCode;

    /**
     * 规格名称
     */
    private String specName;

    /**
     * 规格项编码
     */
    private String specItemCode;

    /**
     * 规格项名称
     */
    private String specItemName;

    public boolean isValid() {
        if (!RegexUtils.isValidCode(specCode, true)) {
            return false;
        }
        if (!RegexUtils.isValidName(specName, true)) {
            return false;
        }
        if (!RegexUtils.isValidCode(specItemCode, true)) {
            return false;
        }
        if (!RegexUtils.isValidName(specItemName, true)) {
            return false;
        }
        return true;
    }
}