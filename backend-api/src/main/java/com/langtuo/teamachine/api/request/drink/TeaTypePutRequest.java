package com.langtuo.teamachine.api.request.drink;

import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

import java.util.Map;

@Data
public class TeaTypePutRequest {
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
     * 茶类型编码
     */
    private String teaTypeCode;

    /**
     * 茶类型名称
     */
    private String teaTypeName;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (RegexUtils.isValidCode(tenantCode, true)
                // && RegexUtils.isValidCode(comment, false)
                && RegexUtils.isValidCode(teaTypeCode, true)
                && RegexUtils.isValidName(teaTypeName, true)) {
            return true;
        }
        return false;
    }
}
