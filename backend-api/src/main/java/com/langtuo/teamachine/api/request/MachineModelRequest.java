package com.langtuo.teamachine.api.request;

import java.util.List;
import java.util.Map;

public class MachineModelRequest {
    /**
     * 同MachineModelDTO
     */
    private String modelCode;

    /**
     * 同MachineModelDTO
     */
    private Integer enableFlowAll;

    /**
     * 同MachineModelDTO
     */
    private List<MachineModelPipelineRequest> pipelineList;

    /**
     * 同MachineModelDTO
     */
    private Map<String, String> extraInfo;

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public Integer getEnableFlowAll() {
        return enableFlowAll;
    }

    public void setEnableFlowAll(Integer enableFlowAll) {
        this.enableFlowAll = enableFlowAll;
    }

    public List<MachineModelPipelineRequest> getPipelineList() {
        return pipelineList;
    }

    public void setPipelineList(List<MachineModelPipelineRequest> pipelineList) {
        this.pipelineList = pipelineList;
    }

    public Map<String, String> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, String> extraInfo) {
        this.extraInfo = extraInfo;
    }
}
