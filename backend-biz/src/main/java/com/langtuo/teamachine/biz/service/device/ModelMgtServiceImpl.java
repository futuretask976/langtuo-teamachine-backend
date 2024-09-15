package com.langtuo.teamachine.biz.service.device;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.device.ModelDTO;
import com.langtuo.teamachine.api.model.device.ModelPipelineDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.device.ModelPipelinePutRequest;
import com.langtuo.teamachine.api.request.device.ModelPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.device.ModelMgtService;
import com.langtuo.teamachine.biz.aync.AsyncDispatcher;
import com.langtuo.teamachine.dao.accessor.device.MachineAccessor;
import com.langtuo.teamachine.dao.accessor.device.ModelAccessor;
import com.langtuo.teamachine.dao.accessor.device.ModelPipelineAccessor;
import com.langtuo.teamachine.dao.po.device.ModelPO;
import com.langtuo.teamachine.dao.po.device.ModelPipelinePO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
    private MachineAccessor machineAccessor;

    @Resource
    private AsyncDispatcher asyncDispatcher;

    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<List<ModelDTO>> list() {
        TeaMachineResult<List<ModelDTO>> teaMachineResult;
        try {
            List<ModelPO> list = modelAccessor.selectList();
            List<ModelDTO> dtoList = convertToModelDTO(list);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<ModelDTO>> search(String modelCode,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<ModelDTO>> teaMachineResult;
        try {
            PageInfo<ModelPO> pageInfo = modelAccessor.search(modelCode, pageNum, pageSize);
            List<ModelDTO> dtoList = convertToModelDTO(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<ModelDTO> get(String modelCode) {
        TeaMachineResult<ModelDTO> teaMachineResult;
        try {
            ModelPO modelPO = modelAccessor.selectOneByModelCode(modelCode);
            ModelDTO modelDTO = convert(modelPO);

            List<ModelPipelinePO> pipelinePOList = modelPipelineAccessor.selectListByModelCode(modelCode);
            if (!CollectionUtils.isEmpty(pipelinePOList)) {
                modelDTO.setPipelineList(convert(pipelinePOList));
            }
            teaMachineResult = TeaMachineResult.success(modelDTO);
        } catch (Exception e) {
            log.error("get error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(ModelPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        ModelPO modelPO = convert(request);
        List<ModelPipelinePO> modelPipelinePOList = convert(modelPO.getModelCode(), request.getPipelineList());

        TeaMachineResult<Void> teaMachineResult;
        if (request.isNewPut()) {
            teaMachineResult = putNew(modelPO, modelPipelinePOList);
        } else {
            teaMachineResult = putUpdate(modelPO, modelPipelinePOList);
        }

        // 异步发送消息准备配置信息分发
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_MODEL_UPDATED);
        jsonPayload.put(CommonConsts.JSON_KEY_MODEL_CODE, request.getModelCode());
        asyncDispatcher.dispatch(jsonPayload);

        return teaMachineResult;
    }

    private TeaMachineResult<Void> putNew(ModelPO po, List<ModelPipelinePO> modelPipelinePOList) {
        try {
            ModelPO exist = modelAccessor.selectOneByModelCode(po.getModelCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = modelAccessor.insert(po);
            if (inserted != CommonConsts.NUM_ONE) {
                log.error("modelMgtService|putNewModel|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }

            int deleted = modelPipelineAccessor.deleteByModelCode(po.getModelCode());
            for (ModelPipelinePO modelPipelinePO : modelPipelinePOList) {
                int inserted4Pipeline = modelPipelineAccessor.insert(modelPipelinePO);
                if (inserted4Pipeline != CommonConsts.NUM_ONE) {
                    log.error("modelMgtService|putNewPipeline|error|" + inserted);
                    return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("modelMgtService|putNew|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(ModelPO po, List<ModelPipelinePO> modelPipelinePOList) {
        try {
            ModelPO exist = modelAccessor.selectOneByModelCode(po.getModelCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
            }

            int updated = modelAccessor.update(po);
            if (updated != CommonConsts.NUM_ONE) {
                log.error("modelMgtService|putUpdateModel|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }

            int deleted = modelPipelineAccessor.deleteByModelCode(po.getModelCode());
            for (ModelPipelinePO modelPipelinePO : modelPipelinePOList) {
                int inserted4Pipeline = modelPipelineAccessor.insert(modelPipelinePO);
                if (inserted4Pipeline != CommonConsts.NUM_ONE) {
                    log.error("modelMgtService|putUpdatePipeline|error|" + inserted4Pipeline);
                    return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("modelMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String modelCode) {
        if (StringUtils.isEmpty(modelCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int countByModelCode = machineAccessor.countByModelCode(modelCode);
            if (countByModelCode == CommonConsts.DB_SELECT_RESULT_EMPTY) {
                int deleted4ModelCode = modelAccessor.deleteByModelCode(modelCode);
                int deleted4Pipeline = modelPipelineAccessor.deleteByModelCode(modelCode);
                teaMachineResult = TeaMachineResult.success();
            } else {
                teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_MODEL));
            }
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
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
