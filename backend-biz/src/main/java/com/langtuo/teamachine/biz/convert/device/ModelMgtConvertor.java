package com.langtuo.teamachine.biz.convert.device;

import com.langtuo.teamachine.api.model.device.ModelDTO;
import com.langtuo.teamachine.api.model.device.ModelPipelineDTO;
import com.langtuo.teamachine.api.request.device.ModelPipelinePutRequest;
import com.langtuo.teamachine.api.request.device.ModelPutRequest;
import com.langtuo.teamachine.dao.po.device.ModelPO;
import com.langtuo.teamachine.dao.po.device.ModelPipelinePO;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ModelMgtConvertor {
    public static List<ModelDTO> convertToModelDTO(List<ModelPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<ModelDTO> list = poList.stream()
                .map(po -> convertToModelDTO(po))
                .collect(Collectors.toList());
        return list;
    }

    public static ModelDTO convertToModelDTO(ModelPO po) {
        if (po == null) {
            return null;
        }

        ModelDTO dto = new ModelDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setModelCode(po.getModelCode());
        dto.setEnableFlowAll(po.getEnableFlowAll());
        return dto;
    }

    public static ModelPO convertToModelDTO(ModelPutRequest request) {
        if (request == null) {
            return null;
        }

        ModelPO po = new ModelPO();
        po.setModelCode(request.getModelCode());
        po.setEnableFlowAll(request.getEnableFlowAll());
        return po;
    }

    public static List<ModelPipelinePO> convertToModelDTO(String modelCode, List<ModelPipelinePutRequest> requestList) {
        if (CollectionUtils.isEmpty(requestList)) {
            return null;
        }

        List<ModelPipelinePO> resultList = requestList.stream().map(request -> {
            ModelPipelinePO po = new ModelPipelinePO();
            po.setModelCode(modelCode);
            po.setPipelineNum(request.getPipelineNum());
            po.setEnableFreeze(request.getEnableFreeze());
            po.setEnableWarm(request.getEnableWarm());
            po.setCapacity(request.getCapacity());
            return po;
        }).collect(Collectors.toList());
        return resultList;
    }

    public static List<ModelPipelineDTO> convertToModelPipelineDTO(List<ModelPipelinePO> pipelinePOList) {
        if (CollectionUtils.isEmpty(pipelinePOList)) {
            return null;
        }

        List<ModelPipelineDTO> resultList = pipelinePOList.stream().map(po -> {
            ModelPipelineDTO dto = new ModelPipelineDTO();
            dto.setModelCode(po.getModelCode());
            dto.setPipelineNum(po.getPipelineNum());
            dto.setEnableFreeze(po.getEnableFreeze());
            dto.setEnableWarm(po.getEnableWarm());
            dto.setCapacity(po.getCapacity());
            return dto;
        }).collect(Collectors.toList());
        return resultList;
    }
}
