package com.langtuo.teamachine.biz.service.impl.rule;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.CleanRuleDTO;
import com.langtuo.teamachine.api.model.rule.CleanRuleDispatchDTO;
import com.langtuo.teamachine.api.model.rule.CleanRuleStepDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.request.rule.CleanRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.CleanRulePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.rule.CleanRuleMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.dao.accessor.rule.CleanRuleAccessor;
import com.langtuo.teamachine.dao.accessor.rule.CleanRuleDispatchAccessor;
import com.langtuo.teamachine.dao.accessor.rule.CleanRuleExceptAccessor;
import com.langtuo.teamachine.dao.accessor.rule.CleanRuleStepAccessor;
import com.langtuo.teamachine.dao.po.rule.CleanRuleDispatchPO;
import com.langtuo.teamachine.dao.po.rule.CleanRuleExceptPO;
import com.langtuo.teamachine.dao.po.rule.CleanRulePO;
import com.langtuo.teamachine.dao.po.rule.CleanRuleStepPO;
import com.langtuo.teamachine.mqtt.publish.MqttPublisher4Console;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.TeaMachineResult.getModel;

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
    private ShopMgtService shopMgtService;

    @Resource
    private MqttPublisher4Console mqttPublisher4Console;

    @Override
    public TeaMachineResult<CleanRuleDTO> getByCode(String tenantCode, String cleanRuleCode) {
        TeaMachineResult<CleanRuleDTO> teaMachineResult;
        try {
            CleanRulePO po = cleanRuleAccessor.selectOneByCode(tenantCode, cleanRuleCode);
            CleanRuleDTO dto = convert(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<CleanRuleDTO> getByName(String tenantCode, String cleanRuleName) {
        TeaMachineResult<CleanRuleDTO> teaMachineResult;
        try {
            CleanRulePO po = cleanRuleAccessor.selectOneByName(tenantCode, cleanRuleName);
            CleanRuleDTO dto = convert(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<CleanRuleDTO>> list(String tenantCode) {
        TeaMachineResult<List<CleanRuleDTO>> teaMachineResult;
        try {
            List<CleanRulePO> cleanRulePOList = cleanRuleAccessor.selectList(tenantCode);
            List<CleanRuleDTO> cleanRuleDTOList = convertToCleanRuleDTO(cleanRulePOList);
            teaMachineResult = TeaMachineResult.success(cleanRuleDTOList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<CleanRuleDTO>> listByShopCode(String tenantCode, String shopCode) {
        TeaMachineResult<List<CleanRuleDTO>> teaMachineResult;
        try {
            ShopDTO shopDTO = getModel(shopMgtService.getByCode(tenantCode, shopCode));
            if (shopDTO == null) {
                teaMachineResult = TeaMachineResult.success();
            }

            List<CleanRuleDispatchPO> cleanRuleDispatchPOList = cleanRuleDispatchAccessor.selectListByShopGroupCode(
                    tenantCode, shopDTO.getShopGroupCode());
            if (CollectionUtils.isEmpty(cleanRuleDispatchPOList)) {
                teaMachineResult = TeaMachineResult.success();
            }

            List<String> cleanRuleCodeList = cleanRuleDispatchPOList.stream()
                    .map(CleanRuleDispatchPO::getCleanRuleCode)
                    .collect(Collectors.toList());
            List<CleanRulePO> cleanRulePOList = cleanRuleAccessor.selectListByCleanRuleCode(tenantCode,
                    cleanRuleCodeList);
            List<CleanRuleDTO> cleanRuleDTOList = convertToCleanRuleDTO(cleanRulePOList);
            teaMachineResult = TeaMachineResult.success(cleanRuleDTOList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<CleanRuleDTO>> search(String tenantCode, String cleanRuleCode, String cleanRuleName,
            int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<CleanRuleDTO>> teaMachineResult;
        try {
            PageInfo<CleanRulePO> pageInfo = cleanRuleAccessor.search(tenantCode, cleanRuleCode, cleanRuleName,
                    pageNum, pageSize);
            List<CleanRuleDTO> dtoList = convertToCleanRuleDTO(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(CleanRulePutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        CleanRulePO cleanRulePO = convertToCleanRulePO(request);
        List<CleanRuleStepPO> cleanRuleStepPOList = convertToCleanRuleStepPO(request);
        List<CleanRuleExceptPO> cleanRuleExceptPOList = convertToCleanRuleExceptPO(request);

        TeaMachineResult<Void> teaMachineResult;
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

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String cleanRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = cleanRuleAccessor.delete(tenantCode, cleanRuleCode);
            int deleted4Step = cleanRuleStepAccessor.delete(tenantCode, cleanRuleCode);
            int deleted4Except = cleanRuleExceptAccessor.delete(tenantCode, cleanRuleCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> putDispatch(CleanRuleDispatchPutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        List<CleanRuleDispatchPO> poList = convert(request);
        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = cleanRuleDispatchAccessor.delete(request.getTenantCode(),
                    request.getCleanRuleCode());
            poList.forEach(po -> {
                cleanRuleDispatchAccessor.insert(po);
            });

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("putDispatch error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }

        // 异步发送消息准备配置信息分发
        mqttPublisher4Console.send4CleanRule(
                request.getTenantCode(), request.getCleanRuleCode());

        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<CleanRuleDispatchDTO> getDispatchByCleanRuleCode(String tenantCode, String cleanRuleCode) {
        TeaMachineResult<CleanRuleDispatchDTO> teaMachineResult;
        try {
            CleanRuleDispatchDTO dto = new CleanRuleDispatchDTO();
            dto.setCleanRuleCode(cleanRuleCode);

            List<CleanRuleDispatchPO> poList = cleanRuleDispatchAccessor.selectList(tenantCode, cleanRuleCode);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(po -> po.getShopGroupCode())
                        .collect(Collectors.toList()));
            }

            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getDispatchByCleanRuleCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
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
