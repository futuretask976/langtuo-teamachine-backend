package com.langtuo.teamachine.api.request.drink;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
public class TeaUnitPutRequest {
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
    private List<SpecItemRulePutRequest> specItemRuleList;

    /**
     *
     */
    private List<ToppingAdjustRulePutRequest> toppingAdjustRuleList;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(teaUnitCode)
                || StringUtils.isBlank(teaUnitName)) {
            return false;
        }
        if (specItemRuleList == null || specItemRuleList.size() == 0) {
            return false;
        }
        for (SpecItemRulePutRequest s : specItemRuleList) {
            if (!s.isValid()) {
                return false;
            }
        }
        if (toppingAdjustRuleList == null || toppingAdjustRuleList.size() == 0) {
            return false;
        }
        for (ToppingAdjustRulePutRequest t : toppingAdjustRuleList) {
            if (!t.isValid()) {
                return false;
            }
        }
        return true;
    }
}
