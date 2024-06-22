package com.langtuo.teamachine.api.request;

public class MachineModelPipelineRequest {
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

    public Integer getPipelineNum() {
        return pipelineNum;
    }

    public void setPipelineNum(Integer pipelineNum) {
        this.pipelineNum = pipelineNum;
    }

    public Integer getEnableFreeze() {
        return enableFreeze;
    }

    public void setEnableFreeze(Integer enableFreeze) {
        this.enableFreeze = enableFreeze;
    }

    public Integer getEnableWarm() {
        return enableWarm;
    }

    public void setEnableWarm(Integer enableWarm) {
        this.enableWarm = enableWarm;
    }
}
