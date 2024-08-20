package com.langtuo.teamachine.api.request.drink;

import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

import java.util.List;

@Data
public class ActStepPutRequest {
    /**
     * 步骤序号
     */
    private int stepIndex;

    /**
     *
     */
    private List<ToppingBaseRulePutRequest> toppingBaseRuleList;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (stepIndex > 0
                && isValidToppingBaseRuleList()) {
            return true;
        }
        return false;
    }

    private boolean isValidToppingBaseRuleList() {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(toppingBaseRuleList)) {
            isValid = false;
        } else {
            for (ToppingBaseRulePutRequest m : toppingBaseRuleList) {
                if (!m.isValid()) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }
}
