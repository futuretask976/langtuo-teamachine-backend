package com.langtuo.teamachine.api.request.device;

import lombok.Data;

@Data
public class ModelPipelinePutRequest {
    /**
     * 同MachineModelPipelineDTO
     */
    private int pipelineNum;

    /**
     * 同MachineModelPipelineDTO
     */
    private int enableFreeze;

    /**
     * 同MachineModelPipelineDTO
     */
    private int enableWarm;

    /**
     * 是否新建
     */
    private boolean newPut;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (pipelineNum == 0) {
            return false;
        }
        return true;
    }
}
