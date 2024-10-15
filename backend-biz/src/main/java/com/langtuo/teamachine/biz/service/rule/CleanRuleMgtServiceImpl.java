package com.langtuo.teamachine.biz.service.rule;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.biz.aync.AsyncDispatcher;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.CleanRuleDTO;
import com.langtuo.teamachine.api.model.rule.CleanRuleDispatchDTO;
import com.langtuo.teamachine.api.request.rule.CleanRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.CleanRulePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.rule.CleanRuleMgtService;
import com.langtuo.teamachine.dao.accessor.rule.CleanRuleAccessor;
import com.langtuo.teamachine.dao.accessor.rule.CleanRuleDispatchAccessor;
import com.langtuo.teamachine.dao.accessor.rule.CleanRuleExceptAccessor;
import com.langtuo.teamachine.dao.accessor.rule.CleanRuleStepAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.po.rule.CleanRuleDispatchPO;
import com.langtuo.teamachine.dao.po.rule.CleanRuleExceptPO;
import com.langtuo.teamachine.dao.po.rule.CleanRulePO;
import com.langtuo.teamachine.dao.po.rule.CleanRuleStepPO;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.LocaleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.biz.convert.rule.CleanRuleMgtConvertor.*;

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
    private ShopAccessor shopAccessor;

    @Resource
    private AsyncDispatcher asyncDispatcher;

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<CleanRuleDTO> getByCleanRuleCode(String tenantCode, String cleanRuleCode) {
        try {
            CleanRulePO po = cleanRuleAccessor.getByCleanRuleCode(tenantCode, cleanRuleCode);
            CleanRuleDTO dto = convertToCleanRuleStepDTO(po);
            return TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("cleanRuleMgtService|getByCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<List<CleanRuleDTO>> list(String tenantCode) {
        try {
            List<CleanRulePO> poList = cleanRuleAccessor.selectList(tenantCode);
            List<CleanRuleDTO> dtoList = convertToCleanRuleDTO(poList);
            return TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("cleanRuleMgtService|list|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<List<CleanRuleDTO>> listByShopCode(String tenantCode, String shopCode) {
        try {
            ShopPO shopPO = shopAccessor.getByShopCode(tenantCode, shopCode);
            if (shopPO == null) {
                return TeaMachineResult.success();
            }

            List<CleanRuleDispatchPO> cleanRuleDispatchPOList = cleanRuleDispatchAccessor.listByShopGroupCode(
                    tenantCode, shopPO.getShopGroupCode());
            if (CollectionUtils.isEmpty(cleanRuleDispatchPOList)) {
                return TeaMachineResult.success();
            }

            List<String> cleanRuleCodeList = cleanRuleDispatchPOList.stream()
                    .map(CleanRuleDispatchPO::getCleanRuleCode)
                    .collect(Collectors.toList());
            List<CleanRulePO> cleanRulePOList = cleanRuleAccessor.selectListByCleanRuleCode(tenantCode,
                    cleanRuleCodeList);
            List<CleanRuleDTO> cleanRuleDTOList = convertToCleanRuleDTO(cleanRulePOList);
            return TeaMachineResult.success(cleanRuleDTOList);
        } catch (Exception e) {
            log.error("cleanRuleMgtService|listByShopCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<PageDTO<CleanRuleDTO>> search(String tenantCode, String cleanRuleCode, String cleanRuleName,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<CleanRulePO> pageInfo = cleanRuleAccessor.search(tenantCode, cleanRuleCode, cleanRuleName,
                    pageNum, pageSize);
            List<CleanRuleDTO> dtoList = convertToCleanRuleDTO(pageInfo.getList());
            return TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("cleanRuleMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(CleanRulePutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        CleanRulePO cleanRulePO = convertToCleanRulePO(request);
        List<CleanRuleStepPO> cleanRuleStepPOList = convertToCleanRuleStepPO(request);
        List<CleanRuleExceptPO> cleanRuleExceptPOList = convertToCleanRuleExceptPO(request);
        try {
            if (request.isPutNew()) {
                return doPutNew(cleanRulePO, cleanRuleStepPOList, cleanRuleExceptPOList);
            } else {
                return doPutUpdate(cleanRulePO, cleanRuleStepPOList, cleanRuleExceptPOList);
            }
        } catch (Exception e) {
            log.error("cleanRuleMgtService|put|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> deleteByCleanRuleCode(String tenantCode, String cleanRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            return doDeleteByCleanRuleCode(tenantCode, cleanRuleCode);
        } catch (Exception e) {
            log.error("cleanRuleMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> putDispatch(CleanRuleDispatchPutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        List<CleanRuleDispatchPO> poList = convertToCleanRuleStepDTO(request);
        try {
            TeaMachineResult<Void> result = doPutDispatch(request.getTenantCode(), request.getCleanRuleCode(), poList);

            // 异步发送消息准备配置信息分发
            JSONObject jsonPayload = getAsyncDispatchMsg(request.getTenantCode(), request.getCleanRuleCode());
            asyncDispatcher.dispatch(jsonPayload);

            return result;
        } catch (Exception e) {
            log.error("cleanRuleMgtService|putDispatch|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<CleanRuleDispatchDTO> getDispatchByCleanRuleCode(String tenantCode, String cleanRuleCode) {
        try {
            CleanRuleDispatchDTO dto = new CleanRuleDispatchDTO();
            dto.setCleanRuleCode(cleanRuleCode);

            List<String> shopGroupCodeList = DaoUtils.getShopGroupCodeListByLoginSession(tenantCode);
            List<CleanRuleDispatchPO> poList = cleanRuleDispatchAccessor.listByCleanRuleCode(tenantCode, cleanRuleCode,
                    shopGroupCodeList);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(CleanRuleDispatchPO::getShopGroupCode)
                        .collect(Collectors.toList()));
            }
            return TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("cleanRuleMgtService|getDispatchByCleanRuleCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutNew(CleanRulePO po, List<CleanRuleStepPO> stepPOList,
            List<CleanRuleExceptPO> exceptPOList) {
        CleanRulePO exist = cleanRuleAccessor.getByCleanRuleCode(po.getTenantCode(), po.getCleanRuleCode());
        if (exist != null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
        }

        int inserted = cleanRuleAccessor.insert(po);
        if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
            log.error("cleanRuleMgtService|doPutNew|error|" + inserted);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }

        int deleted4Step = cleanRuleStepAccessor.deleteByCleanRuleCode(po.getTenantCode(), po.getCleanRuleCode());
        for (CleanRuleStepPO stepPO : stepPOList) {
            int inserted4Step = cleanRuleStepAccessor.insert(stepPO);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4Step) {
                log.error("cleanRuleMgtService|doPutNew|error|" + inserted4Step);
            }
        }

        int deleted4Except = cleanRuleExceptAccessor.deleteByCleanRuleCode(po.getTenantCode(),
                po.getCleanRuleCode());
        for (CleanRuleExceptPO exceptPO : exceptPOList) {
            int inserted4Except = cleanRuleExceptAccessor.insert(exceptPO);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                log.error("cleanRuleMgtService|doPutNew|error|" + inserted4Except);
            }
        }
        return TeaMachineResult.success();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutUpdate(CleanRulePO po, List<CleanRuleStepPO> stepPOList,
            List<CleanRuleExceptPO> exceptPOList) {
        CleanRulePO exist = cleanRuleAccessor.getByCleanRuleCode(po.getTenantCode(), po.getCleanRuleCode());
        if (exist == null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
        }

        int updated = cleanRuleAccessor.update(po);
        if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
            log.error("cleanRuleMgtService|doPutUpdate|error|" + updated);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }

        int deleted4Step = cleanRuleStepAccessor.deleteByCleanRuleCode(po.getTenantCode(), po.getCleanRuleCode());
        for (CleanRuleStepPO stepPO : stepPOList) {
            int inserted4Step = cleanRuleStepAccessor.insert(stepPO);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4Step) {
                log.error("cleanRuleMgtService|putUpdateStep|error|" + inserted4Step);
            }
        }

        int deleted4Except = cleanRuleExceptAccessor.deleteByCleanRuleCode(po.getTenantCode(),
                po.getCleanRuleCode());
        for (CleanRuleExceptPO exceptPO : exceptPOList) {
            int inserted4Except = cleanRuleExceptAccessor.insert(exceptPO);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4Except) {
                log.error("cleanRuleMgtService|doPutUpdate|error|" + inserted4Except);
            }
        }
        return TeaMachineResult.success();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doDeleteByCleanRuleCode(String tenantCode, String cleanRuleCode) {
        int deleted = cleanRuleAccessor.deleteByCleanRuleCode(tenantCode, cleanRuleCode);
        int deleted4Step = cleanRuleStepAccessor.deleteByCleanRuleCode(tenantCode, cleanRuleCode);
        int deleted4Except = cleanRuleExceptAccessor.deleteByCleanRuleCode(tenantCode, cleanRuleCode);
        int deleted4Dispatch = cleanRuleDispatchAccessor.deleteAllByCleanRuleCode(tenantCode, cleanRuleCode);
        return TeaMachineResult.success();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutDispatch(String tenantCode, String cleanRuleCode,
            List<CleanRuleDispatchPO> poList) {
        List<String> shopGroupCodeList = DaoUtils.getShopGroupCodeListByLoginSession(tenantCode);
        int deleted = cleanRuleDispatchAccessor.deleteByCleanRuleCode(tenantCode, cleanRuleCode, shopGroupCodeList);
        for (CleanRuleDispatchPO po : poList) {
            cleanRuleDispatchAccessor.insert(po);
        }
        return TeaMachineResult.success();
    }

    private JSONObject getAsyncDispatchMsg(String tenantCode, String cleanRuleCode) {
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_CLEAN_RULE_DISPATCH_REQUESTED);
        jsonPayload.put(CommonConsts.JSON_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(CommonConsts.JSON_KEY_LOGIN_NAME, DaoUtils.getAdminPOByLoginSession(tenantCode).getLoginName());
        jsonPayload.put(CommonConsts.JSON_KEY_CLEAN_RULE_CODE, cleanRuleCode);
        return jsonPayload;
    }
}
