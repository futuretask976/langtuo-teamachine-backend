package com.langtuo.teamachine.biz.service.impl.rule;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.WarningRuleDTO;
import com.langtuo.teamachine.api.request.rule.WarningRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.rule.WarningRuleMgtService;
import com.langtuo.teamachine.dao.accessor.rule.WarningRuleAccessor;
import com.langtuo.teamachine.dao.po.rule.WarningRulePO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WarningRuleMgtServiceImpl implements WarningRuleMgtService {
    @Resource
    private WarningRuleAccessor accessor;

    @Override
    public LangTuoResult<WarningRuleDTO> getByCode(String tenantCode, String warningRuleCode) {
        LangTuoResult<WarningRuleDTO> langTuoResult = null;
        try {
            WarningRulePO po = accessor.selectOneByCode(tenantCode, warningRuleCode);
            WarningRuleDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<WarningRuleDTO> getByName(String tenantCode, String warningRuleName) {
        LangTuoResult<WarningRuleDTO> langTuoResult = null;
        try {
            WarningRulePO po = accessor.selectOneByName(tenantCode, warningRuleName);
            WarningRuleDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<WarningRuleDTO>> list(String tenantCode) {
        LangTuoResult<List<WarningRuleDTO>> langTuoResult = null;
        try {
            List<WarningRulePO> poList = accessor.selectList(tenantCode);
            List<WarningRuleDTO> dtoList = convert(poList);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<WarningRuleDTO>> search(String tenantCode, String warningRuleCode,
            String warningRuleName, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<WarningRuleDTO>> langTuoResult = null;
        try {
            PageInfo<WarningRulePO> pageInfo = accessor.search(tenantCode, warningRuleCode,
                    warningRuleName, pageNum, pageSize);
            List<WarningRuleDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(WarningRulePutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        WarningRulePO warningRulePO = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            WarningRulePO exist = accessor.selectOneByCode(warningRulePO.getTenantCode(),
                    warningRulePO.getWarningRuleCode());
            if (exist != null) {
                int updated = accessor.update(warningRulePO);
            } else {
                int inserted = accessor.insert(warningRulePO);
            }

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String warningRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = accessor.delete(tenantCode, warningRuleCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private List<WarningRuleDTO> convert(List<WarningRulePO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<WarningRuleDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private WarningRuleDTO convert(WarningRulePO po) {
        if (po == null) {
            return null;
        }

        WarningRuleDTO dto = new WarningRuleDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTenantCode(po.getTenantCode());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setWarningRuleCode(po.getWarningRuleCode());
        dto.setWarningRuleName(po.getWarningRuleName());
        dto.setWarningContent(po.getWarningContent());
        dto.setWarningType(po.getWarningType());
        dto.setThreshold(po.getThreshold());
        dto.setThresholdMode(po.getThresholdMode());
        dto.setComment(po.getComment());
        return dto;
    }

    private WarningRulePO convert(WarningRulePutRequest request) {
        if (request == null) {
            return null;
        }

        WarningRulePO po = new WarningRulePO();
        po.setTenantCode(request.getTenantCode());
        po.setComment(request.getComment());
        po.setExtraInfo(request.getExtraInfo());
        po.setWarningRuleCode(request.getWarningRuleCode());
        po.setWarningRuleName(request.getWarningRuleName());
        po.setWarningContent(request.getWarningContent());
        po.setWarningType(request.getWarningType());
        po.setThreshold(request.getThreshold());
        po.setThresholdMode(request.getThresholdMode());
        po.setComment(request.getComment());
        return po;
    }
}
