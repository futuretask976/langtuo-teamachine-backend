package com.langtuo.teamachine.biz.service.impl.rule;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.WarningRuleDispatchDTO;
import com.langtuo.teamachine.api.model.rule.WarningRuleDTO;
import com.langtuo.teamachine.api.request.rule.WarningRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.WarningRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.rule.WarningRuleMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.dao.accessor.rule.WarningRuleAccessor;
import com.langtuo.teamachine.dao.accessor.rule.WarningRuleDispatchAccessor;
import com.langtuo.teamachine.dao.po.rule.WarningRuleDispatchPO;
import com.langtuo.teamachine.dao.po.rule.WarningRulePO;
import com.langtuo.teamachine.mqtt.publish.MqttPublisher4Console;
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
    private WarningRuleAccessor warningRuleAccessor;

    @Resource
    private WarningRuleDispatchAccessor warningRuleDispatchAccessor;

    @Resource
    private MqttPublisher4Console mqttPublisher4Console;

    @Override
    public LangTuoResult<WarningRuleDTO> getByCode(String tenantCode, String warningRuleCode) {
        LangTuoResult<WarningRuleDTO> langTuoResult = null;
        try {
            WarningRulePO po = warningRuleAccessor.selectOneByCode(tenantCode, warningRuleCode);
            WarningRuleDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<WarningRuleDTO> getByName(String tenantCode, String warningRuleName) {
        LangTuoResult<WarningRuleDTO> langTuoResult = null;
        try {
            WarningRulePO po = warningRuleAccessor.selectOneByName(tenantCode, warningRuleName);
            WarningRuleDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<WarningRuleDTO>> list(String tenantCode) {
        LangTuoResult<List<WarningRuleDTO>> langTuoResult = null;
        try {
            List<WarningRulePO> poList = warningRuleAccessor.selectList(tenantCode);
            List<WarningRuleDTO> dtoList = convert(poList);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<WarningRuleDTO>> search(String tenantCode, String warningRuleCode,
            String warningRuleName, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        LangTuoResult<PageDTO<WarningRuleDTO>> langTuoResult = null;
        try {
            PageInfo<WarningRulePO> pageInfo = warningRuleAccessor.search(tenantCode, warningRuleCode,
                    warningRuleName, pageNum, pageSize);
            List<WarningRuleDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
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
            WarningRulePO exist = warningRuleAccessor.selectOneByCode(warningRulePO.getTenantCode(),
                    warningRulePO.getWarningRuleCode());
            if (exist != null) {
                int updated = warningRuleAccessor.update(warningRulePO);
            } else {
                int inserted = warningRuleAccessor.insert(warningRulePO);
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
            int deleted = warningRuleAccessor.delete(tenantCode, warningRuleCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> putDispatch(WarningRuleDispatchPutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        List<WarningRuleDispatchPO> poList = convert(request);
        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = warningRuleDispatchAccessor.delete(request.getTenantCode(),
                    request.getWarningRuleCode());
            poList.forEach(po -> {
                warningRuleDispatchAccessor.insert(po);
            });

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("putDispatch error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }

        // 异步发送消息准备配置信息分发
        mqttPublisher4Console.send4WarningRule(
                request.getTenantCode(), request.getWarningRuleCode());

        return langTuoResult;
    }

    @Override
    public LangTuoResult<WarningRuleDispatchDTO> getDispatchByCode(String tenantCode, String warningRuleCode) {
        LangTuoResult<WarningRuleDispatchDTO> langTuoResult = null;
        try {
            WarningRuleDispatchDTO dto = new WarningRuleDispatchDTO();
            dto.setWarningRuleCode(warningRuleCode);

            List<WarningRuleDispatchPO> poList = warningRuleDispatchAccessor.selectList(tenantCode, warningRuleCode);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(po -> po.getShopGroupCode())
                        .collect(Collectors.toList()));
            }

            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getDispatchByWarningRuleCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
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

    private List<WarningRuleDispatchPO> convert(WarningRuleDispatchPutRequest request) {
        String tenantCode = request.getTenantCode();
        String warningRuleCode = request.getWarningRuleCode();

        return request.getShopGroupCodeList().stream()
                .map(shopGroupCode -> {
                    WarningRuleDispatchPO po = new WarningRuleDispatchPO();
                    po.setTenantCode(tenantCode);
                    po.setWarningRuleCode(warningRuleCode);
                    po.setShopGroupCode(shopGroupCode);
                    return po;
                }).collect(Collectors.toList());
    }
}
