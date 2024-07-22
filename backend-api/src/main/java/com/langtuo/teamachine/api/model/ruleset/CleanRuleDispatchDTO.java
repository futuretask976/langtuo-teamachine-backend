package com.langtuo.teamachine.api.model.ruleset;

import lombok.Data;

import java.util.List;

@Data
public class CleanRuleDispatchDTO {
    /**
     * 菜单编码
     */
    private String cleanRuleCode;

    /**
     * 店铺编码
     */
    private List<String> shopGroupCodeList;
}
