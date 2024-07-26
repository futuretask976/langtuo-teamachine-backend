package com.langtuo.teamachine.api.request.deviceset;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ModelPutRequest {
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
    private List<ModelPipelinePutRequest> pipelineList;

    /**
     * 同MachineModelDTO
     */
    private Map<String, String> extraInfo;
}
