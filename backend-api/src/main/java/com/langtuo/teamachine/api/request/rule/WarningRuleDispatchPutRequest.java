package com.langtuo.teamachine.api.request.rule;

import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

import java.util.List;

@Data
public class WarningRuleDispatchPutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 预警规则编码
     */
    private String warningRuleCode;

    /**
     * 店铺组编码列表
     */
    private List<String> shopGroupCodeList;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (RegexUtils.isValidStr(tenantCode, true)
                && RegexUtils.isValidStr(warningRuleCode, true)
                && isValidShopGroupCodeList()) {
            return true;
        }
        return false;
    }

    private boolean isValidShopGroupCodeList() {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(shopGroupCodeList)) {
            isValid = false;
        } else {
            for (String m : shopGroupCodeList) {
                if (!RegexUtils.isValidStr(m, true)) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }
}
