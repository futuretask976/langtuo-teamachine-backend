package com.langtuo.teamachine.api.request.deviceset;

import lombok.Data;

@Data
public class ModelPipelinePutRequest {
    /**
     * 同MachineModelPipelineDTO
     */
    private Integer pipelineNum;

    /**
     * 同MachineModelPipelineDTO
     */
    private Integer enableFreeze;

    /**
     * 同MachineModelPipelineDTO
     */
    private Integer enableWarm;
}
