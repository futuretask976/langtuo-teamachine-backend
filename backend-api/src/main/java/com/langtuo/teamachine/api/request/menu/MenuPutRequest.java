package com.langtuo.teamachine.api.request.menu;

import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class MenuPutRequest {
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
     * 菜单编码
     */
    private String menuCode;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 生效时间
     */
    private Date validFrom;

    /**
     * 菜单-系列关系
     */
    private List<MenuSeriesRelPutRequest> menuSeriesRelList;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (RegexUtils.isValidCode(tenantCode, true)
                // && RegexUtils.isValidCode(comment, false)
                && RegexUtils.isValidCode(menuCode, true)
                && RegexUtils.isValidName(menuName, true)
                && isValidPipelineList()
                && validFrom != null) {
            return true;
        }
        return false;
    }

    private boolean isValidPipelineList() {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(menuSeriesRelList)) {
            isValid = false;
        } else {
            for (MenuSeriesRelPutRequest m : menuSeriesRelList) {
                if (!m.isValid()) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }
}
