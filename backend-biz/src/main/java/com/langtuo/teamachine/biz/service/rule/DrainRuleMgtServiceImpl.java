package com.langtuo.teamachine.biz.service.rule;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.biz.aync.AsyncDispatcher;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDispatchDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDTO;
import com.langtuo.teamachine.api.request.rule.DrainRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.DrainRulePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.rule.DrainRuleMgtService;
import com.langtuo.teamachine.biz.convert.rule.DrainRuleMgtConvertor;
import com.langtuo.teamachine.dao.accessor.drink.ToppingAccessor;
import com.langtuo.teamachine.dao.accessor.rule.*;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.po.rule.*;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.biz.convert.rule.DrainRuleMgtConvertor.*;

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

    @Override
    public TeaMachineResult<DrainRuleDTO> getByDrainRuleCode(String tenantCode, String drainRuleCode) {
        try {
            DrainRulePO po = drainRuleAccessor.getByDrainRuleCode(tenantCode, drainRuleCode);
            DrainRuleDTO dto = DrainRuleMgtConvertor.convertToDrainRuleDTO(po);
            return TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("drainRuleMgtService|getByCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<List<DrainRuleDTO>> list(String tenantCode) {
        try {
            List<DrainRulePO> poList = drainRuleAccessor.list(tenantCode);
            List<DrainRuleDTO> dtoList = convertToDrainRuleDTO(poList);
            return TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("drainRuleMgtService|list|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<List<DrainRuleDTO>> listByShopCode(String tenantCode, String shopCode) {
        try {
            ShopPO shopPO = shopAccessor.getByShopCode(tenantCode, shopCode);
            if (shopPO == null) {
                return TeaMachineResult.success();
            }

            List<DrainRuleDispatchPO> drainRuleDispatchPOList = drainRuleDispatchAccessor.listByShopGroupCode(
                    tenantCode, shopPO.getShopGroupCode());
            if (CollectionUtils.isEmpty(drainRuleDispatchPOList)) {
                return TeaMachineResult.success();
            }

            List<String> drainRuleCodeList = drainRuleDispatchPOList.stream()
                    .map(DrainRuleDispatchPO::getDrainRuleCode)
                    .collect(Collectors.toList());
            List<DrainRulePO> cleanRulePOList = drainRuleAccessor.listByDrainRuleCode(tenantCode,
                    drainRuleCodeList);
            List<DrainRuleDTO> drainRuleDTOList = convertToDrainRuleDTO(cleanRulePOList);
            return TeaMachineResult.success(drainRuleDTOList);
        } catch (Exception e) {
            log.error("drainRuleMgtService|listByShopCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<PageDTO<DrainRuleDTO>> search(String tenantCode, String openRuleCode,
            String openRuleName, int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<DrainRulePO> pageInfo = drainRuleAccessor.search(tenantCode, openRuleCode,
                    openRuleName, pageNum, pageSize);
            List<DrainRuleDTO> dtoList = convertToDrainRuleDTO(pageInfo.getList());
            return TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("drainRuleMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(DrainRulePutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        DrainRulePO drainRulePO = convertToDrainRulePO(request);
        List<DrainRuleToppingPO> drainRuleToppingPOList = convertToDrainRuleIncludePO(request);
        if (request.isPutNew()) {
            return putNew(drainRulePO, drainRuleToppingPOList);
        } else {
            return putUpdate(drainRulePO, drainRuleToppingPOList);
        }
    }

    private TeaMachineResult<Void> putNew(DrainRulePO po, List<DrainRuleToppingPO> toppingPOList) {
        try {
            DrainRulePO exist = drainRuleAccessor.getByDrainRuleCode(po.getTenantCode(), po.getDrainRuleCode());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = drainRuleAccessor.insert(po);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                log.error("drainRuleMgtService|putNewDrainRule|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }

            int deleted4Topping = drainRuleToppingAccessor.deleteByDrainRuleCode(po.getTenantCode(), po.getDrainRuleCode());
            for (DrainRuleToppingPO toppingPO : toppingPOList) {
                int inserted4Topping = drainRuleToppingAccessor.insert(toppingPO);
                if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4Topping) {
                    log.error("drainRuleMgtService|putNewTopping|error|" + inserted4Topping);
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("drainRuleMgtService|putNew|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(DrainRulePO po, List<DrainRuleToppingPO> toppingPOList) {
        try {
            DrainRulePO exist = drainRuleAccessor.getByDrainRuleCode(po.getTenantCode(), po.getDrainRuleCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = drainRuleAccessor.update(po);
            if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
                log.error("drainRuleMgtService|putUpdateDrainRule|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }

            int deleted4Topping = drainRuleToppingAccessor.deleteByDrainRuleCode(po.getTenantCode(), po.getDrainRuleCode());
            for (DrainRuleToppingPO toppingPO : toppingPOList) {
                int inserted4Topping = drainRuleToppingAccessor.insert(toppingPO);
                if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4Topping) {
                    log.error("drainRuleMgtService|putUpdateTopping|error|" + inserted4Topping);
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("drainRuleMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> deleteByDrainRuleCode(String tenantCode, String drainRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = drainRuleAccessor.deleteByDrainRuleCode(tenantCode, drainRuleCode);
            int deleted4Topping = drainRuleToppingAccessor.deleteByDrainRuleCode(tenantCode, drainRuleCode);
            int deleted4Dispatch = drainRuleDispatchAccessor.deleteAllByDrainRuleCode(tenantCode, drainRuleCode);
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("drainRuleMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> putDispatch(DrainRuleDispatchPutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        List<DrainRuleDispatchPO> poList = DrainRuleMgtConvertor.convertToDrainRuleDTO(request);
        try {
            List<String> shopGroupCodeList = DaoUtils.getShopGroupCodeListByLoginSession(request.getTenantCode());
            int deleted = drainRuleDispatchAccessor.deleteByDrainRuleCode(request.getTenantCode(),
                    request.getDrainRuleCode(), shopGroupCodeList);
            for (DrainRuleDispatchPO po : poList) {
                drainRuleDispatchAccessor.insert(po);
            }

            // 异步发送消息准备配置信息分发
            JSONObject jsonPayload = getAsyncDispatchMsg(request.getTenantCode(), request.getDrainRuleCode());
            asyncDispatcher.dispatch(jsonPayload);

            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("drainRuleMgtService|putDispatch|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<DrainRuleDispatchDTO> getDispatchByDrainRuleCode(String tenantCode, String drainRuleCode) {
        try {
            DrainRuleDispatchDTO dto = new DrainRuleDispatchDTO();
            dto.setDrainRuleCode(drainRuleCode);

            List<String> shopGroupCodeList = DaoUtils.getShopGroupCodeListByLoginSession(tenantCode);
            List<DrainRuleDispatchPO> poList = drainRuleDispatchAccessor.listByDrainRuleCode(tenantCode, drainRuleCode,
                    shopGroupCodeList);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(po -> po.getShopGroupCode())
                        .collect(Collectors.toList()));
            }

            return TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("drainRuleMgtService|getDispatchByDrainRuleCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    private JSONObject getAsyncDispatchMsg(String tenantCode, String cleanRuleCode) {
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_DRAIN_RULE_DISPATCH_REQUESTED);
        jsonPayload.put(CommonConsts.JSON_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(CommonConsts.JSON_KEY_LOGIN_NAME, DaoUtils.getAdminPOByLoginSession(tenantCode).getLoginName());
        jsonPayload.put(CommonConsts.JSON_KEY_DRAIN_RULE_CODE, cleanRuleCode);
        return jsonPayload;
    }
}
