package com.langtuo.teamachine.biz.service.impl;

import com.langtuo.teamachine.api.model.MachineModelDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.MachineModelMgtService;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.dao.accessor.MachineModelAccessor;
import com.langtuo.teamachine.dao.po.MachineModelPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MachineModelMgtServiceImpl implements MachineModelMgtService {
    private static int DB_INSERT_RESULT_OK = 1;
    private static int DB_DELETE_RESULT_OK = 1;

    @Resource
    private MachineModelAccessor accessor;

    @Override
    public LangTuoResult<List<MachineModelDTO>> list() {
        LangTuoResult<List<MachineModelDTO>> langTuoResult = null;
        try {
            List<MachineModelPO> list = accessor.selectList();
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
    public LangTuoResult<MachineModelDTO> get(String modelCode) {
        LangTuoResult<MachineModelDTO> langTuoResult = null;
        try {
            MachineModelPO po = accessor.selectOne(modelCode);
            langTuoResult = LangTuoResult.success(po);
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(MachineModelDTO machineModelDTO) {
        if (machineModelDTO == null) {
            return null;
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int rtn = accessor.insert(convert(machineModelDTO));
            if (rtn == DB_INSERT_RESULT_OK) {
                langTuoResult = LangTuoResult.success();
            } else {
                langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
            }
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
            int rtn = accessor.delete(modelCode);
            if (rtn == DB_DELETE_RESULT_OK) {
                langTuoResult = LangTuoResult.success();
            } else {
                langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
            }
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
        dto.setModelCode(po.getModelCode());
        dto.setEnableFlowAll(po.getEnableFlowAll());
        dto.setExtraInfo(po.getExtraInfo());
        return dto;
    }

    private MachineModelPO convert(MachineModelDTO dto) {
        if (dto == null) {
            return null;
        }

        MachineModelPO po = new MachineModelPO();
        po.setModelCode(dto.getModelCode());
        po.setEnableFlowAll(dto.getEnableFlowAll());
        po.setExtraInfo(po.getExtraInfo());
        return po;
    }
}
