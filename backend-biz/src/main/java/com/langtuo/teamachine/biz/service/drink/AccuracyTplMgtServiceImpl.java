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
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
        TeaMachineResult<List<AccuracyTplDTO>> teaMachineResult;
        try {
            List<AccuracyTplPO> list = accuracyTplAccessor.selectList(tenantCode);
            List<AccuracyTplDTO> dtoList = list.stream()
                    .map(po -> convertToAccuracyTplPO(po))
                    .collect(Collectors.toList());
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("accuracyTplMgtService|list|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<AccuracyTplDTO>> search(String tenantName, String templateCode, String templateName,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<AccuracyTplDTO>> teaMachineResult;
        try {
            PageInfo<AccuracyTplPO> pageInfo = accuracyTplAccessor.search(tenantName, templateCode, templateName,
                    pageNum, pageSize);
            List<AccuracyTplDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convertToAccuracyTplPO(po))
                    .collect(Collectors.toList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("accuracyTplMgtService|search|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<AccuracyTplDTO> getByCode(String tenantCode, String specCode) {
        TeaMachineResult<AccuracyTplDTO> teaMachineResult;
        try {
            AccuracyTplPO po = accuracyTplAccessor.selectOneByTplCode(tenantCode, specCode);
            AccuracyTplDTO dto = convertToAccuracyTplPO(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("accuracyTplMgtService|getByCode|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<AccuracyTplDTO> getByName(String tenantCode, String specName) {
        TeaMachineResult<AccuracyTplDTO> teaMachineResult;
        try {
            AccuracyTplPO po = accuracyTplAccessor.selectOneByTplName(tenantCode, specName);
            AccuracyTplDTO dto = convertToAccuracyTplPO(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("accuracyTplMgtService|getByName|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(AccuracyTplPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        AccuracyTplPO po = convertToAccuracyTplPO(request);
        List<AccuracyTplToppingPO> toppingPOList = convertToAccuracyTplToppingPO(request);

        TeaMachineResult<Void> teaMachineResult;
        if (request.isNewPut()) {
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
            AccuracyTplPO exist = accuracyTplAccessor.selectOneByTplCode(po.getTenantCode(), po.getTemplateCode());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = accuracyTplAccessor.insert(po);
            if (inserted != CommonConsts.NUM_ONE) {
                log.error("accuracyTplMgtService|putNewAccuracyTpl|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }

            int deleted = accuracyTplToppingAccessor.deleteByTplCode(po.getTenantCode(), po.getTemplateCode());
            for (AccuracyTplToppingPO toppingPO : toppingPOList) {
                int inserted4Topping = accuracyTplToppingAccessor.insert(toppingPO);
                if (inserted4Topping != CommonConsts.NUM_ONE) {
                    log.error("accuracyTplMgtService|putNewAccuracyTplTopping|error|" + inserted);
                    return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("accuracyTplMgtService|putNew|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(AccuracyTplPO po, List<AccuracyTplToppingPO> toppingPOList) {
        try {
            AccuracyTplPO exist = accuracyTplAccessor.selectOneByTplCode(po.getTenantCode(), po.getTemplateCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = accuracyTplAccessor.update(po);
            if (updated != CommonConsts.NUM_ONE) {
                log.error("accuracyTplMgtService|putUpdateAccuracyTpl|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }

            int deleted = accuracyTplToppingAccessor.deleteByTplCode(po.getTenantCode(), po.getTemplateCode());
            for (AccuracyTplToppingPO toppingPO : toppingPOList) {
                int inserted4Topping = accuracyTplToppingAccessor.insert(toppingPO);
                if (inserted4Topping != CommonConsts.NUM_ONE) {
                    log.error("accuracyTplMgtService|putUpdateAccuracyTplTopping|error|" + inserted4Topping);
                    return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("accuracyTplMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String templateCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = accuracyTplAccessor.deleteByTplCode(tenantCode, templateCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("accuracyTplMgtService|delete|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    private AccuracyTplDTO convertToAccuracyTplPO(AccuracyTplPO po) {
        if (po == null) {
            return null;
        }

        AccuracyTplDTO dto = new AccuracyTplDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setTemplateCode(po.getTemplateCode());
        dto.setTemplateName(po.getTemplateName());
        dto.setOverMode(po.getOverMode());
        dto.setOverAmount(po.getOverAmount());
        dto.setUnderMode(po.getUnderMode());
        dto.setUnderAmount(po.getUnderAmount());

        List<AccuracyTplToppingPO> toppingPOList = accuracyTplToppingAccessor.selectList(
                po.getTenantCode(), po.getTemplateCode());
        if (!CollectionUtils.isEmpty(toppingPOList)) {
            List<String> toppingCodeList = Lists.newArrayList();
            toppingPOList.forEach(toppingPO -> {
                toppingCodeList.add(toppingPO.getToppingCode());
            });
            dto.setToppingCodeList(toppingCodeList);
        }
        return dto;
    }

    private AccuracyTplPO convertToAccuracyTplPO(AccuracyTplPutRequest request) {
        if (request == null) {
            return null;
        }

        AccuracyTplPO po = new AccuracyTplPO();
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setTemplateCode(request.getTemplateCode());
        po.setTemplateName(request.getTemplateName());
        po.setOverMode(request.getOverUnit());
        po.setOverAmount(request.getOverAmount());
        po.setUnderMode(request.getUnderUnit());
        po.setUnderAmount(request.getUnderAmount());
        po.setComment(request.getComment());
        return po;
    }

    private List<AccuracyTplToppingPO> convertToAccuracyTplToppingPO(AccuracyTplPutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getToppingCodeList())) {
            return null;
        }

        return request.getToppingCodeList().stream()
                .map(toppingCode -> {
                    AccuracyTplToppingPO po = new AccuracyTplToppingPO();
                    po.setTenantCode(request.getTenantCode());
                    po.setTemplateCode(request.getTemplateCode());
                    po.setToppingCode(toppingCode);
                    return po;
                }).collect(Collectors.toList());
    }
}
