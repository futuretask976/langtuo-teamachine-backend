package com.langtuo.teamachine.biz.service.impl.rule;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.CleanRuleDTO;
import com.langtuo.teamachine.api.model.rule.CleanRuleDispatchDTO;
import com.langtuo.teamachine.api.model.rule.CleanRuleStepDTO;
import com.langtuo.teamachine.api.request.rule.CleanRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.CleanRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.rule.CleanRuleMgtService;
import com.langtuo.teamachine.dao.accessor.rule.CleanRuleAccessor;
import com.langtuo.teamachine.dao.accessor.rule.CleanRuleDispatchAccessor;
import com.langtuo.teamachine.dao.accessor.rule.CleanRuleExceptAccessor;
import com.langtuo.teamachine.dao.accessor.rule.CleanRuleStepAccessor;
import com.langtuo.teamachine.dao.po.rule.CleanRuleDispatchPO;
import com.langtuo.teamachine.dao.po.rule.CleanRuleExceptPO;
import com.langtuo.teamachine.dao.po.rule.CleanRulePO;
import com.langtuo.teamachine.dao.po.rule.CleanRuleStepPO;
import com.langtuo.teamachine.mqtt.MqttService;
import com.langtuo.teamachine.mqtt.config.MqttConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CleanRuleMgtServiceImpl implements CleanRuleMgtService {
    @Resource
    private CleanRuleAccessor cleanRuleAccessor;

    @Resource
    private CleanRuleStepAccessor cleanRuleStepAccessor;

    @Resource
    private CleanRuleDispatchAccessor cleanRuleDispatchAccessor;

    @Resource
    private CleanRuleExceptAccessor cleanRuleExceptAccessor;

    @Resource
    private MqttService mqttService;

    @Override
    public LangTuoResult<CleanRuleDTO> getByCode(String tenantCode, String cleanRuleCode) {
        LangTuoResult<CleanRuleDTO> langTuoResult = null;
        try {
            CleanRulePO po = cleanRuleAccessor.selectOneByCode(tenantCode, cleanRuleCode);
            CleanRuleDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<CleanRuleDTO> getByName(String tenantCode, String cleanRuleName) {
        LangTuoResult<CleanRuleDTO> langTuoResult = null;
        try {
            CleanRulePO po = cleanRuleAccessor.selectOneByName(tenantCode, cleanRuleName);
            CleanRuleDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<CleanRuleDTO>> list(String tenantCode) {
        LangTuoResult<List<CleanRuleDTO>> langTuoResult = null;
        try {
            List<CleanRulePO> cleanRulePOList = cleanRuleAccessor.selectList(tenantCode);
            List<CleanRuleDTO> teaDTOList = convertToCleanRuleDTO(cleanRulePOList);
            langTuoResult = LangTuoResult.success(teaDTOList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<CleanRuleDTO>> search(String tenantCode, String cleanRuleCode, String cleanRuleName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<CleanRuleDTO>> langTuoResult = null;
        try {
            PageInfo<CleanRulePO> pageInfo = cleanRuleAccessor.search(tenantCode, cleanRuleCode, cleanRuleName,
                    pageNum, pageSize);
            List<CleanRuleDTO> dtoList = convertToCleanRuleDTO(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(CleanRulePutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        CleanRulePO cleanRulePO = convertToCleanRulePO(request);
        List<CleanRuleStepPO> cleanRuleStepPOList = convertToCleanRuleStepPO(request);
        List<CleanRuleExceptPO> cleanRuleExceptPOList = convertToCleanRuleExceptPO(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            CleanRulePO exist = cleanRuleAccessor.selectOneByCode(cleanRulePO.getTenantCode(),
                    cleanRulePO.getCleanRuleCode());
            if (exist != null) {
                int updated = cleanRuleAccessor.update(cleanRulePO);
            } else {
                int inserted = cleanRuleAccessor.insert(cleanRulePO);
            }

            int deleted4Step = cleanRuleStepAccessor.delete(request.getTenantCode(), request.getCleanRuleCode());
            if (!CollectionUtils.isEmpty(cleanRuleStepPOList)) {
                cleanRuleStepPOList.forEach(item -> {
                    int inserted4Step = cleanRuleStepAccessor.insert(item);
                });
            }

            int deleted4Except = cleanRuleExceptAccessor.delete(request.getTenantCode(), request.getCleanRuleCode());
            if (!CollectionUtils.isEmpty(cleanRuleExceptPOList)) {
                cleanRuleExceptPOList.forEach(item -> {
                    int inserted4Except = cleanRuleExceptAccessor.insert(item);
                });
            }

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String cleanRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = cleanRuleAccessor.delete(tenantCode, cleanRuleCode);
            int deleted4Step = cleanRuleStepAccessor.delete(tenantCode, cleanRuleCode);
            int deleted4Except = cleanRuleExceptAccessor.delete(tenantCode, cleanRuleCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> putDispatch(CleanRuleDispatchPutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        List<CleanRuleDispatchPO> poList = convert(request);
        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = cleanRuleDispatchAccessor.delete(request.getTenantCode(),
                    request.getCleanRuleCode());
            poList.forEach(po -> {
                cleanRuleDispatchAccessor.insert(po);
            });

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("putDispatch error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }

        // 发送一步消息推送机器
        JSONObject payloadJSON = new JSONObject();
        payloadJSON.put("tenantCode", request.getTenantCode());
        payloadJSON.put("cleanRuleCode", request.getCleanRuleCode());
        mqttService.sendConsoleMsgByTopic(MqttConfig.CONSOLE_TOPIC_PREPARE_DISPATCH_CLEAN_RULE, payloadJSON.toJSONString());

        return langTuoResult;
    }

    @Override
    public LangTuoResult<CleanRuleDispatchDTO> getDispatchByCode(String tenantCode, String cleanRuleCode) {
        LangTuoResult<CleanRuleDispatchDTO> langTuoResult = null;
        try {
            CleanRuleDispatchDTO dto = new CleanRuleDispatchDTO();
            dto.setCleanRuleCode(cleanRuleCode);

            List<CleanRuleDispatchPO> poList = cleanRuleDispatchAccessor.selectList(tenantCode, cleanRuleCode);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(po -> po.getShopGroupCode())
                        .collect(Collectors.toList()));
            }

            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getDispatchByCleanRuleCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    private List<CleanRuleDTO> convertToCleanRuleDTO(List<CleanRulePO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<CleanRuleDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private CleanRuleDTO convert(CleanRulePO po) {
        if (po == null) {
            return null;
        }

        CleanRuleDTO dto = new CleanRuleDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setCleanRuleCode(po.getCleanRuleCode());
        dto.setCleanRuleName(po.getCleanRuleName());
        dto.setPermitBatch(po.getPermitBatch());
        dto.setPermitRemind(po.getPermitRemind());

        List<CleanRuleStepPO> cleanRuleStepPOList = cleanRuleStepAccessor.selectList(
                po.getTenantCode(), dto.getCleanRuleCode());
        if (!CollectionUtils.isEmpty(cleanRuleStepPOList)) {
            dto.setCleanRuleStepList(convert(cleanRuleStepPOList));
        }
        List<CleanRuleExceptPO> cleanRuleExceptPOList = cleanRuleExceptAccessor.selectList(
                po.getTenantCode(), dto.getCleanRuleCode());
        if (!CollectionUtils.isEmpty(cleanRuleExceptPOList)) {
            dto.setExceptToppingCodeList(cleanRuleExceptPOList.stream()
                    .map(cleanRuleExceptPO -> cleanRuleExceptPO.getExceptToppingCode())
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private List<CleanRuleStepDTO> convert(List<CleanRuleStepPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<CleanRuleStepDTO> list = poList.stream()
                .map(po -> {
                    CleanRuleStepDTO dto = new CleanRuleStepDTO();
                    dto.setTenantCode(po.getTenantCode());
                    dto.setCleanRuleCode(po.getCleanRuleCode());
                    dto.setCleanContent(po.getCleanContent());
                    dto.setRemindContent(po.getRemindContent());
                    dto.setRemindTitle(po.getRemindTitle());
                    dto.setSoakMin(po.getSoakMin());
                    dto.setStepIndex(po.getStepIndex());
                    dto.setFlushIntervalMin(po.getFlushIntervalMin());
                    dto.setWashSec(po.getWashSec());
                    dto.setFlushSec(po.getFlushSec());
                    dto.setStepIndex(po.getStepIndex());
                    dto.setNeedConfirm(po.getNeedConfirm());
                    dto.setCleanAgentType(po.getCleanAgentType());
                    return dto;
                })
                .collect(Collectors.toList());
        return list;
    }

    private CleanRulePO convertToCleanRulePO(CleanRulePutRequest request) {
        if (request == null) {
            return null;
        }

        CleanRulePO po = new CleanRulePO();
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setCleanRuleCode(request.getCleanRuleCode());
        po.setCleanRuleName(request.getCleanRuleName());
        po.setPermitBatch(request.getPermitBatch());
        po.setPermitRemind(request.getPermitRemind());
        return po;
    }

    private List<CleanRuleStepPO> convertToCleanRuleStepPO(CleanRulePutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getCleanRuleStepList())) {
            return null;
        }

        List<CleanRuleStepPO> list = request.getCleanRuleStepList().stream()
                .filter(Objects::nonNull)
                .map(cleanRuleStepPutRequest -> {
                    CleanRuleStepPO po = new CleanRuleStepPO();
                    po.setTenantCode(request.getTenantCode());
                    po.setCleanRuleCode(request.getCleanRuleCode());
                    po.setCleanContent(cleanRuleStepPutRequest.getCleanContent());
                    po.setRemindContent(cleanRuleStepPutRequest.getRemindContent());
                    po.setRemindTitle(cleanRuleStepPutRequest.getRemindTitle());
                    po.setSoakMin(cleanRuleStepPutRequest.getSoakMin());
                    po.setStepIndex(cleanRuleStepPutRequest.getStepIndex());
                    po.setFlushIntervalMin(cleanRuleStepPutRequest.getFlushIntervalMin());
                    po.setWashSec(cleanRuleStepPutRequest.getWashSec());
                    po.setFlushSec(cleanRuleStepPutRequest.getFlushSec());
                    po.setStepIndex(cleanRuleStepPutRequest.getStepIndex());
                    po.setNeedConfirm(cleanRuleStepPutRequest.getNeedConfirm());
                    po.setCleanAgentType(cleanRuleStepPutRequest.getCleanAgentType());
                    return po;
                }).collect(Collectors.toList());
        return list;
    }

    private List<CleanRuleDispatchPO> convert(CleanRuleDispatchPutRequest request) {
        String tenantCode = request.getTenantCode();
        String cleanRuleCode = request.getCleanRuleCode();

        return request.getShopGroupCodeList().stream()
                .map(shopGroupCode -> {
                    CleanRuleDispatchPO po = new CleanRuleDispatchPO();
                    po.setTenantCode(tenantCode);
                    po.setCleanRuleCode(cleanRuleCode);
                    po.setShopGroupCode(shopGroupCode);
                    return po;
                }).collect(Collectors.toList());
    }

    private List<CleanRuleExceptPO> convertToCleanRuleExceptPO(CleanRulePutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getExceptToppingCodeList())) {
            return null;
        }

        List<CleanRuleExceptPO> poList = request.getExceptToppingCodeList().stream()
                .map(exceptToppingCode -> {
                    CleanRuleExceptPO po = new CleanRuleExceptPO();
                    po.setExceptToppingCode(exceptToppingCode);
                    po.setTenantCode(request.getTenantCode());
                    po.setCleanRuleCode(request.getCleanRuleCode());
                    return po;
                }).collect(Collectors.toList());
        return poList;
    }
}
