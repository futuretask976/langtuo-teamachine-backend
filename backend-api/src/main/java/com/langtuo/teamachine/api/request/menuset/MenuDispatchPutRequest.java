package com.langtuo.teamachine.api.request.menuset;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class MenuDispatchPutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 菜单编码
     */
    private String menuCode;

    /**
     * 店铺编码
     */
    private List<String> shopGroupCodeList;
}
