package com.langtuo.teamachine.api.request.drinkset;

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
        if (StringUtils.isBlank(specItemCode)
                || StringUtils.isBlank(specItemName)
                || StringUtils.isBlank(outerSpecItemCode)) {
            return false;
        }
        return true;
    }
}
