package com.langtuo.teamachine.biz.service.impl.device;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.device.ModelDTO;
import com.langtuo.teamachine.api.model.device.ModelPipelineDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.device.ModelPipelinePutRequest;
import com.langtuo.teamachine.api.request.device.ModelPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.device.ModelMgtService;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.dao.accessor.device.ModelAccessor;
import com.langtuo.teamachine.dao.accessor.device.ModelPipelineAccessor;
import com.langtuo.teamachine.dao.po.device.ModelPO;
import com.langtuo.teamachine.dao.po.device.ModelPipelinePO;
import com.langtuo.teamachine.mqtt.publish.MqttPublisher4Console;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ModelMgtServiceImpl implements ModelMgtService {
    @Resource
    private ModelAccessor modelAccessor;

    @Resource
    private ModelPipelineAccessor modelPipelineAccessor;

    @Resource
    private MqttPublisher4Console mqttPublisher4Console;

    @Override
    public LangTuoResult<List<ModelDTO>> list() {
        LangTuoResult<List<ModelDTO>> langTuoResult = null;
        try {
            List<ModelPO> list = modelAccessor.selectList();
            List<ModelDTO> dtoList = convertToModelDTO(list);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<ModelDTO>> search(String modelCode,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<ModelDTO>> langTuoResult = null;
        try {
            PageInfo<ModelPO> pageInfo = modelAccessor.search(modelCode, pageNum, pageSize);
            List<ModelDTO> dtoList = convertToModelDTO(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<ModelDTO> get(String modelCode) {
        LangTuoResult<ModelDTO> langTuoResult = null;
        try {
            ModelPO modelPO = modelAccessor.selectOne(modelCode);
            ModelDTO modelDTO = convert(modelPO);

            List<ModelPipelinePO> pipelinePOList = modelPipelineAccessor.selectList(modelCode);
            if (!CollectionUtils.isEmpty(pipelinePOList)) {
                modelDTO.setPipelineList(convert(pipelinePOList));
            }
            langTuoResult = LangTuoResult.success(modelDTO);
        } catch (Exception e) {
            log.error("get error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(ModelPutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        String modelCode = request.getModelCode();
        ModelPO modelPO = convert(request);
        List<ModelPipelinePO> modelPipelinePOList = convert(modelCode, request.getPipelineList());

        LangTuoResult<Void> langTuoResult = null;
        try {
            ModelPO exist = modelAccessor.selectOne(modelCode);
            if (exist != null) {
                int updated = modelAccessor.update(modelPO);
            } else {
                int inserted = modelAccessor.insert(modelPO);
            }

            int deleted = modelPipelineAccessor.delete(modelCode);
            modelPipelinePOList.stream().forEach(po -> {
                int inserted = modelPipelineAccessor.insert(po);
            });
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }

        // 异步发送消息准备配置信息分发
        mqttPublisher4Console.send4Model(request.getModelCode());

        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String modelCode) {
        if (StringUtils.isEmpty(modelCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted4ModelCode = modelAccessor.delete(modelCode);
            int deleted4Pipeline = modelPipelineAccessor.delete(modelCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private List<ModelDTO> convertToModelDTO(List<ModelPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<ModelDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private ModelDTO convert(ModelPO po) {
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

    private ModelPO convert(ModelPutRequest request) {
        if (request == null) {
            return null;
        }

        ModelPO po = new ModelPO();
        po.setModelCode(request.getModelCode());
        po.setEnableFlowAll(request.getEnableFlowAll());
        return po;
    }

    private List<ModelPipelinePO> convert(String modelCode, List<ModelPipelinePutRequest> requestList) {
        if (CollectionUtils.isEmpty(requestList)) {
            return null;
        }

        List<ModelPipelinePO> resultList = requestList.stream().map(request -> {
            ModelPipelinePO po = new ModelPipelinePO();
            po.setModelCode(modelCode);
            po.setPipelineNum(request.getPipelineNum());
            po.setEnableFreeze(request.getEnableFreeze());
            po.setEnableWarm(request.getEnableWarm());
            return po;
        }).collect(Collectors.toList());
        return resultList;
    }

    private List<ModelPipelineDTO> convert(List<ModelPipelinePO> pipelinePOList) {
        if (CollectionUtils.isEmpty(pipelinePOList)) {
            return null;
        }

        List<ModelPipelineDTO> resultList = pipelinePOList.stream().map(po -> {
            ModelPipelineDTO dto = new ModelPipelineDTO();
            dto.setModelCode(po.getModelCode());
            dto.setPipelineNum(po.getPipelineNum());
            dto.setEnableFreeze(po.getEnableFreeze());
            dto.setEnableWarm(po.getEnableWarm());
            return dto;
        }).collect(Collectors.toList());
        return resultList;
    }
}
