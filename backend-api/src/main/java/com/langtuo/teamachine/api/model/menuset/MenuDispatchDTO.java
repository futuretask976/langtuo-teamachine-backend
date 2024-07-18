package com.langtuo.teamachine.api.model.menuset;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MenuDispatchDTO {
    /**
     * 菜单编码
     */
    private String menuCode;

    /**
     * 店铺编码
     */
    private List<String> shopGroupCodeList;
}
