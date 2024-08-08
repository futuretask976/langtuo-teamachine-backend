package com.langtuo.teamachine.biz.service.impl.rule;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.CloseRuleDTO;
import com.langtuo.teamachine.api.request.rule.CloseRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.rule.CloseRuleMgtService;
import com.langtuo.teamachine.dao.accessor.rule.CloseRuleAccessor;
import com.langtuo.teamachine.dao.po.rule.CloseRulePO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CloseRuleMgtServiceImpl implements CloseRuleMgtService {
    @Resource
    private CloseRuleAccessor accessor;

    @Override
    public LangTuoResult<CloseRuleDTO> getByCode(String tenantCode, String closeRuleCode) {
        LangTuoResult<CloseRuleDTO> langTuoResult = null;
        try {
            CloseRulePO po = accessor.selectOneByCode(tenantCode, closeRuleCode);
            CloseRuleDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<CloseRuleDTO> getByName(String tenantCode, String closeRuleName) {
        LangTuoResult<CloseRuleDTO> langTuoResult = null;
        try {
            CloseRulePO po = accessor.selectOneByName(tenantCode, closeRuleName);
            CloseRuleDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<CloseRuleDTO>> list(String tenantCode) {
        LangTuoResult<List<CloseRuleDTO>> langTuoResult = null;
        try {
            List<CloseRulePO> poList = accessor.selectList(tenantCode);
            List<CloseRuleDTO> dtoList = convertToCloseRuleDTO(poList);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<CloseRuleDTO>> search(String tenantCode, String closeRuleCode,
            String closeRuleName, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<CloseRuleDTO>> langTuoResult = null;
        try {
            PageInfo<CloseRulePO> pageInfo = accessor.search(tenantCode, closeRuleCode,
                    closeRuleName, pageNum, pageSize);
            List<CloseRuleDTO> dtoList = convertToCloseRuleDTO(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(CloseRulePutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        CloseRulePO openRulePO = convertToCloseRulePO(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            CloseRulePO exist = accessor.selectOneByCode(openRulePO.getTenantCode(),
                    openRulePO.getCloseRuleCode());
            if (exist != null) {
                int updated = accessor.update(openRulePO);
            } else {
                int inserted = accessor.insert(openRulePO);
            }

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String closeRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = accessor.delete(tenantCode, closeRuleCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private List<CloseRuleDTO> convertToCloseRuleDTO(List<CloseRulePO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<CloseRuleDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private CloseRuleDTO convert(CloseRulePO po) {
        if (po == null) {
            return null;
        }

        CloseRuleDTO dto = new CloseRuleDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTenantCode(po.getTenantCode());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setCloseRuleCode(po.getCloseRuleCode());
        dto.setCloseRuleName(po.getCloseRuleName());
        dto.setDefaultRule(po.getDefaultRule());
        dto.setWashSec(po.getWashSec());
        dto.setSoakMin(po.getSoakMin());
        dto.setFlushIntervalMin(po.getFlushIntervalMin());
        dto.setFlushSec(po.getFlushSec());
        return dto;
    }

    private CloseRulePO convertToCloseRulePO(CloseRulePutRequest request) {
        if (request == null) {
            return null;
        }

        CloseRulePO po = new CloseRulePO();
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setCloseRuleCode(request.getCloseRuleCode());
        po.setCloseRuleName(request.getCloseRuleName());
        po.setDefaultRule(request.getDefaultRule());
        po.setWashSec(request.getWashSec());
        po.setSoakMin(request.getSoakMin());
        po.setFlushIntervalMin(request.getFlushIntervalMin());
        po.setFlushSec(request.getFlushSec());
        return po;
    }
}
