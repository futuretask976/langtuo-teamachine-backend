package com.langtuo.teamachine.api.request.record;

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
        if (RegexUtils.isValidCode(specCode, true)
                && RegexUtils.isValidName(specName, true)
                && RegexUtils.isValidCode(specItemCode, true)
                && RegexUtils.isValidName(specItemName, true)) {
            return true;
        }
        return false;
    }
}
