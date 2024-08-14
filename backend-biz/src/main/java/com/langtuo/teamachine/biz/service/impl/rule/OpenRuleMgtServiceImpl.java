package com.langtuo.teamachine.biz.service.impl.rule;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.ToppingDTO;
import com.langtuo.teamachine.api.model.rule.OpenRuleDispatchDTO;
import com.langtuo.teamachine.api.model.rule.OpenRuleDTO;
import com.langtuo.teamachine.api.model.rule.OpenRuleToppingDTO;
import com.langtuo.teamachine.api.request.rule.OpenRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.OpenRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drink.ToppingMgtService;
import com.langtuo.teamachine.api.service.rule.OpenRuleMgtService;
import com.langtuo.teamachine.dao.accessor.rule.*;
import com.langtuo.teamachine.dao.po.rule.OpenRuleDispatchPO;
import com.langtuo.teamachine.dao.po.rule.OpenRuleToppingPO;
import com.langtuo.teamachine.dao.po.rule.OpenRulePO;
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
public class OpenRuleMgtServiceImpl implements OpenRuleMgtService {
    @Resource
    private OpenRuleAccessor openRuleAccessor;

    @Resource
    private OpenRuleToppingAccessor openRuleToppingAccessor;

    @Resource
    private OpenRuleDispatchAccessor openRuleDispatchAccessor;

    @Resource
    private ToppingMgtService toppingMgtService;

    @Override
    public LangTuoResult<OpenRuleDTO> getByCode(String tenantCode, String openRuleCode) {
        LangTuoResult<OpenRuleDTO> langTuoResult = null;
        try {
            OpenRulePO po = openRuleAccessor.selectOneByCode(tenantCode, openRuleCode);
            OpenRuleDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<OpenRuleDTO> getByName(String tenantCode, String openRuleName) {
        LangTuoResult<OpenRuleDTO> langTuoResult = null;
        try {
            OpenRulePO po = openRuleAccessor.selectOneByName(tenantCode, openRuleName);
            OpenRuleDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<OpenRuleDTO>> list(String tenantCode) {
        LangTuoResult<List<OpenRuleDTO>> langTuoResult = null;
        try {
            List<OpenRulePO> poList = openRuleAccessor.selectList(tenantCode);
            List<OpenRuleDTO> dtoList = convertToOpenRuleDTO(poList);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<OpenRuleDTO>> search(String tenantCode, String openRuleCode,
            String openRuleName, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<OpenRuleDTO>> langTuoResult = null;
        try {
            PageInfo<OpenRulePO> pageInfo = openRuleAccessor.search(tenantCode, openRuleCode,
                    openRuleName, pageNum, pageSize);
            List<OpenRuleDTO> dtoList = convertToOpenRuleDTO(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(OpenRulePutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        OpenRulePO openRulePO = convertToOpenRulePO(request);
        List<OpenRuleToppingPO> openRuleToppingPOList = convertToOpenRuleIncludePO(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            OpenRulePO exist = openRuleAccessor.selectOneByCode(openRulePO.getTenantCode(),
                    openRulePO.getOpenRuleCode());
            if (exist != null) {
                int updated = openRuleAccessor.update(openRulePO);
            } else {
                int inserted = openRuleAccessor.insert(openRulePO);
            }

            int deleted4Topping = openRuleToppingAccessor.delete(request.getTenantCode(),
                    request.getOpenRuleCode());
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
    public LangTuoResult<Void> putDispatch(OpenRuleDispatchPutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        List<OpenRuleDispatchPO> poList = convert(request);
        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = openRuleDispatchAccessor.delete(request.getTenantCode(),
                    request.getOpenRuleCode());
            poList.forEach(po -> {
                openRuleDispatchAccessor.insert(po);
            });

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("putDispatch error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<OpenRuleDispatchDTO> getDispatchByCode(String tenantCode, String openRuleCode) {
        LangTuoResult<OpenRuleDispatchDTO> langTuoResult = null;
        try {
            OpenRuleDispatchDTO dto = new OpenRuleDispatchDTO();
            dto.setOpenRuleCode(openRuleCode);

            List<OpenRuleDispatchPO> poList = openRuleDispatchAccessor.selectList(tenantCode, openRuleCode);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(po -> po.getShopGroupCode())
                        .collect(Collectors.toList()));
            }

            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getDispatchByOpenRuleCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    private List<OpenRuleDTO> convertToOpenRuleDTO(List<OpenRulePO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<OpenRuleDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private OpenRuleDTO convert(OpenRulePO po) {
        if (po == null) {
            return null;
        }

        OpenRuleDTO dto = new OpenRuleDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTenantCode(po.getTenantCode());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setOpenRuleCode(po.getOpenRuleCode());
        dto.setOpenRuleName(po.getOpenRuleName());
        dto.setDefaultRule(po.getDefaultRule());

        List<OpenRuleToppingPO> poList = openRuleToppingAccessor.selectList(
                po.getTenantCode(), po.getOpenRuleCode());
        if (!CollectionUtils.isEmpty(poList)) {
            dto.setToppingRuleList(convertToOpenRuleToppingDTO(poList));
        }
        return dto;
    }

    private List<OpenRuleToppingDTO> convertToOpenRuleToppingDTO(List<OpenRuleToppingPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<OpenRuleToppingDTO> list = poList.stream()
                .map(po -> {
                    OpenRuleToppingDTO dto = new OpenRuleToppingDTO();
                    dto.setTenantCode(po.getTenantCode());
                    dto.setOpenRuleCode(po.getOpenRuleCode());
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

    private OpenRulePO convertToOpenRulePO(OpenRulePutRequest request) {
        if (request == null) {
            return null;
        }

        OpenRulePO po = new OpenRulePO();
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setOpenRuleCode(request.getOpenRuleCode());
        po.setOpenRuleName(request.getOpenRuleName());
        po.setDefaultRule(request.getDefaultRule());
        return po;
    }

    private List<OpenRuleToppingPO> convertToOpenRuleIncludePO(OpenRulePutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getToppingRuleList())) {
            return null;
        }

        List<OpenRuleToppingPO> list = request.getToppingRuleList().stream()
                .filter(Objects::nonNull)
                .map(openRuleToppingPutRequest -> {
                    OpenRuleToppingPO po = new OpenRuleToppingPO();
                    po.setTenantCode(request.getTenantCode());
                    po.setOpenRuleCode(request.getOpenRuleCode());
                    po.setToppingCode(openRuleToppingPutRequest.getToppingCode());
                    po.setFlushSec(openRuleToppingPutRequest.getFlushSec());
                    po.setFlushWeight(openRuleToppingPutRequest.getFlushWeight());
                    return po;
                }).collect(Collectors.toList());
        return list;
    }

    private List<OpenRuleDispatchPO> convert(OpenRuleDispatchPutRequest request) {
        String tenantCode = request.getTenantCode();
        String openRuleCode = request.getOpenRuleCode();

        return request.getShopGroupCodeList().stream()
                .map(shopGroupCode -> {
                    OpenRuleDispatchPO po = new OpenRuleDispatchPO();
                    po.setTenantCode(tenantCode);
                    po.setOpenRuleCode(openRuleCode);
                    po.setShopGroupCode(shopGroupCode);
                    return po;
                }).collect(Collectors.toList());
    }
}
