package com.langtuo.teamachine.dao.po.drink;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class TeaTypePO {
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
     * 茶品类型编码
     */
    private String teaTypeCode;

    /**
     * 茶品类型名称
     */
    private String teaTypeName;

    /**
     * 状态，0：禁用，1：启用
     */
    private int state;
}
