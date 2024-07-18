package com.langtuo.teamachine.api.request.menuset;

import lombok.Data;

@Data
public class MenuSeriesRelPutRequest {
    /**
     * 菜单编码
     */
    private String menuCode;

    /**
     * 系列编码
     */
    private String seriesCode;
}
