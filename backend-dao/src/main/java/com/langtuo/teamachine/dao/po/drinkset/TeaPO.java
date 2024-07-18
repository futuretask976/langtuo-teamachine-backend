package com.langtuo.teamachine.dao.po.drinkset;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class TeaPO {
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
    private Integer state;

    /**
     * 茶类型编码
     */
    private String teaTypeCode;

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
}
