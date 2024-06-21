package com.langtuo.teamachine.biz.service.impl;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.MachineModelDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.MachineModelMgtService;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.biz.service.constant.DBOpeConts;
import com.langtuo.teamachine.dao.accessor.MachineModelAccessor;
import com.langtuo.teamachine.dao.po.MachineModelPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MachineModelMgtServiceImpl implements MachineModelMgtService {
    @Resource
    private MachineModelAccessor accessor;

    @Override
    public LangTuoResult<PageDTO<MachineModelDTO>> list(int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<MachineModelDTO>> langTuoResult = null;
        try {
            PageInfo<MachineModelPO> pageInfo = accessor.selectList(pageNum, pageSize);
            List<MachineModelDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            PageDTO<MachineModelDTO> pageDTO = new PageDTO<>();
            pageDTO.setList(dtoList);
            pageDTO.setPageNum(pageNum);
            pageDTO.setPageSize(pageSize);
            pageDTO.setTotal(pageInfo.getTotal());

            langTuoResult = LangTuoResult.success(pageDTO);
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
            PageInfo<MachineModelPO> pageInfo = accessor.search(modelCode, pageNum, pageSize);
            List<MachineModelDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            PageDTO<MachineModelDTO> pageDTO = new PageDTO<>();
            pageDTO.setList(dtoList);
            pageDTO.setPageNum(pageNum);
            pageDTO.setPageSize(pageSize);
            pageDTO.setTotal(pageInfo.getTotal());

            langTuoResult = LangTuoResult.success(pageDTO);
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
            if (rtn == DBOpeConts.DB_OPE_INSERT_RESULT_OK) {
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
            if (rtn == DBOpeConts.DB_OPE_DELETE_RESULT_OK) {
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
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
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
