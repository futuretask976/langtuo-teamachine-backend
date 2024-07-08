package com.langtuo.teamachine.dao.po;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class SpecSubPO {
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
     * 规格编码
     */
    private String specCode;

    /**
     * 子规格编码
     */
    private String specSubCode;

    /**
     * 子规格名称
     */
    private String specSubName;

    /**
     * 外部子规格编码
     */
    private String outerSpecSubCode;

    /**
     * 租户编码
     */
    private String tenantCode;
}
