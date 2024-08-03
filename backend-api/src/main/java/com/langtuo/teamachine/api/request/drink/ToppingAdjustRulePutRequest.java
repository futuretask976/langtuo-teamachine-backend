package com.langtuo.teamachine.api.request.drink;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ToppingAdjustRulePutRequest {
    /**
     * 步骤，从1开始
     */
    private int stepIndex;

    /**
     * 物料编码
     */
    private String toppingCode;

    /**
     * 基础用量
     */
    private int baseAmount;

    /**
     * 调整类型，0：减少，1：添加
     */
    private int adjustType;

    /**
     * 调整模式，0：固定值，1：百分比
     */
    private int adjustMode;

    /**
     * 调整用量
     */
    private int adjustAmount;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (stepIndex <= 0
                || StringUtils.isBlank(toppingCode)) {
            return false;
        }
        return true;
    }
}
