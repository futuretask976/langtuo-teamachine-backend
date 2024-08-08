package com.langtuo.teamachine.api.model.menu;

import lombok.Data;

import java.util.Date;

@Data
public class MenuSeriesRelDTO {
    /**
     * 菜单编码
     */
    private String menuCode;

    /**
     * 系列编码
     */
    private String seriesCode;
}
