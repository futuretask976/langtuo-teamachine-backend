package com.langtuo.teamachine.api.request.drink;

import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SpecPutRequest {
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
     * 规格编码
     */
    private String specCode;

    /**
     * 规格名称
     */
    private String specName;

    /**
     *
     */
    private List<SpecItemPutRequest> specItemList;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (RegexUtils.isValidCode(tenantCode, true)
                && RegexUtils.isValidComment(comment, false)
                && RegexUtils.isValidCode(specCode, true)
                && RegexUtils.isValidName(specName, true)
                && isValidSpecItem()) {
            return true;
        }
        return false;
    }

    private boolean isValidSpecItem() {
        if (CollectionUtils.isEmpty(specItemList)) {
            return false;
        }
        for (SpecItemPutRequest s : specItemList) {
            if (!s.isValid()) {
                return false;
            }
        }
        return true;
    }
}
