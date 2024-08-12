package com.langtuo.teamachine.api.request.drink;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Data
public class ToppingTypePutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 备注
     */
    private String comment;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 物料类型编码
     */
    private String toppingTypeCode;

    /**
     * 物料类型名称
     */
    private String toppingTypeName;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(toppingTypeCode)
                || StringUtils.isBlank(toppingTypeName)) {
            return false;
        }
        return true;
    }
}
