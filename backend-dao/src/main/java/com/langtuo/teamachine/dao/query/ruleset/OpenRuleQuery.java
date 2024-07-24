package com.langtuo.teamachine.dao.query.ruleset;

import lombok.Data;

@Data
public class OpenRuleQuery {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 菜单编码
     */
    private String openRuleCode;

    /**
     * 菜单名称
     */
    private String openRuleName;
}
