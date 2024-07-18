package com.langtuo.teamachine.api.request;

import lombok.Data;

@Data
public class MachineModelPipelinePutRequest {
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
