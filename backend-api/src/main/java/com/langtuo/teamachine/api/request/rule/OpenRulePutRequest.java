package com.langtuo.teamachine.api.request.rule;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Data
public class OpenRulePutRequest {
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
    private String openRuleCode;

    /**
     * 开业规则名称
     */
    private String openRuleName;

    /**
     * 是否允许提醒，0：不允许，1：允许
     */
    private int defaultRule;

    /**
     * 包括物料列表
     */
    private List<OpenRuleToppingPutRequest> toppingRuleList;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(openRuleCode)
                || StringUtils.isBlank(openRuleName)) {
            return false;
        }
        if (toppingRuleList == null || toppingRuleList.size() == 0) {
            return false;
        }
        for (OpenRuleToppingPutRequest t : toppingRuleList) {
            if (!t.isValid()) {
                return false;
            }
        }
        return true;
    }
}
