package com.langtuo.teamachine.biz.service.impl.drink;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.ToppingTypeDTO;
import com.langtuo.teamachine.api.request.drink.ToppingTypePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drink.ToppingMgtService;
import com.langtuo.teamachine.api.service.drink.ToppingTypeMgtService;
import com.langtuo.teamachine.dao.accessor.drink.ToppingTypeAccessor;
import com.langtuo.teamachine.dao.po.drink.ToppingTypePO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.LangTuoResult.getModel;

@Component
@Slf4j
public class ToppingTypeMgtServiceImpl implements ToppingTypeMgtService {
    @Resource
    private ToppingTypeAccessor accessor;

    @Resource
    private ToppingMgtService toppingMgtService;

    @Override
    public LangTuoResult<List<ToppingTypeDTO>> list(String tenantCode) {
        LangTuoResult<List<ToppingTypeDTO>> langTuoResult = null;
        try {
            List<ToppingTypePO> list = accessor.selectList(tenantCode);
            List<ToppingTypeDTO> dtoList = list.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<ToppingTypeDTO>> search(String tenantName, String toppingTypeCode,
            String toppingTypeName, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<ToppingTypeDTO>> langTuoResult = null;
        try {
            PageInfo<ToppingTypePO> pageInfo = accessor.search(tenantName, toppingTypeCode, toppingTypeName,
                    pageNum, pageSize);
            List<ToppingTypeDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<ToppingTypeDTO> getByCode(String tenantCode, String toppingTypeCode) {
        LangTuoResult<ToppingTypeDTO> langTuoResult = null;
        try {
            ToppingTypePO toppingTypePO = accessor.selectOneByCode(tenantCode, toppingTypeCode);
            ToppingTypeDTO tenantDTO = convert(toppingTypePO);

            langTuoResult = LangTuoResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<ToppingTypeDTO> getByName(String tenantCode, String toppingTypeName) {
        LangTuoResult<ToppingTypeDTO> langTuoResult = null;
        try {
            ToppingTypePO toppingTypePO = accessor.selectOneByName(tenantCode, toppingTypeName);
            ToppingTypeDTO tenantDTO = convert(toppingTypePO);

            langTuoResult = LangTuoResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(ToppingTypePutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        ToppingTypePO toppingTypePO = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            ToppingTypePO exist = accessor.selectOneByCode(toppingTypePO.getTenantCode(),
                    toppingTypePO.getToppingTypeCode());
            if (exist != null) {
                int updated = accessor.update(toppingTypePO);
            } else {
                int inserted = accessor.insert(toppingTypePO);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String toppingTypeCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = accessor.delete(tenantCode, toppingTypeCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private ToppingTypeDTO convert(ToppingTypePO po) {
        if (po == null) {
            return null;
        }

        ToppingTypeDTO dto = new ToppingTypeDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setToppingTypeCode(po.getToppingTypeCode());
        dto.setToppingTypeName(po.getToppingTypeName());
        dto.setComment(po.getComment());
        po.setExtraInfo(po.getExtraInfo());

        int toppingCount = getModel(toppingMgtService.countByToppingTypeCode(po.getTenantCode(), po.getToppingTypeCode()));
        dto.setToppingCount(toppingCount);

        return dto;
    }

    private ToppingTypePO convert(ToppingTypePutRequest request) {
        if (request == null) {
            return null;
        }

        ToppingTypePO po = new ToppingTypePO();
        po.setToppingTypeCode(request.getToppingTypeCode());
        po.setToppingTypeName(request.getToppingTypeName());
        po.setTenantCode(request.getTenantCode());
        po.setComment(request.getComment());
        po.setExtraInfo(po.getExtraInfo());
        return po;
    }
}
