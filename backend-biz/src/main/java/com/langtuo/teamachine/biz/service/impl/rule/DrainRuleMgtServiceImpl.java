package com.langtuo.teamachine.biz.service.impl.rule;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.ToppingDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDispatchDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleToppingDTO;
import com.langtuo.teamachine.api.request.rule.DrainRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.DrainRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drink.ToppingMgtService;
import com.langtuo.teamachine.api.service.rule.DrainRuleMgtService;
import com.langtuo.teamachine.dao.accessor.rule.*;
import com.langtuo.teamachine.dao.po.rule.DrainRuleDispatchPO;
import com.langtuo.teamachine.dao.po.rule.DrainRuleToppingPO;
import com.langtuo.teamachine.dao.po.rule.DrainRulePO;
import com.langtuo.teamachine.mqtt.publish.MqttPublisher4Console;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.LangTuoResult.getModel;

@Component
@Slf4j
public class DrainRuleMgtServiceImpl implements DrainRuleMgtService {
    @Resource
    private DrainRuleAccessor openRuleAccessor;

    @Resource
    private DrainRuleToppingAccessor openRuleToppingAccessor;

    @Resource
    private DrainRuleDispatchAccessor openRuleDispatchAccessor;

    @Resource
    private ToppingMgtService toppingMgtService;

    @Resource
    private MqttPublisher4Console mqttPublisher4Console;

