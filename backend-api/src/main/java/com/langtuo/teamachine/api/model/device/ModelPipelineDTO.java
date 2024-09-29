package com.langtuo.teamachine.api.model.device;

import lombok.Data;

@Data
public class ModelPipelineDTO {
    /**
     * 型号编码
     */
    private String modelCode;

    /**
     * 管道号码
     */
    private int pipelineNum;

    /**
     * 是否支持冷藏，0：不支持，1：支持
     */
    private int enableFreeze;

    /**
     * 是否支持加热，0：不支持，1：支持
     */
    private int enableWarm;

    /**
     * 容量
     */
    private int capacity;
}
