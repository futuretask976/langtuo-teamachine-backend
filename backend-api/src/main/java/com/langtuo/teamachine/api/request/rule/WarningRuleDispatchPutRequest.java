package com.langtuo.teamachine.api.request.rule;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(warningRuleCode)) {
            return false;
        }
        if (shopGroupCodeList == null || shopGroupCodeList.size() == 0) {
            return false;
        }
        for (String s : shopGroupCodeList) {
            if (StringUtils.isBlank(s)) {
                return false;
            }
        }
        return true;
    }
}