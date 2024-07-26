package com.langtuo.teamachine.biz.service.impl.deviceset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.deviceset.ModelDTO;
import com.langtuo.teamachine.api.model.deviceset.ModelPipelineDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.deviceset.ModelPipelinePutRequest;
import com.langtuo.teamachine.api.request.deviceset.ModelPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.deviceset.MachineModelMgtService;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.dao.accessor.deviceset.MachineModelAccessor;
import com.langtuo.teamachine.dao.accessor.deviceset.MachineModelPipelineAccessor;
import com.langtuo.teamachine.dao.po.deviceset.ModelPO;
import com.langtuo.teamachine.dao.po.deviceset.ModelPipelinePO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MachineModelMgtServiceImpl implements MachineModelMgtService {
    @Resource
    private MachineModelAccessor machineModelAccessor;

    @Resource
    private MachineModelPipelineAccessor machineModelPipelineAccessor;

    @Override
    public LangTuoResult<PageDTO<ModelDTO>> list() {
        LangTuoResult<PageDTO<ModelDTO>> langTuoResult = null;
        try {
            List<ModelPO> list = machineModelAccessor.selectList();
            List<ModelDTO> dtoList = list.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
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
            PageInfo<ModelPO> pageInfo = machineModelAccessor.search(modelCode, pageNum, pageSize);
            List<ModelDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<ModelDTO> get(String modelCode) {
        LangTuoResult<ModelDTO> langTuoResult = null;
        try {
            ModelPO modelPO = machineModelAccessor.selectOne(modelCode);
            ModelDTO modelDTO = convert(modelPO);

            List<ModelPipelinePO> pipelinePOList = machineModelPipelineAccessor.selectList(modelCode);
            if (!CollectionUtils.isEmpty(pipelinePOList)) {
                modelDTO.setPipelineList(convert(pipelinePOList));
            }
            langTuoResult = LangTuoResult.success(modelDTO);
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(ModelPutRequest modelPutRequest) {
        if (modelPutRequest == null
                || StringUtils.isBlank(modelPutRequest.getModelCode())
                || modelPutRequest.getEnableFlowAll() == null
                || CollectionUtils.isEmpty(modelPutRequest.getPipelineList())) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        String modelCode = modelPutRequest.getModelCode();
        ModelPO modelPO = convert(modelPutRequest);
        List<ModelPipelinePO> modelPipelinePOList = convert(modelCode, modelPutRequest.getPipelineList());

        LangTuoResult<Void> langTuoResult = null;
        try {
            ModelPO exist = machineModelAccessor.selectOne(modelCode);
            System.out.printf("$$$$$ MachineModelMgtServiceImpl#put exist=%s\n", exist);
            if (exist != null) {
                int updated = machineModelAccessor.update(modelPO);
                System.out.printf("$$$$$ MachineModelMgtServiceImpl#put updated=%s\n", updated);
            } else {
                int inserted = machineModelAccessor.insert(modelPO);
                System.out.printf("$$$$$ MachineModelMgtServiceImpl#put inserted=%s\n", inserted);
            }

            int deleted = machineModelPipelineAccessor.delete(modelCode);
            System.out.printf("$$$$$ MachineModelMgtServiceImpl#put deleted4Pipeline=%s\n", deleted);
            modelPipelinePOList.stream().forEach(po -> {
                int inserted = machineModelPipelineAccessor.insert(po);
                System.out.printf("$$$$$ MachineModelMgtServiceImpl#put inserted4Pipeline=%s\n", inserted);
            });
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String modelCode) {
        if (StringUtils.isEmpty(modelCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted4ModelCode = machineModelAccessor.delete(modelCode);
            System.out.printf("$$$$$ MachineModelMgtServiceImpl#put deleted4ModelCode=%s\n", deleted4ModelCode);
            int deleted4Pipeline = machineModelPipelineAccessor.delete(modelCode);
            System.out.printf("$$$$$ MachineModelMgtServiceImpl#put deleted4Pipeline=%s\n", deleted4Pipeline);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
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
        dto.setExtraInfo(po.getExtraInfo());
        return dto;
    }

    private ModelPO convert(ModelPutRequest request) {
        if (request == null) {
            return null;
        }

        ModelPO po = new ModelPO();
        po.setModelCode(request.getModelCode());
        po.setEnableFlowAll(request.getEnableFlowAll());
        po.setExtraInfo(po.getExtraInfo());
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
            dto.setId(po.getId());
            dto.setGmtCreated(po.getGmtCreated());
            dto.setGmtModified(po.getGmtModified());
            dto.setModelCode(po.getModelCode());
            dto.setPipelineNum(po.getPipelineNum());
            dto.setEnableFreeze(po.getEnableFreeze());
            dto.setEnableWarm(po.getEnableWarm());
            return dto;
        }).collect(Collectors.toList());
        return resultList;
    }
}
