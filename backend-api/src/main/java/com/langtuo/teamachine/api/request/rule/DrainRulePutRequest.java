package com.langtuo.teamachine.api.request.rule;

import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DrainRulePutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 开业规则编码
     */
    private String drainRuleCode;

    /**
     * 开业规则名称
     */
    private String drainRuleName;

    /**
     * 是否允许提醒，0：不允许，1：允许
     */
    private int defaultRule;

    /**
     * 包括物料列表
     */
    private List<DrainRuleToppingPutRequest> toppingRuleList;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (RegexUtils.isValidCode(tenantCode, true)
                && RegexUtils.isValidCode(drainRuleCode, true)
                && RegexUtils.isValidName(drainRuleName, true)
                && isValidToppingRuleList()) {
            return true;
        }
        return false;
    }

    private boolean isValidToppingRuleList() {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(toppingRuleList)) {
            isValid = false;
        } else {
            for (DrainRuleToppingPutRequest m : toppingRuleList) {
                if (!m.isValid()) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }
}
