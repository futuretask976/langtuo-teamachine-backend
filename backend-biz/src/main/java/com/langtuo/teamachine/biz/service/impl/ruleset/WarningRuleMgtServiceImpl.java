package com.langtuo.teamachine.biz.service.impl.ruleset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.ruleset.OpenRuleDTO;
import com.langtuo.teamachine.api.model.ruleset.OpenRuleToppingDTO;
import com.langtuo.teamachine.api.request.ruleset.OpenRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.ruleset.OpenRuleMgtService;
import com.langtuo.teamachine.dao.accessor.drinkset.ToppingAccessor;
import com.langtuo.teamachine.dao.accessor.ruleset.*;
import com.langtuo.teamachine.dao.po.drinkset.ToppingPO;
import com.langtuo.teamachine.dao.po.ruleset.OpenRuleToppingPO;
import com.langtuo.teamachine.dao.po.ruleset.OpenRulePO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OpenRuleMgtServiceImpl implements OpenRuleMgtService {
    @Resource
    private OpenRuleAccessor openRuleAccessor;

    @Resource
    private OpenRuleToppingAccessor openRuleToppingAccessor;

    @Resource
    private ToppingAccessor toppingAccessor;

    @Override
    public LangTuoResult<OpenRuleDTO> getByCode(String tenantCode, String openRuleCode) {
        LangTuoResult<OpenRuleDTO> langTuoResult = null;
        try {
            OpenRulePO po = openRuleAccessor.selectOneByCode(tenantCode, openRuleCode);
            OpenRuleDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
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
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
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
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
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
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(OpenRulePutRequest request) {
        if (request == null) {
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
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
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
        dto.setId(po.getId());
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
                    dto.setId(po.getId());
                    dto.setGmtCreated(po.getGmtCreated());
                    dto.setGmtModified(po.getGmtModified());
                    dto.setTenantCode(po.getTenantCode());
                    dto.setOpenRuleCode(po.getOpenRuleCode());
                    dto.setToppingCode(po.getToppingCode());
                    dto.setFlushTime(po.getFlushTime());
                    dto.setFlushWeight(po.getFlushWeight());

                    ToppingPO toppingPO = toppingAccessor.selectOneByCode(po.getTenantCode(), po.getToppingCode());
                    if (toppingPO != null) {
                        dto.setToppingName(toppingPO.getToppingName());
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
                    po.setFlushTime(openRuleToppingPutRequest.getFlushTime());
                    po.setFlushWeight(openRuleToppingPutRequest.getFlushWeight());
                    return po;
                }).collect(Collectors.toList());
        return list;
    }
}
