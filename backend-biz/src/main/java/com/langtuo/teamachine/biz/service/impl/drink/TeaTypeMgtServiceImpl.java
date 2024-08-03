package com.langtuo.teamachine.biz.service.impl.drink;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.TeaTypeDTO;
import com.langtuo.teamachine.api.request.drink.TeaTypePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drink.TeaTypeMgtService;
import com.langtuo.teamachine.dao.accessor.drink.TeaTypeAccessor;
import com.langtuo.teamachine.dao.po.drink.TeaTypePO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TeaTypeMgtServiceImpl implements TeaTypeMgtService {
    @Resource
    private TeaTypeAccessor accessor;

    @Override
    public LangTuoResult<List<TeaTypeDTO>> list(String tenantCode) {
        LangTuoResult<List<TeaTypeDTO>> langTuoResult = null;
        try {
            List<TeaTypePO> list = accessor.selectList(tenantCode);
            List<TeaTypeDTO> dtoList = convert(list);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
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
            List<TeaTypeDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
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
            log.error("getByCode error: " + e.getMessage(), e);
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
            log.error("getByName error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(TeaTypePutRequest request) {
        if (request == null || !request.isValid()) {
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
            log.error("put error: " + e.getMessage(), e);
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
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private List<TeaTypeDTO> convert(List<TeaTypePO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<TeaTypeDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
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
