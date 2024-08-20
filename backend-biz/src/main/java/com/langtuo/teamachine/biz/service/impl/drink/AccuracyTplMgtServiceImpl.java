package com.langtuo.teamachine.biz.service.impl.drink;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.AccuracyTplDTO;
import com.langtuo.teamachine.api.request.drink.AccuracyTplPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drink.AccuracyTplMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.dao.accessor.drink.AccuracyTplAccessor;
import com.langtuo.teamachine.dao.accessor.drink.AccuracyTplToppingAccessor;
import com.langtuo.teamachine.dao.po.drink.AccuracyTplPO;
import com.langtuo.teamachine.dao.po.drink.AccuracyTplToppingPO;
import com.langtuo.teamachine.mqtt.publish.MqttPublisher4Console;
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
    private MqttPublisher4Console mqttPublisher4Console;

    @Override
    public LangTuoResult<List<AccuracyTplDTO>> list(String tenantCode) {
        LangTuoResult<List<AccuracyTplDTO>> langTuoResult;
        try {
            List<AccuracyTplPO> list = accuracyTplAccessor.selectList(tenantCode);
            List<AccuracyTplDTO> dtoList = list.stream()
                    .map(po -> convertToAccuracyTplPO(po))
                    .collect(Collectors.toList());
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<AccuracyTplDTO>> search(String tenantName, String templateCode, String templateName,
            int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        LangTuoResult<PageDTO<AccuracyTplDTO>> langTuoResult;
        try {
            PageInfo<AccuracyTplPO> pageInfo = accuracyTplAccessor.search(tenantName, templateCode, templateName,
                    pageNum, pageSize);
            List<AccuracyTplDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convertToAccuracyTplPO(po))
                    .collect(Collectors.toList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<AccuracyTplDTO> getByCode(String tenantCode, String specCode) {
        LangTuoResult<AccuracyTplDTO> langTuoResult;
        try {
            AccuracyTplPO po = accuracyTplAccessor.selectOneByCode(tenantCode, specCode);
            AccuracyTplDTO dto = convertToAccuracyTplPO(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<AccuracyTplDTO> getByName(String tenantCode, String specName) {
        LangTuoResult<AccuracyTplDTO> langTuoResult;
        try {
            AccuracyTplPO po = accuracyTplAccessor.selectOneByName(tenantCode, specName);
            AccuracyTplDTO dto = convertToAccuracyTplPO(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(AccuracyTplPutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }
        AccuracyTplPO po = convertToAccuracyTplPO(request);
        List<AccuracyTplToppingPO> toppingPOList = convertToAccuracyTplToppingPO(request);

        LangTuoResult<Void> langTuoResult;
        try {
            AccuracyTplPO exist = accuracyTplAccessor.selectOneByCode(po.getTenantCode(), po.getTemplateCode());
            if (exist != null) {
                int updated = accuracyTplAccessor.update(po);
            } else {
                int inserted = accuracyTplAccessor.insert(po);
            }

            int deleted = accuracyTplToppingAccessor.delete(request.getTenantCode(), request.getTemplateCode());
            if (!CollectionUtils.isEmpty(toppingPOList)) {
                toppingPOList.forEach(toppingPO -> {
                    int inserted = accuracyTplToppingAccessor.insert(toppingPO);
                });
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }

        // 异步发送消息准备配置信息分发
        mqttPublisher4Console.send4AccuracyTpl(request.getTenantCode(), request.getTemplateCode());

        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String templateCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult;
        try {
            int deleted = accuracyTplAccessor.delete(tenantCode, templateCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
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
