package com.langtuo.teamachine.api.request.drink;

import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

@Data
public class ToppingBaseRulePutRequest {
    /**
     * 物料编码
     */
    private String toppingCode;

    /**
     * 基础用量
     */
    private int baseAmount;

    /**
     * 物料名称
     */
    private String toppingName;

    /**
     * 度量单位
     */
    private int measureUnit;

    /**
     *
     */
    private int state;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (RegexUtils.isValidCode(toppingCode, true)
                && RegexUtils.isValidName(toppingName, true)) {
            return true;
        }
        return false;
    }
}
