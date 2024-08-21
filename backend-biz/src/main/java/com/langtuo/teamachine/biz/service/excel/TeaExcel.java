package com.langtuo.teamachine.biz.service.excel;

import lombok.Data;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Data
public class TeaExcel {
    /**
     *
     */
    private TeaInfoExcel teaInfoExcel;

    /**
     *
     */
    private List<ToppingBaseRuleExcel> toppingBaseRuleExcelList;

    /**
     *
     */
    private List<TeaUnitExcel> teaUnitExcelList;

    /**
     * toppingAdjustRuleExcelList的长度必定是toppingBaseRuleExcelList的长度的teaUnit长度倍数
     */
    private List<ToppingAdjustRuleExcel> toppingAdjustRuleExcelList;

    public void addAll(List<ToppingAdjustRuleExcel> toppingAdjustRuleExcelList) {
        if (toppingAdjustRuleExcelList == null) {
            return;
        }
        if (this.toppingAdjustRuleExcelList == null) {
            this.toppingAdjustRuleExcelList = Lists.newArrayList();
        }
        this.toppingAdjustRuleExcelList.addAll(toppingAdjustRuleExcelList);
    }
}