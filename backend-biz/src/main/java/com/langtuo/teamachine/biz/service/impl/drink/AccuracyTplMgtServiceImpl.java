package com.langtuo.teamachine.biz.service.impl.drink;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.biz.service.aync.AsyncDispatcher;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.AccuracyTplDTO;
import com.langtuo.teamachine.api.request.drink.AccuracyTplPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.drink.AccuracyTplMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.util.MessageUtils;
import com.langtuo.teamachine.dao.accessor.drink.AccuracyTplAccessor;
import com.langtuo.teamachine.dao.accessor.drink.AccuracyTplToppingAccessor;
import com.langtuo.teamachine.dao.po.drink.AccuracyTplPO;
import com.langtuo.teamachine.dao.po.drink.AccuracyTplToppingPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

    @Autowired
    private MessageSource messageSource;

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
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<AccuracyTplDTO>> search(String tenantName, String templateCode, String templateName,
            int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

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
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
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
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
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
            log.error("getByName error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(AccuracyTplPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }
        AccuracyTplPO po = convertToAccuracyTplPO(request);
        List<AccuracyTplToppingPO> toppingPOList = convertToAccuracyTplToppingPO(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            AccuracyTplPO exist = accuracyTplAccessor.selectOneByTplCode(po.getTenantCode(), po.getTemplateCode());
            if (exist != null) {
                int updated = accuracyTplAccessor.update(po);
            } else {
                int inserted = accuracyTplAccessor.insert(po);
            }

            int deleted = accuracyTplToppingAccessor.deleteByTplCode(request.getTenantCode(), request.getTemplateCode());
            if (!CollectionUtils.isEmpty(toppingPOList)) {
                toppingPOList.forEach(toppingPO -> {
                    int inserted = accuracyTplToppingAccessor.insert(toppingPO);
                });
            }
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
        }

        // 异步发送消息准备配置信息分发
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(BizConsts.JSON_KEY_BIZ_CODE, BizConsts.BIZ_CODE_ACCURACY_TPL_UPDATED);
        jsonPayload.put(BizConsts.JSON_KEY_TENANT_CODE, request.getTenantCode());
        jsonPayload.put(BizConsts.JSON_KEY_TEMPLATE_CODE, request.getTemplateCode());
        asyncDispatcher.dispatch(jsonPayload);

        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String templateCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = accuracyTplAccessor.deleteByTplCode(tenantCode, templateCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
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