    @Override
    public LangTuoResult<DrainRuleDTO> getByCode(String tenantCode, String openRuleCode) {
        LangTuoResult<DrainRuleDTO> langTuoResult = null;
        try {
            DrainRulePO po = openRuleAccessor.selectOneByCode(tenantCode, openRuleCode);
            DrainRuleDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<DrainRuleDTO> getByName(String tenantCode, String openRuleName) {
        LangTuoResult<DrainRuleDTO> langTuoResult = null;
        try {
            DrainRulePO po = openRuleAccessor.selectOneByName(tenantCode, openRuleName);
            DrainRuleDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<DrainRuleDTO>> list(String tenantCode) {
        LangTuoResult<List<DrainRuleDTO>> langTuoResult = null;
        try {
            List<DrainRulePO> poList = openRuleAccessor.selectList(tenantCode);
            List<DrainRuleDTO> dtoList = convertToDrainRuleDTO(poList);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<DrainRuleDTO>> search(String tenantCode, String openRuleCode,
            String openRuleName, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<DrainRuleDTO>> langTuoResult = null;
        try {
            PageInfo<DrainRulePO> pageInfo = openRuleAccessor.search(tenantCode, openRuleCode,
                    openRuleName, pageNum, pageSize);
            List<DrainRuleDTO> dtoList = convertToDrainRuleDTO(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(DrainRulePutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        DrainRulePO openRulePO = convertToDrainRulePO(request);
        List<DrainRuleToppingPO> openRuleToppingPOList = convertToDrainRuleIncludePO(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            DrainRulePO exist = openRuleAccessor.selectOneByCode(openRulePO.getTenantCode(),
                    openRulePO.getDrainRuleCode());
            if (exist != null) {
                int updated = openRuleAccessor.update(openRulePO);
            } else {
                int inserted = openRuleAccessor.insert(openRulePO);
            }

            int deleted4Topping = openRuleToppingAccessor.delete(request.getTenantCode(),
                    request.getDrainRuleCode());
            if (!CollectionUtils.isEmpty(openRuleToppingPOList)) {
                openRuleToppingPOList.forEach(item -> {
                    int inserted4Topping = openRuleToppingAccessor.insert(item);
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
    public LangTuoResult<Void> delete(String tenantCode, String openRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = openRuleAccessor.delete(tenantCode, openRuleCode);
            int deleted4Topping = openRuleToppingAccessor.delete(tenantCode, openRuleCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> putDispatch(DrainRuleDispatchPutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        List<DrainRuleDispatchPO> poList = convert(request);
        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = openRuleDispatchAccessor.delete(request.getTenantCode(),
                    request.getDrainRuleCode());
            poList.forEach(po -> {
                openRuleDispatchAccessor.insert(po);
            });

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("putDispatch error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }

        // 异步发送消息准备配置信息分发
        mqttPublisher4Console.send4DrainRule(
                request.getTenantCode(), request.getDrainRuleCode());

        return langTuoResult;
    }

    @Override
    public LangTuoResult<DrainRuleDispatchDTO> getDispatchByCode(String tenantCode, String openRuleCode) {
        LangTuoResult<DrainRuleDispatchDTO> langTuoResult = null;
        try {
            DrainRuleDispatchDTO dto = new DrainRuleDispatchDTO();
            dto.setDrainRuleCode(openRuleCode);

            List<DrainRuleDispatchPO> poList = openRuleDispatchAccessor.selectList(tenantCode, openRuleCode);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(po -> po.getShopGroupCode())
                        .collect(Collectors.toList()));
            }

            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getDispatchByDrainRuleCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    private List<DrainRuleDTO> convertToDrainRuleDTO(List<DrainRulePO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<DrainRuleDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private DrainRuleDTO convert(DrainRulePO po) {
        if (po == null) {
            return null;
        }

        DrainRuleDTO dto = new DrainRuleDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTenantCode(po.getTenantCode());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setDrainRuleCode(po.getDrainRuleCode());
        dto.setDrainRuleName(po.getDrainRuleName());
        dto.setDefaultRule(po.getDefaultRule());

        List<DrainRuleToppingPO> poList = openRuleToppingAccessor.selectList(
                po.getTenantCode(), po.getDrainRuleCode());
        if (!CollectionUtils.isEmpty(poList)) {
            dto.setToppingRuleList(convertToDrainRuleToppingDTO(poList));
        }
        return dto;
    }

    private List<DrainRuleToppingDTO> convertToDrainRuleToppingDTO(List<DrainRuleToppingPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<DrainRuleToppingDTO> list = poList.stream()
                .map(po -> {
                    DrainRuleToppingDTO dto = new DrainRuleToppingDTO();
                    dto.setTenantCode(po.getTenantCode());
                    dto.setDrainRuleCode(po.getDrainRuleCode());
                    dto.setToppingCode(po.getToppingCode());
                    dto.setFlushSec(po.getFlushSec());
                    dto.setFlushWeight(po.getFlushWeight());

                    ToppingDTO toppingDTO = getModel(toppingMgtService.getByCode(
                            po.getTenantCode(), po.getToppingCode()));
                    if (toppingDTO != null) {
                        dto.setToppingName(toppingDTO.getToppingName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
        return list;
    }

    private DrainRulePO convertToDrainRulePO(DrainRulePutRequest request) {
        if (request == null) {
            return null;
        }

        DrainRulePO po = new DrainRulePO();
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setDrainRuleCode(request.getDrainRuleCode());
        po.setDrainRuleName(request.getDrainRuleName());
        po.setDefaultRule(request.getDefaultRule());
        return po;
    }

    private List<DrainRuleToppingPO> convertToDrainRuleIncludePO(DrainRulePutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getToppingRuleList())) {
            return null;
        }

        List<DrainRuleToppingPO> list = request.getToppingRuleList().stream()
                .filter(Objects::nonNull)
                .map(openRuleToppingPutRequest -> {
                    DrainRuleToppingPO po = new DrainRuleToppingPO();
                    po.setTenantCode(request.getTenantCode());
                    po.setDrainRuleCode(request.getDrainRuleCode());
                    po.setToppingCode(openRuleToppingPutRequest.getToppingCode());
                    po.setFlushSec(openRuleToppingPutRequest.getFlushSec());
                    po.setFlushWeight(openRuleToppingPutRequest.getFlushWeight());
                    return po;
                }).collect(Collectors.toList());
        return list;
    }

    private List<DrainRuleDispatchPO> convert(DrainRuleDispatchPutRequest request) {
        String tenantCode = request.getTenantCode();
        String openRuleCode = request.getDrainRuleCode();

        return request.getShopGroupCodeList().stream()
                .map(shopGroupCode -> {
                    DrainRuleDispatchPO po = new DrainRuleDispatchPO();
                    po.setTenantCode(tenantCode);
                    po.setDrainRuleCode(openRuleCode);
                    po.setShopGroupCode(shopGroupCode);
                    return po;
                }).collect(Collectors.toList());
    }
}
