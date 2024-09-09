package com.langtuo.teamachine.api.request.drink;

import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

@Data
public class SpecItemPutRequest {
    /**
     * 规格项编码
     */
    private String specItemCode;

    /**
     * 规格项名称
     */
    private String specItemName;

    /**
     * 外部规格项编码
     */
    private String outerSpecItemCode;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (!RegexUtils.isValidCode(specItemCode, true)) {
            return false;
        }
        if (!RegexUtils.isValidName(specItemName, true)) {
            return false;
        }
        if (!RegexUtils.isValidCode(outerSpecItemCode, true)) {
            return false;
        }
        return true;
    }
}
