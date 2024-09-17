package com.langtuo.teamachine.biz.service.rule;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.biz.aync.AsyncDispatcher;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.WarningRuleDispatchDTO;
import com.langtuo.teamachine.api.model.rule.WarningRuleDTO;
import com.langtuo.teamachine.api.request.rule.WarningRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.WarningRulePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.rule.WarningRuleMgtService;
import com.langtuo.teamachine.dao.accessor.rule.WarningRuleAccessor;
import com.langtuo.teamachine.dao.accessor.rule.WarningRuleDispatchAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.po.rule.WarningRuleDispatchPO;
import com.langtuo.teamachine.dao.po.rule.WarningRulePO;
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
import java.util.stream.Collectors;

@Component
@Slf4j
public class WarningRuleMgtServiceImpl implements WarningRuleMgtService {
    @Resource
    private WarningRuleAccessor warningRuleAccessor;

    @Resource
    private WarningRuleDispatchAccessor warningRuleDispatchAccessor;

    @Resource
    private ShopAccessor shopAccessor;

    @Resource
    private AsyncDispatcher asyncDispatcher;

    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<WarningRuleDTO> getByCode(String tenantCode, String warningRuleCode) {
        TeaMachineResult<WarningRuleDTO> teaMachineResult;
        try {
            WarningRulePO po = warningRuleAccessor.getByWarningRuleCode(tenantCode, warningRuleCode);
            WarningRuleDTO dto = convertToWarningRuleDTO(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("warningRuleMgtService|getByCode|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<WarningRuleDTO> getByName(String tenantCode, String warningRuleName) {
        TeaMachineResult<WarningRuleDTO> teaMachineResult;
        try {
            WarningRulePO po = warningRuleAccessor.getByWarningRuleName(tenantCode, warningRuleName);
            WarningRuleDTO dto = convertToWarningRuleDTO(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("warningRuleMgtService|getByName|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<WarningRuleDTO>> list(String tenantCode) {
        TeaMachineResult<List<WarningRuleDTO>> teaMachineResult;
        try {
            List<WarningRulePO> poList = warningRuleAccessor.list(tenantCode);
            List<WarningRuleDTO> dtoList = convertToWarningRuleDTO(poList);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("warningRuleMgtService|list|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<WarningRuleDTO>> listByShopCode(String tenantCode, String shopCode) {
        TeaMachineResult<List<WarningRuleDTO>> teaMachineResult;
        try {
            ShopPO shopPO = shopAccessor.getByShopCode(tenantCode, shopCode);
            if (shopPO == null) {
                teaMachineResult = TeaMachineResult.success();
            }

            List<WarningRuleDispatchPO> warningRuleDispatchPOList = warningRuleDispatchAccessor.listByShopGroupCode(
                    tenantCode, shopPO.getShopGroupCode());
            if (CollectionUtils.isEmpty(warningRuleDispatchPOList)) {
                teaMachineResult = TeaMachineResult.success();
            }

            List<String> warningRuleCodeList = warningRuleDispatchPOList.stream()
                    .map(WarningRuleDispatchPO::getWarningRuleCode)
                    .collect(Collectors.toList());
            List<WarningRulePO> warningRulePOList = warningRuleAccessor.listByWarningRuleCode(tenantCode,
                    warningRuleCodeList);
            List<WarningRuleDTO> drainRuleDTOList = convertToWarningRuleDTO(warningRulePOList);
            teaMachineResult = TeaMachineResult.success(drainRuleDTOList);
        } catch (Exception e) {
            log.error("warningRuleMgtService|listByShopCode|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<WarningRuleDTO>> search(String tenantCode, String warningRuleCode,
            String warningRuleName, int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<WarningRuleDTO>> teaMachineResult;
        try {
            PageInfo<WarningRulePO> pageInfo = warningRuleAccessor.search(tenantCode, warningRuleCode,
                    warningRuleName, pageNum, pageSize);
            List<WarningRuleDTO> dtoList = convertToWarningRuleDTO(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("warningRuleMgtService|search|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(WarningRulePutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        WarningRulePO po = convertToWarningRuleDTO(request);
        if (request.isPutNew()) {
            return putNew(po);
        } else {
            return putUpdate(po);
        }
    }

    private TeaMachineResult<Void> putNew(WarningRulePO po) {
        try {
            WarningRulePO exist = warningRuleAccessor.getByWarningRuleCode(po.getTenantCode(),
                    po.getWarningRuleCode());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = warningRuleAccessor.insert(po);
            if (CommonConsts.NUM_ONE != inserted) {
                log.error("warningRuleMgtService|putNewWarningRule|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("warningRuleMgtService|putNew|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(WarningRulePO po) {
        try {
            WarningRulePO exist = warningRuleAccessor.getByWarningRuleCode(po.getTenantCode(),
                    po.getWarningRuleCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = warningRuleAccessor.update(po);
            if (CommonConsts.NUM_ONE != updated) {
                log.error("warningRuleMgtService|putUpdateWarningRule|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("warningRuleMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String warningRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = warningRuleAccessor.deleteByWarningRuleCode(tenantCode, warningRuleCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("warningRuleMgtService|delete|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> putDispatch(WarningRuleDispatchPutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        List<WarningRuleDispatchPO> poList = convertToWarningRuleDTO(request);
        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = warningRuleDispatchAccessor.deleteByWarningRuleCode(request.getTenantCode(),
                    request.getWarningRuleCode());
            poList.forEach(po -> {
                warningRuleDispatchAccessor.insert(po);
            });

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("warningRuleMgtService|putDispatch|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }

        // 异步发送消息准备配置信息分发
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_WARNING_RULE_DISPATCH_REQUESTED);
        jsonPayload.put(CommonConsts.JSON_KEY_TENANT_CODE, request.getTenantCode());
        jsonPayload.put(CommonConsts.JSON_KEY_WARNING_RULE_CODE, request.getWarningRuleCode());
        asyncDispatcher.dispatch(jsonPayload);

        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<WarningRuleDispatchDTO> getDispatchByWarningRuleCode(String tenantCode, String warningRuleCode) {
        TeaMachineResult<WarningRuleDispatchDTO> teaMachineResult;
        try {
            WarningRuleDispatchDTO dto = new WarningRuleDispatchDTO();
            dto.setWarningRuleCode(warningRuleCode);

            List<WarningRuleDispatchPO> poList = warningRuleDispatchAccessor.listByWarningRuleCode(tenantCode, warningRuleCode);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(po -> po.getShopGroupCode())
                        .collect(Collectors.toList()));
            }

            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("warningRuleMgtService|getDispatchByWarningRuleCode|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    private List<WarningRuleDTO> convertToWarningRuleDTO(List<WarningRulePO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<WarningRuleDTO> list = poList.stream()
                .map(po -> convertToWarningRuleDTO(po))
                .collect(Collectors.toList());
        return list;
    }

    private WarningRuleDTO convertToWarningRuleDTO(WarningRulePO po) {
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

    private WarningRulePO convertToWarningRuleDTO(WarningRulePutRequest request) {
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

    private List<WarningRuleDispatchPO> convertToWarningRuleDTO(WarningRuleDispatchPutRequest request) {
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
