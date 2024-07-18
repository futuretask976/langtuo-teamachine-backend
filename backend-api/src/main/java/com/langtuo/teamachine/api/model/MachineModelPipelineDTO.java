package com.langtuo.teamachine.api.model;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class MachineModelPipelineDTO {
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
     * 型号编码
     */
    private String modelCode;

    /**
     * 管道号码
     */
    private Integer pipelineNum;

    /**
     * 是否支持冷藏，0：不支持，1：支持
     */
    private Integer enableFreeze;

    /**
     * 是否支持加热，0：不支持，1：支持
     */
    private Integer enableWarm;
}
