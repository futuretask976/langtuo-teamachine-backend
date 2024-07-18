package com.langtuo.teamachine.dao.po.menuset;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class MenuDispatchPO {
    /**
     * 数据表id
     */
    private long id;

    /**
     * 数据表记录插入时间
     */
    private Date gmtCreated;

    /**
     * 数据表记录最近修改时间
     */
    private Date gmtModified;

    /**
     * 菜单编码
     */
    private String menuCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 租户编码
     */
    private String tenantCode;
}
