package com.langtuo.teamachine.biz.service.excel.export;

import lombok.Data;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Data
public class TeaExcel {
    /**
     *
     */
    private TeaInfoPart teaInfoPart;

    /**
     *
     */
    private List<ToppingBaseRulePart> toppingBaseRulePartList;

    /**
     *
     */
    private List<TeaUnitPart> teaUnitPartList;

    /**
     * toppingAdjustRuleExcelList的长度必定是toppingBaseRuleExcelList的长度的teaUnit长度倍数
     */
    private List<ToppingAdjustRulePart> toppingAdjustRulePartList;

    public void addAll(List<ToppingAdjustRulePart> toppingAdjustRulePartList) {
        if (toppingAdjustRulePartList == null) {
            return;
        }
        if (this.toppingAdjustRulePartList == null) {
            this.toppingAdjustRulePartList = Lists.newArrayList();
        }
        this.toppingAdjustRulePartList.addAll(toppingAdjustRulePartList);
    }
}