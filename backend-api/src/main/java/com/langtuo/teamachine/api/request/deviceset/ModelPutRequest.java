package com.langtuo.teamachine.api.request.deviceset;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Data
public class ModelPutRequest {
    /**
     * 型号编码
     */
    private String modelCode;

    /**
     * 是否启用同时出料，0：禁用，1：启用
     */
    private int enableFlowAll;

    /**
     * 同MachineModelDTO
     */
    private List<ModelPipelinePutRequest> pipelineList;

    /**
     * 同MachineModelDTO
     */
    private Map<String, String> extraInfo;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(modelCode)) {
            return false;
        }
        if (pipelineList == null || pipelineList.size() == 0) {
            return false;
        }
        for (ModelPipelinePutRequest m : pipelineList) {
            if (!m.isValid()) {
                return false;
            }
        }
        return true;
    }
}
