package com.langtuo.teamachine.biz.service.drink;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.biz.aync.AsyncDispatcher;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.AccuracyTplDTO;
import com.langtuo.teamachine.api.request.drink.AccuracyTplPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.drink.AccuracyTplMgtService;
import com.langtuo.teamachine.dao.accessor.drink.AccuracyTplAccessor;
import com.langtuo.teamachine.dao.accessor.drink.AccuracyTplToppingAccessor;
import com.langtuo.teamachine.dao.po.drink.AccuracyTplPO;
import com.langtuo.teamachine.dao.po.drink.AccuracyTplToppingPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.LocaleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.biz.convert.drink.AccuracyTplMgtConvertor.convertToAccuracyTplPO;
import static com.langtuo.teamachine.biz.convert.drink.AccuracyTplMgtConvertor.convertToAccuracyTplToppingPO;

@Component
@Slf4j
public class AccuracyTplMgtServiceImpl implements AccuracyTplMgtService {
    @Resource
    private AccuracyTplAccessor accuracyTplAccessor;

    @Resource
    private AccuracyTplToppingAccessor accuracyTplToppingAccessor;

    @Resource
    private AsyncDispatcher asyncDispatcher;

    @Override
    public TeaMachineResult<List<AccuracyTplDTO>> list(String tenantCode) {
        try {
            List<AccuracyTplPO> list = accuracyTplAccessor.list(tenantCode);
            List<AccuracyTplDTO> dtoList = list.stream()
                    .map(po -> convertToAccuracyTplPO(po))
                    .collect(Collectors.toList());
            return TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("accuracyTplMgtService|list|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<PageDTO<AccuracyTplDTO>> search(String tenantName, String templateCode, String templateName,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<AccuracyTplPO> pageInfo = accuracyTplAccessor.search(tenantName, templateCode, templateName,
                    pageNum, pageSize);
            List<AccuracyTplDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convertToAccuracyTplPO(po))
                    .collect(Collectors.toList());
            return TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("accuracyTplMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<AccuracyTplDTO> getByTplCode(String tenantCode, String specCode) {
        try {
            AccuracyTplPO po = accuracyTplAccessor.getByTplCode(tenantCode, specCode);
            AccuracyTplDTO dto = convertToAccuracyTplPO(po);
            return TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("accuracyTplMgtService|getByCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(AccuracyTplPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        AccuracyTplPO po = convertToAccuracyTplPO(request);
        List<AccuracyTplToppingPO> toppingPOList = convertToAccuracyTplToppingPO(request);

        TeaMachineResult<Void> teaMachineResult;
        if (request.isPutNew()) {
            teaMachineResult = putNew(po, toppingPOList);
        } else {
            teaMachineResult = putUpdate(po, toppingPOList);
        }

        // 异步发送消息准备配置信息分发
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_ACCURACY_TPL_UPDATED);
        jsonPayload.put(CommonConsts.JSON_KEY_TENANT_CODE, request.getTenantCode());
        jsonPayload.put(CommonConsts.JSON_KEY_TEMPLATE_CODE, request.getTemplateCode());
        asyncDispatcher.dispatch(jsonPayload);

        return teaMachineResult;
    }

    private TeaMachineResult<Void> putNew(AccuracyTplPO po, List<AccuracyTplToppingPO> toppingPOList) {
        try {
            AccuracyTplPO exist = accuracyTplAccessor.getByTplCode(po.getTenantCode(), po.getTemplateCode());
            if (exist != null) {
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = accuracyTplAccessor.insert(po);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                log.error("accuracyTplMgtService|putNewAccuracyTpl|error|" + inserted);
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }

            int deleted = accuracyTplToppingAccessor.deleteByTplCode(po.getTenantCode(), po.getTemplateCode());
            for (AccuracyTplToppingPO toppingPO : toppingPOList) {
                int inserted4Topping = accuracyTplToppingAccessor.insert(toppingPO);
                if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4Topping) {
                    log.error("accuracyTplMgtService|putNewAccuracyTplTopping|error|" + inserted);
                    return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("accuracyTplMgtService|putNew|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(AccuracyTplPO po, List<AccuracyTplToppingPO> toppingPOList) {
        try {
            AccuracyTplPO exist = accuracyTplAccessor.getByTplCode(po.getTenantCode(), po.getTemplateCode());
            if (exist == null) {
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = accuracyTplAccessor.update(po);
            if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
                log.error("accuracyTplMgtService|putUpdateAccuracyTpl|error|" + updated);
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }

            int deleted = accuracyTplToppingAccessor.deleteByTplCode(po.getTenantCode(), po.getTemplateCode());
            for (AccuracyTplToppingPO toppingPO : toppingPOList) {
                int inserted4Topping = accuracyTplToppingAccessor.insert(toppingPO);
                if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4Topping) {
                    log.error("accuracyTplMgtService|putUpdateAccuracyTplTopping|error|" + inserted4Topping);
                    return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("accuracyTplMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> deleteByTplCode(String tenantCode, String templateCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            int deleted = accuracyTplAccessor.deleteByTplCode(tenantCode, templateCode);

            // 异步发送消息准备配置信息分发
            JSONObject jsonPayload = new JSONObject();
            jsonPayload.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_ACCURACY_TPL_DELETED);
            jsonPayload.put(CommonConsts.JSON_KEY_TENANT_CODE, tenantCode);
            jsonPayload.put(CommonConsts.JSON_KEY_TEMPLATE_CODE, templateCode);
            asyncDispatcher.dispatch(jsonPayload);

            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("accuracyTplMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }
}
