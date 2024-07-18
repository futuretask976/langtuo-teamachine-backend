package com.langtuo.teamachine.biz.service.impl.drinkset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drinkset.TeaTypeDTO;
import com.langtuo.teamachine.api.request.drinkset.TeaTypePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drinkset.TeaTypeMgtService;
import com.langtuo.teamachine.dao.accessor.drinkset.TeaTypeAccessor;
import com.langtuo.teamachine.dao.po.drinkset.TeaTypePO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeaTypeMgtServiceImpl implements TeaTypeMgtService {
    @Resource
    private TeaTypeAccessor accessor;

    @Override
    public LangTuoResult<List<TeaTypeDTO>> list(String tenantCode) {
        LangTuoResult<List<TeaTypeDTO>> langTuoResult = null;
        try {
            List<TeaTypePO> list = accessor.selectList(tenantCode);
            List<TeaTypeDTO> dtoList = list.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<TeaTypeDTO>> search(String tenantName, String toppingTypeCode,
            String toppingTypeName, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<TeaTypeDTO>> langTuoResult = null;
        try {
            PageInfo<TeaTypePO> pageInfo = accessor.search(tenantName, toppingTypeCode, toppingTypeName,
                    pageNum, pageSize);
            List<TeaTypeDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<TeaTypeDTO> getByCode(String tenantCode, String toppingTypeCode) {
        LangTuoResult<TeaTypeDTO> langTuoResult = null;
        try {
            TeaTypePO toppingTypePO = accessor.selectOneByCode(tenantCode, toppingTypeCode);
            TeaTypeDTO tenantDTO = convert(toppingTypePO);

            langTuoResult = LangTuoResult.success(tenantDTO);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<TeaTypeDTO> getByName(String tenantCode, String toppingTypeName) {
        LangTuoResult<TeaTypeDTO> langTuoResult = null;
        try {
            TeaTypePO toppingTypePO = accessor.selectOneByName(tenantCode, toppingTypeName);
            TeaTypeDTO tenantDTO = convert(toppingTypePO);

            langTuoResult = LangTuoResult.success(tenantDTO);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(TeaTypePutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaTypePO teaTypePO = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            TeaTypePO exist = accessor.selectOneByCode(teaTypePO.getTenantCode(), teaTypePO.getTeaTypeCode());
            if (exist != null) {
                int updated = accessor.update(teaTypePO);
            } else {
                int inserted = accessor.insert(teaTypePO);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String teaTypeCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = accessor.delete(tenantCode, teaTypeCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private TeaTypeDTO convert(TeaTypePO po) {
        if (po == null) {
            return null;
        }

        TeaTypeDTO dto = new TeaTypeDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTeaTypeCode(po.getTeaTypeCode());
        dto.setTeaTypeName(po.getTeaTypeName());
        dto.setState(po.getState());
        dto.setComment(po.getComment());
        po.setExtraInfo(po.getExtraInfo());
        return dto;
    }

    private TeaTypePO convert(TeaTypePutRequest request) {
        if (request == null) {
            return null;
        }

        TeaTypePO po = new TeaTypePO();
        po.setTeaTypeCode(request.getTeaTypeCode());
        po.setTeaTypeName(request.getTeaTypeName());
        po.setState(request.getState());
        po.setTenantCode(request.getTenantCode());
        po.setComment(request.getComment());
        po.setExtraInfo(request.getExtraInfo());
        return po;
    }
}
