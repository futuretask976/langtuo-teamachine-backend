package com.langtuo.teamachine.api.request.drink;

import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
        if (RegexUtils.isValidStr(tenantCode, true)
                && RegexUtils.isValidStr(comment, false)
                && RegexUtils.isValidStr(specCode, true)
                && RegexUtils.isValidStr(specName, true)
                && isValidSpecItem()) {
            return true;
        }
        return false;
    }

    private boolean isValidSpecItem() {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(specItemList)) {
            isValid = false;
        } else {
            for (SpecItemPutRequest s : specItemList) {
                if (!s.isValid()) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }
}
