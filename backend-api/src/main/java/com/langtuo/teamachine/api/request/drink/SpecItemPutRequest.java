package com.langtuo.teamachine.api.request.drink;

import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
        if (RegexUtils.isValidStr(specItemCode, true)
                && RegexUtils.isValidStr(specItemName, true)
                && RegexUtils.isValidStr(outerSpecItemCode, true)) {
            return true;
        }
        return false;
    }
}
