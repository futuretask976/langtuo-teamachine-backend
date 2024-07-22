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
     * 菜单编码
     */
    private String cleanRuleCode;

    /**
     * 店铺编码
     */
    private List<String> shopGroupCodeList;
}
