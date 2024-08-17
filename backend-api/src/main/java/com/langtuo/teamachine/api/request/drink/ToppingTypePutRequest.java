package com.langtuo.teamachine.api.request.drink;

import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

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
        if (RegexUtils.isValidCode(tenantCode, true)
                && RegexUtils.isValidComment(comment, false)
                && RegexUtils.isValidCode(toppingTypeCode, true)
                && RegexUtils.isValidName(toppingTypeName, true)) {
            return true;
        }
        return false;
    }
}
