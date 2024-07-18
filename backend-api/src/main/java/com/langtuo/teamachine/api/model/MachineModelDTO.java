package com.langtuo.teamachine.api.model;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MachineModelDTO {
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
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 型号编码
     */
    private String modelCode;

    /**
     * 是否支持同时出料，0：不支持，1：支持
     */
    private Integer enableFlowAll;

    /**
     * 管道列表
     */
    private List<MachineModelPipelineDTO> pipelineList;
}
