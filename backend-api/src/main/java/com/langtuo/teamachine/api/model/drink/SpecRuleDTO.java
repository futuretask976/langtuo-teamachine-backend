package com.langtuo.teamachine.api.model.drink;

import lombok.Data;

import java.util.List;

@Data
public class SpecRuleDTO {
    /**
     * 规格编码
     */
    private String specCode;

    /**
     * 规格名称
     */
    private String specName;

    /**
     *
     */
    private List<SpecItemRuleDTO> specItemRuleList;
}
