package com.langtuo.teamachine.api.request.drink;

import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

@Data
public class SpecItemRulePutRequest {
    /**
     * 规格编码
     */
    private String specCode;

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
     * 是否选中
     */
    private int selected;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (RegexUtils.isValidCode(specCode, true)
                && RegexUtils.isValidCode(specItemCode, true)
                && RegexUtils.isValidName(specItemName, true)
                && RegexUtils.isValidCode(outerSpecItemCode, true)) {
            return true;
        }
        return false;
    }
}
