package com.langtuo.teamachine.biz.service.impl.drink;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.AccuracyTplDTO;
import com.langtuo.teamachine.api.request.drink.AccuracyTplPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drink.AccuracyTplMgtService;
import com.langtuo.teamachine.dao.accessor.drink.AccuracyTplAccessor;
import com.langtuo.teamachine.dao.po.drink.AccuracyTplPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AccuracyTplMgtServiceImpl implements AccuracyTplMgtService {
    @Resource
    private AccuracyTplAccessor accessor;

    @Override
    public LangTuoResult<List<AccuracyTplDTO>> list(String tenantCode) {
        LangTuoResult<List<AccuracyTplDTO>> langTuoResult = null;
        try {
            List<AccuracyTplPO> list = accessor.selectList(tenantCode);
            List<AccuracyTplDTO> dtoList = list.stream()
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
    public LangTuoResult<PageDTO<AccuracyTplDTO>> search(String tenantName, String templateCode, String templateName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<AccuracyTplDTO>> langTuoResult = null;
        try {
            PageInfo<AccuracyTplPO> pageInfo = accessor.search(tenantName, templateCode, templateName,
                    pageNum, pageSize);
            List<AccuracyTplDTO> dtoList = pageInfo.getList().stream()
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
    public LangTuoResult<AccuracyTplDTO> getByCode(String tenantCode, String specCode) {
        LangTuoResult<AccuracyTplDTO> langTuoResult = null;
        try {
            AccuracyTplPO po = accessor.selectOneByCode(tenantCode, specCode);
            AccuracyTplDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<AccuracyTplDTO> getByName(String tenantCode, String specName) {
        LangTuoResult<AccuracyTplDTO> langTuoResult = null;
        try {
            AccuracyTplPO po = accessor.selectOneByName(tenantCode, specName);
            AccuracyTplDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(AccuracyTplPutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }
        AccuracyTplPO po = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            AccuracyTplPO exist = accessor.selectOneByCode(po.getTenantCode(), po.getTemplateCode());
            if (exist != null) {
                int updated = accessor.update(po);
            } else {
                int inserted = accessor.insert(po);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String templateCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = accessor.delete(tenantCode, templateCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private AccuracyTplDTO convert(AccuracyTplPO po) {
        if (po == null) {
            return null;
        }

        AccuracyTplDTO dto = new AccuracyTplDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setTemplateCode(po.getTemplateCode());
        dto.setTemplateName(po.getTemplateName());
        dto.setOverMode(po.getOverMode());
        dto.setOverAmount(po.getOverAmount());
        dto.setUnderMode(po.getUnderMode());
        dto.setUnderAmount(po.getUnderAmount());
        dto.setToppingCode(po.getToppingCode());
        dto.setComment(po.getComment());
        return dto;
    }

    private AccuracyTplPO convert(AccuracyTplPutRequest request) {
        if (request == null) {
            return null;
        }

        AccuracyTplPO po = new AccuracyTplPO();
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setTemplateCode(request.getTemplateCode());
        po.setTemplateName(request.getTemplateName());
        po.setOverMode(request.getOverUnit());
        po.setOverAmount(request.getOverAmount());
        po.setUnderMode(request.getUnderUnit());
        po.setUnderAmount(request.getUnderAmount());
        po.setToppingCode(request.getToppingCode());
        po.setComment(request.getComment());
        return po;
    }
}
