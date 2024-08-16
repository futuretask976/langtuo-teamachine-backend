package com.langtuo.teamachine.api.request.drink;

import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TeaPutRequest {
    /**
     * 茶编码
     */
    private String teaCode;

    /**
     * 茶名称
     */
    private String teaName;

    /**
     * 外部茶编码
     */
    private String outerTeaCode;

    /**
     * 状态，0：禁用，1：启用
     */
    private int state;

    /**
     * 茶类型编码
     */
    private String teaTypeCode;

    /**
     * 茶品图片链接
     */
    private String imgLink;

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
     * 茶-物料关系列表
     */
    private List<TeaUnitPutRequest> teaUnitList;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (RegexUtils.isValidCode(tenantCode, true)
                // && RegexUtils.isValidCode(comment, false)
                && RegexUtils.isValidCode(teaCode, true)
                && RegexUtils.isValidName(teaName, true)
                && RegexUtils.isValidCode(outerTeaCode, true)
                && RegexUtils.isValidCode(teaTypeCode, true)
                // && RegexUtils.isValidCode(imgLink, true)
                && isValidTeaUnitList()) {
            return true;
        }
        return false;
    }

    private boolean isValidTeaUnitList() {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(teaUnitList)) {
            isValid = false;
        } else {
            for (TeaUnitPutRequest s : teaUnitList) {
                if (!s.isValid()) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }
}
