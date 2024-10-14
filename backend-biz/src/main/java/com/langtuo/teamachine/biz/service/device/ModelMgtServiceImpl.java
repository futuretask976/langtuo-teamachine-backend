package com.langtuo.teamachine.biz.service.device;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.device.ModelDTO;
import com.langtuo.teamachine.api.request.device.ModelPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.device.ModelMgtService;
import com.langtuo.teamachine.biz.aync.AsyncDispatcher;
import com.langtuo.teamachine.biz.convert.device.ModelMgtConvertor;
import com.langtuo.teamachine.dao.accessor.device.DeployAccessor;
import com.langtuo.teamachine.dao.accessor.device.MachineAccessor;
import com.langtuo.teamachine.dao.accessor.device.ModelAccessor;
import com.langtuo.teamachine.dao.accessor.device.ModelPipelineAccessor;
import com.langtuo.teamachine.dao.po.device.ModelPO;
import com.langtuo.teamachine.dao.po.device.ModelPipelinePO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.LocaleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.langtuo.teamachine.biz.convert.device.ModelMgtConvertor.convertToModelDTO;

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
    private DeployAccessor deployAccessor;

    @Resource
    private AsyncDispatcher asyncDispatcher;

    @Override
    public TeaMachineResult<List<ModelDTO>> list() {
        try {
            List<ModelPO> list = modelAccessor.list();
            List<ModelDTO> dtoList = convertToModelDTO(list);
            return TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("modelMgtService|list|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<PageDTO<ModelDTO>> search(String modelCode,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<ModelPO> pageInfo = modelAccessor.search(modelCode, pageNum, pageSize);
            List<ModelDTO> dtoList = convertToModelDTO(pageInfo.getList());
            return TeaMachineResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("modelMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<ModelDTO> getByModelCode(String modelCode) {
        if (StringUtils.isBlank(modelCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            ModelPO modelPO = modelAccessor.getByModelCode(modelCode);
            ModelDTO modelDTO = ModelMgtConvertor.convertToModelDTO(modelPO);
            return TeaMachineResult.success(modelDTO);
        } catch (Exception e) {
            log.error("modelMgtService|get|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(ModelPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        ModelPO modelPO = ModelMgtConvertor.convertToModelDTO(request);
        List<ModelPipelinePO> modelPipelinePOList = ModelMgtConvertor.convertToModelDTO(modelPO.getModelCode(), request.getPipelineList());

        TeaMachineResult<Void> teaMachineResult;
        if (request.isPutNew()) {
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
            ModelPO exist = modelAccessor.getByModelCode(po.getModelCode());
            if (exist != null) {
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = modelAccessor.insert(po);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                log.error("modelMgtService|putNewModel|error|" + inserted);
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }

            int deleted = modelPipelineAccessor.deleteByModelCode(po.getModelCode());
            for (ModelPipelinePO modelPipelinePO : modelPipelinePOList) {
                int inserted4Pipeline = modelPipelineAccessor.insert(modelPipelinePO);
                if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4Pipeline) {
                    log.error("modelMgtService|putNewPipeline|error|" + inserted);
                    return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("modelMgtService|putNew|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(ModelPO po, List<ModelPipelinePO> modelPipelinePOList) {
        try {
            ModelPO exist = modelAccessor.getByModelCode(po.getModelCode());
            if (exist == null) {
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
            }

            int updated = modelAccessor.update(po);
            if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
                log.error("modelMgtService|putUpdateModel|error|" + updated);
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }

            int deleted = modelPipelineAccessor.deleteByModelCode(po.getModelCode());
            for (ModelPipelinePO modelPipelinePO : modelPipelinePOList) {
                int inserted4Pipeline = modelPipelineAccessor.insert(modelPipelinePO);
                if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4Pipeline) {
                    log.error("modelMgtService|putUpdatePipeline|error|" + inserted4Pipeline);
                    return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("modelMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> deleteByModelCode(String modelCode) {
        if (StringUtils.isEmpty(modelCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            int countDeployByModelCode = deployAccessor.countByModelCode(modelCode);
            if (CommonConsts.DB_COUNT_RESULT_ZERO != countDeployByModelCode) {
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
            }

            int countMachineByModelCode = machineAccessor.countByModelCode(modelCode);
            if (CommonConsts.DB_COUNT_RESULT_ZERO != countMachineByModelCode) {
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
            }

            int deleted4ModelCode = modelAccessor.deleteByModelCode(modelCode);
            int deleted4Pipeline = modelPipelineAccessor.deleteByModelCode(modelCode);
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("modelMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }
}
