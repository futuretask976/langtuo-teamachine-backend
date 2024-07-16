package com.langtuo.teamachine.api.model;

import lombok.Data;

import java.util.Date;

@Data
public class SeriesTeaRelDTO {
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
     * 系列编码
     */
    private String seriesCode;

    /**
     * 茶饮编码
     */
    private String teaCode;
}
