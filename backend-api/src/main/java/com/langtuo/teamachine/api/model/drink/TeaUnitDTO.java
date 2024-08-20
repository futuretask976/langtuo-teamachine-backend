package com.langtuo.teamachine.api.model.drink;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TeaUnitDTO {
    /**
     * 茶品单位编码
     */
    private String teaUnitCode;

    /**
     * 茶品单位名称
     */
    private String teaUnitName;

    /**
     *
     */
    private List<SpecItemRuleDTO> specItemRuleList;

    /**
     *
     */
    private List<ToppingAdjustRuleDTO> toppingAdjustRuleList;

    /**
     * 添加SpecItemRuleDTO元素
     * @param dto
     */
    public void addSpecItemRule(SpecItemRuleDTO dto) {
        if (dto == null) {
            return;
        }
        if (specItemRuleList == null) {
            specItemRuleList = Lists.newArrayList();
        }
        specItemRuleList.add(dto);
    }
}
