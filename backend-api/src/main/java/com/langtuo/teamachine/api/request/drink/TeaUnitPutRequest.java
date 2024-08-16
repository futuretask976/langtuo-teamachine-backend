package com.langtuo.teamachine.api.request.drink;

import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

import java.util.List;

@Data
public class TeaUnitPutRequest {
    /**
     * 茶品单位编码
     */
    private String teaUnitCode;

    /**
     * 茶品单位名称
     */
    private String teaUnitName;

    /**
     *
     */
    private List<SpecItemRulePutRequest> specItemRuleList;

    /**
     *
     */
    private List<ToppingAdjustRulePutRequest> toppingAdjustRuleList;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (RegexUtils.isValidCode(teaUnitCode, true)
                && RegexUtils.isValidName(teaUnitName, true)
                && isValidSpecItemRuleList()
                && isValidToppingAdjustRuleList()) {
            return true;
        }
        return false;
    }

    private boolean isValidSpecItemRuleList() {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(specItemRuleList)) {
            isValid = false;
        } else {
            for (SpecItemRulePutRequest m : specItemRuleList) {
                if (!m.isValid()) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }

    private boolean isValidToppingAdjustRuleList() {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(toppingAdjustRuleList)) {
            isValid = false;
        } else {
            for (ToppingAdjustRulePutRequest m : toppingAdjustRuleList) {
                if (!m.isValid()) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }
}
