package com.langtuo.teamachine.api.request.ruleset;

import lombok.Data;

import java.util.List;

@Data
public class CleanRuleDispatchPutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 清洁规则编码
     */
    private String cleanRuleCode;

    /**
     * 店铺组编码列表
     */
    private List<String> shopGroupCodeList;
}
