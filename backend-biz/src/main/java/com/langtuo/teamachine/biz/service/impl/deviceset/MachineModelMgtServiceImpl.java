package com.langtuo.teamachine.biz.service.impl.deviceset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.deviceset.MachineModelDTO;
import com.langtuo.teamachine.api.model.deviceset.MachineModelPipelineDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.deviceset.MachineModelPipelinePutRequest;
import com.langtuo.teamachine.api.request.deviceset.MachineModelPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.deviceset.MachineModelMgtService;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.dao.accessor.deviceset.MachineModelAccessor;
import com.langtuo.teamachine.dao.accessor.deviceset.MachineModelPipelineAccessor;
import com.langtuo.teamachine.dao.po.deviceset.MachineModelPO;
import com.langtuo.teamachine.dao.po.deviceset.MachineModelPipelinePO;
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
    public LangTuoResult<PageDTO<MachineModelDTO>> list() {
        LangTuoResult<PageDTO<MachineModelDTO>> langTuoResult = null;
        try {
            List<MachineModelPO> list = machineModelAccessor.selectList();
            List<MachineModelDTO> dtoList = list.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<MachineModelDTO>> search(String modelCode,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<MachineModelDTO>> langTuoResult = null;
        try {
            PageInfo<MachineModelPO> pageInfo = machineModelAccessor.search(modelCode, pageNum, pageSize);
            List<MachineModelDTO> dtoList = pageInfo.getList().stream()
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
    public LangTuoResult<MachineModelDTO> get(String modelCode) {
        LangTuoResult<MachineModelDTO> langTuoResult = null;
        try {
            MachineModelPO machineModelPO = machineModelAccessor.selectOne(modelCode);
            MachineModelDTO machineModelDTO = convert(machineModelPO);

            List<MachineModelPipelinePO> pipelinePOList = machineModelPipelineAccessor.selectList(modelCode);
            if (!CollectionUtils.isEmpty(pipelinePOList)) {
                machineModelDTO.setPipelineList(convert(pipelinePOList));
            }
            langTuoResult = LangTuoResult.success(machineModelDTO);
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(MachineModelPutRequest machineModelPutRequest) {
        if (machineModelPutRequest == null
                || StringUtils.isBlank(machineModelPutRequest.getModelCode())
                || machineModelPutRequest.getEnableFlowAll() == null
                || CollectionUtils.isEmpty(machineModelPutRequest.getPipelineList())) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        String modelCode = machineModelPutRequest.getModelCode();
        MachineModelPO machineModelPO = convert(machineModelPutRequest);
        List<MachineModelPipelinePO> machineModelPipelinePOList = convert(modelCode, machineModelPutRequest.getPipelineList());

        LangTuoResult<Void> langTuoResult = null;
        try {
            MachineModelPO exist = machineModelAccessor.selectOne(modelCode);
            System.out.printf("$$$$$ MachineModelMgtServiceImpl#put exist=%s\n", exist);
            if (exist != null) {
                int updated = machineModelAccessor.update(machineModelPO);
                System.out.printf("$$$$$ MachineModelMgtServiceImpl#put updated=%s\n", updated);
            } else {
                int inserted = machineModelAccessor.insert(machineModelPO);
                System.out.printf("$$$$$ MachineModelMgtServiceImpl#put inserted=%s\n", inserted);
            }

            int deleted = machineModelPipelineAccessor.delete(modelCode);
            System.out.printf("$$$$$ MachineModelMgtServiceImpl#put deleted4Pipeline=%s\n", deleted);
            machineModelPipelinePOList.stream().forEach(po -> {
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

    private MachineModelDTO convert(MachineModelPO po) {
        if (po == null) {
            return null;
        }

        MachineModelDTO dto = new MachineModelDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setModelCode(po.getModelCode());
        dto.setEnableFlowAll(po.getEnableFlowAll());
        dto.setExtraInfo(po.getExtraInfo());
        return dto;
    }

    private MachineModelPO convert(MachineModelPutRequest request) {
        if (request == null) {
            return null;
        }

        MachineModelPO po = new MachineModelPO();
        po.setModelCode(request.getModelCode());
        po.setEnableFlowAll(request.getEnableFlowAll());
        po.setExtraInfo(po.getExtraInfo());
        return po;
    }

    private List<MachineModelPipelinePO> convert(String modelCode, List<MachineModelPipelinePutRequest> requestList) {
        if (CollectionUtils.isEmpty(requestList)) {
            return null;
        }

        List<MachineModelPipelinePO> resultList = requestList.stream().map(request -> {
            MachineModelPipelinePO po = new MachineModelPipelinePO();
            po.setModelCode(modelCode);
            po.setPipelineNum(request.getPipelineNum());
            po.setEnableFreeze(request.getEnableFreeze());
            po.setEnableWarm(request.getEnableWarm());
            return po;
        }).collect(Collectors.toList());
        return resultList;
    }

    private List<MachineModelPipelineDTO> convert(List<MachineModelPipelinePO> pipelinePOList) {
        if (CollectionUtils.isEmpty(pipelinePOList)) {
            return null;
        }

        List<MachineModelPipelineDTO> resultList = pipelinePOList.stream().map(po -> {
            MachineModelPipelineDTO dto = new MachineModelPipelineDTO();
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
