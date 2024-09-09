package com.langtuo.teamachine.api.request.device;

import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

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
        if (RegexUtils.isValidCode(modelCode, true)
                && isValidPipelineList()) {
            return true;
        }
        return false;
    }

    private boolean isValidPipelineList() {
        if (CollectionUtils.isEmpty(pipelineList)) {
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
