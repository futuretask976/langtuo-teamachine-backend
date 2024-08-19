package com.langtuo.teamachine.biz.service.impl.rule;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.ToppingDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDispatchDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleToppingDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.request.rule.DrainRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.DrainRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drink.ToppingMgtService;
import com.langtuo.teamachine.api.service.rule.DrainRuleMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.dao.accessor.rule.*;
import com.langtuo.teamachine.dao.po.rule.*;
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
    private DrainRuleAccessor drainRuleAccessor;

    @Resource
    private DrainRuleToppingAccessor drainRuleToppingAccessor;

    @Resource
    private DrainRuleDispatchAccessor drainRuleDispatchAccessor;

    @Resource
    private ToppingMgtService toppingMgtService;

    @Resource
    private ShopMgtService shopMgtService;

    @Resource
    private MqttPublisher4Console mqttPublisher4Console;

    @Override
    public LangTuoResult<DrainRuleDTO> getByCode(String tenantCode, String openRuleCode) {
        LangTuoResult<DrainRuleDTO> langTuoResult;
        try {
            DrainRulePO po = drainRuleAccessor.selectOneByCode(tenantCode, openRuleCode);
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
        LangTuoResult<DrainRuleDTO> langTuoResult;
        try {
            DrainRulePO po = drainRuleAccessor.selectOneByName(tenantCode, openRuleName);
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
        LangTuoResult<List<DrainRuleDTO>> langTuoResult;
        try {
            List<DrainRulePO> poList = drainRuleAccessor.selectList(tenantCode);
            List<DrainRuleDTO> dtoList = convertToDrainRuleDTO(poList);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<DrainRuleDTO>> listByShopCode(String tenantCode, String shopCode) {
        LangTuoResult<List<DrainRuleDTO>> langTuoResult;
        try {
            ShopDTO shopDTO = getModel(shopMgtService.getByCode(tenantCode, shopCode));
            if (shopDTO == null) {
                langTuoResult = LangTuoResult.success();
            }

            List<DrainRuleDispatchPO> drainRuleDispatchPOList = drainRuleDispatchAccessor.selectListByShopGroupCode(
                    tenantCode, shopDTO.getShopGroupCode());
            if (CollectionUtils.isEmpty(drainRuleDispatchPOList)) {
                langTuoResult = LangTuoResult.success();
            }

            List<String> drainRuleCodeList = drainRuleDispatchPOList.stream()
                    .map(DrainRuleDispatchPO::getDrainRuleCode)
                    .collect(Collectors.toList());
            List<DrainRulePO> cleanRulePOList = drainRuleAccessor.selectListByDrainRuleCode(tenantCode,
                    drainRuleCodeList);
            List<DrainRuleDTO> drainRuleDTOList = convertToDrainRuleDTO(cleanRulePOList);
            langTuoResult = LangTuoResult.success(drainRuleDTOList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<DrainRuleDTO>> search(String tenantCode, String openRuleCode,
            String openRuleName, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        LangTuoResult<PageDTO<DrainRuleDTO>> langTuoResult;
        try {
            PageInfo<DrainRulePO> pageInfo = drainRuleAccessor.search(tenantCode, openRuleCode,
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

        LangTuoResult<Void> langTuoResult;
        try {
            DrainRulePO exist = drainRuleAccessor.selectOneByCode(openRulePO.getTenantCode(),
                    openRulePO.getDrainRuleCode());
            if (exist != null) {
                int updated = drainRuleAccessor.update(openRulePO);
            } else {
                int inserted = drainRuleAccessor.insert(openRulePO);
            }

            int deleted4Topping = drainRuleToppingAccessor.delete(request.getTenantCode(),
                    request.getDrainRuleCode());
            if (!CollectionUtils.isEmpty(openRuleToppingPOList)) {
                openRuleToppingPOList.forEach(item -> {
                    int inserted4Topping = drainRuleToppingAccessor.insert(item);
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

        LangTuoResult<Void> langTuoResult;
        try {
            int deleted = drainRuleAccessor.delete(tenantCode, openRuleCode);
            int deleted4Topping = drainRuleToppingAccessor.delete(tenantCode, openRuleCode);
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
        LangTuoResult<Void> langTuoResult;
        try {
            int deleted = drainRuleDispatchAccessor.delete(request.getTenantCode(),
                    request.getDrainRuleCode());
            poList.forEach(po -> {
                drainRuleDispatchAccessor.insert(po);
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
        LangTuoResult<DrainRuleDispatchDTO> langTuoResult;
        try {
            DrainRuleDispatchDTO dto = new DrainRuleDispatchDTO();
            dto.setDrainRuleCode(openRuleCode);

            List<DrainRuleDispatchPO> poList = drainRuleDispatchAccessor.selectList(tenantCode, openRuleCode);
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

        List<DrainRuleToppingPO> poList = drainRuleToppingAccessor.selectList(
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
