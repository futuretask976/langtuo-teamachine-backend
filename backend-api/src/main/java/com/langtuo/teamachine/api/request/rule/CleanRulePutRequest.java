package com.langtuo.teamachine.api.request.rule;

import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CleanRulePutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 清洁规则编码
     */
    private String cleanRuleCode;

    /**
     * 清洁规则名称
     */
    private String cleanRuleName;

    /**
     * 是否允许提醒，0：不允许，1：允许
     */
    private int permitRemind;

    /**
     * 是否允许批量，0：不允许，1：允许
     */
    private int permitBatch;

    /**
     * 排除物料编码列表
     */
    private List<String> exceptToppingCodeList;

    /**
     * 清洁步骤列表
     */
    private List<CleanRuleStepPutRequest> cleanRuleStepList;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (RegexUtils.isValidCode(tenantCode, true)
                && RegexUtils.isValidCode(cleanRuleCode, true)
                && RegexUtils.isValidName(cleanRuleName, true)
                && isValidExceptToppingCodeList()
                && isValidCleanRuleStepList()) {
            return true;
        }
        return false;
    }

    private boolean isValidExceptToppingCodeList() {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(exceptToppingCodeList)) {
            isValid = false;
        } else {
            for (String m : exceptToppingCodeList) {
                if (!RegexUtils.isValidCode(m, true)) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }

    private boolean isValidCleanRuleStepList() {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(cleanRuleStepList)) {
            isValid = false;
        } else {
            for (CleanRuleStepPutRequest m : cleanRuleStepList) {
                if (!m.isValid()) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }
}
