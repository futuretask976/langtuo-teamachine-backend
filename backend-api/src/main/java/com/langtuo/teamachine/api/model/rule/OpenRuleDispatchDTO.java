package com.langtuo.teamachine.api.model.rule;

import lombok.Data;

import java.util.List;

@Data
public class OpenRuleDispatchDTO {
    /**
     * 开业规则编码
     */
    private String openRuleCode;

    /**
     * 店铺组编码列表
     */
    private List<String> shopGroupCodeList;
}
