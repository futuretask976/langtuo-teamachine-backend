package com.langtuo.teamachine.api.request.report;

import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

@Data
public class OrderReportPutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 日期
     */
    private String orderCreatedDay;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (!RegexUtils.isValidCode(tenantCode, true)) {
            return false;
        }
        if (StringUtils.isBlank(orderCreatedDay)) {
            return false;
        }
        return true;
    }
}
