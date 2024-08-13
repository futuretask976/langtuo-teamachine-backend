package com.langtuo.teamachine.api.request.rule;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(cleanRuleCode)
                || StringUtils.isBlank(cleanRuleName)) {
            return false;
        }
        if (exceptToppingCodeList == null || exceptToppingCodeList.size() == 0) {
            return false;
        }
        for (String s : exceptToppingCodeList) {
            if (StringUtils.isBlank(s)) {
                return false;
            }
        }

        if (cleanRuleStepList == null || cleanRuleStepList.size() == 0) {
            return false;
        }
        for (CleanRuleStepPutRequest c : cleanRuleStepList) {
            if (!c.isValid()) {
                return false;
            }
        }
        return true;
    }
}
