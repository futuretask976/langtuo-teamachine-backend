package com.langtuo.teamachine.biz.service.rule;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.biz.aync.AsyncDispatcher;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDispatchDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleToppingDTO;
import com.langtuo.teamachine.api.request.rule.DrainRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.DrainRulePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.rule.DrainRuleMgtService;
import com.langtuo.teamachine.dao.accessor.drink.ToppingAccessor;
import com.langtuo.teamachine.dao.accessor.rule.*;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.po.drink.ToppingPO;
import com.langtuo.teamachine.dao.po.rule.*;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private ToppingAccessor toppingAccessor;

    @Resource
    private ShopAccessor shopAccessor;

    @Resource
    private AsyncDispatcher asyncDispatcher;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<DrainRuleDTO> getByCode(String tenantCode, String drainRuleCode) {
        TeaMachineResult<DrainRuleDTO> teaMachineResult;
        try {
            DrainRulePO po = drainRuleAccessor.selectOneByDrainRuleCode(tenantCode, drainRuleCode);
            DrainRuleDTO dto = convert(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<DrainRuleDTO> getByName(String tenantCode, String openRuleName) {
        TeaMachineResult<DrainRuleDTO> teaMachineResult;
        try {
            DrainRulePO po = drainRuleAccessor.selectOneByDrainRuleName(tenantCode, openRuleName);
            DrainRuleDTO dto = convert(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<DrainRuleDTO>> list(String tenantCode) {
        TeaMachineResult<List<DrainRuleDTO>> teaMachineResult;
        try {
            List<DrainRulePO> poList = drainRuleAccessor.selectList(tenantCode);
            List<DrainRuleDTO> dtoList = convertToDrainRuleDTO(poList);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<DrainRuleDTO>> listByShopCode(String tenantCode, String shopCode) {
        TeaMachineResult<List<DrainRuleDTO>> teaMachineResult;
        try {
            ShopPO shopPO = shopAccessor.selectOneByShopCode(tenantCode, shopCode);
            if (shopPO == null) {
                teaMachineResult = TeaMachineResult.success();
            }

            List<DrainRuleDispatchPO> drainRuleDispatchPOList = drainRuleDispatchAccessor.selectListByShopGroupCode(
                    tenantCode, shopPO.getShopGroupCode());
            if (CollectionUtils.isEmpty(drainRuleDispatchPOList)) {
                teaMachineResult = TeaMachineResult.success();
            }

            List<String> drainRuleCodeList = drainRuleDispatchPOList.stream()
                    .map(DrainRuleDispatchPO::getDrainRuleCode)
                    .collect(Collectors.toList());
            List<DrainRulePO> cleanRulePOList = drainRuleAccessor.selectListByDrainRuleCode(tenantCode,
                    drainRuleCodeList);
            List<DrainRuleDTO> drainRuleDTOList = convertToDrainRuleDTO(cleanRulePOList);
            teaMachineResult = TeaMachineResult.success(drainRuleDTOList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<DrainRuleDTO>> search(String tenantCode, String openRuleCode,
            String openRuleName, int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<DrainRuleDTO>> teaMachineResult;
        try {
            PageInfo<DrainRulePO> pageInfo = drainRuleAccessor.search(tenantCode, openRuleCode,
                    openRuleName, pageNum, pageSize);
            List<DrainRuleDTO> dtoList = convertToDrainRuleDTO(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(DrainRulePutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        DrainRulePO openRulePO = convertToDrainRulePO(request);
        List<DrainRuleToppingPO> openRuleToppingPOList = convertToDrainRuleIncludePO(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            DrainRulePO exist = drainRuleAccessor.selectOneByDrainRuleCode(openRulePO.getTenantCode(),
                    openRulePO.getDrainRuleCode());
            if (exist != null) {
                int updated = drainRuleAccessor.update(openRulePO);
            } else {
                int inserted = drainRuleAccessor.insert(openRulePO);
            }

            int deleted4Topping = drainRuleToppingAccessor.deleteByDrainRuleCode(request.getTenantCode(),
                    request.getDrainRuleCode());
            if (!CollectionUtils.isEmpty(openRuleToppingPOList)) {
                openRuleToppingPOList.forEach(item -> {
                    int inserted4Topping = drainRuleToppingAccessor.insert(item);
                });
            }

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String openRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = drainRuleAccessor.deleteByDrainRuleCode(tenantCode, openRuleCode);
            int deleted4Topping = drainRuleToppingAccessor.deleteByDrainRuleCode(tenantCode, openRuleCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> putDispatch(DrainRuleDispatchPutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        List<DrainRuleDispatchPO> poList = convert(request);
        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = drainRuleDispatchAccessor.deleteByCleanRuleCode(request.getTenantCode(),
                    request.getDrainRuleCode());
            poList.forEach(po -> {
                drainRuleDispatchAccessor.insert(po);
            });

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("putDispatch error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }

        // 异步发送消息准备配置信息分发
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_DRAIN_RULE_DISPATCHED);
        jsonPayload.put(CommonConsts.JSON_KEY_TENANT_CODE, request.getTenantCode());
        jsonPayload.put(CommonConsts.JSON_KEY_DRAIN_RULE_CODE, request.getDrainRuleCode());
        asyncDispatcher.dispatch(jsonPayload);

        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<DrainRuleDispatchDTO> getDispatchByDrainRuleCode(String tenantCode, String openRuleCode) {
        TeaMachineResult<DrainRuleDispatchDTO> teaMachineResult;
        try {
            DrainRuleDispatchDTO dto = new DrainRuleDispatchDTO();
            dto.setDrainRuleCode(openRuleCode);

            List<DrainRuleDispatchPO> poList = drainRuleDispatchAccessor.selectListByCleanRuleCode(tenantCode, openRuleCode);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(po -> po.getShopGroupCode())
                        .collect(Collectors.toList()));
            }

            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getDispatchByDrainRuleCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
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

        List<DrainRuleToppingPO> poList = drainRuleToppingAccessor.selectListByDrainRuleCode(
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

                    ToppingPO toppingPO = toppingAccessor.selectOneByToppingCode(
                            po.getTenantCode(), po.getToppingCode());
                    if (toppingPO != null) {
                        dto.setToppingName(toppingPO.getToppingName());
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
