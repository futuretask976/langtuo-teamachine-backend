package com.langtuo.teamachine.api.model.ruleset;

import lombok.Data;

import java.util.List;

@Data
public class CleanRuleDispatchDTO {
    /**
     * 清洁规则编码
     */
    private String cleanRuleCode;

    /**
     * 店铺组编码列表
     */
    private List<String> shopGroupCodeList;
}
