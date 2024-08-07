package com.langtuo.teamachine.api.request.drink;

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
     * 状态，0：禁用，1：启用
     */
    private int state;

    /**
     *
     */
    private List<SpecItemPutRequest> specItemList;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(specCode)
                || StringUtils.isBlank(specName)) {
            return false;
        }
        if (specItemList == null || specItemList.size() == 0) {
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
