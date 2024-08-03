package com.langtuo.teamachine.api.request.menu;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
     * 图片链接
     */
    private String imgLink;

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
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(menuCode)
                || StringUtils.isBlank(menuName)
                || StringUtils.isBlank(imgLink)
                || validFrom == null) {
            return false;
        }
        if (menuSeriesRelList == null || menuSeriesRelList.size() == 0) {
            return false;
        }
        for (MenuSeriesRelPutRequest m : menuSeriesRelList) {
            if (!m.isValid()) {
                return false;
            }
        }
        return true;
    }
}
