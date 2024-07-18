package com.langtuo.teamachine.biz.service.impl.drinkset;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.*;
import com.langtuo.teamachine.api.model.drinkset.*;
import com.langtuo.teamachine.api.request.drinkset.SpecItemRulePutRequest;
import com.langtuo.teamachine.api.request.drinkset.TeaPutRequest;
import com.langtuo.teamachine.api.request.drinkset.TeaUnitPutRequest;
import com.langtuo.teamachine.api.request.drinkset.ToppingAdjustRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drinkset.TeaMgtService;
import com.langtuo.teamachine.dao.accessor.drinkset.*;
import com.langtuo.teamachine.dao.po.drinkset.*;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TeaMgtServiceImpl implements TeaMgtService {
    @Resource
    private TeaAccessor teaAccessor;

    @Resource
    private TeaUnitAccessor teaUnitAccessor;

    @Resource
    private ToppingAdjustRuleAccessor toppingAdjustRuleAccessor;

    @Resource
    private SpecAccessor specAccessor;

    @Resource
    private SpecItemAccessor specItemAccessor;

    @Resource
    private ToppingAccessor toppingAccessor;

    @Override
    public LangTuoResult<TeaDTO> getByCode(String tenantCode, String teaCode) {
        LangTuoResult<TeaDTO> langTuoResult = null;
        try {
            TeaPO po = teaAccessor.selectOneByCode(tenantCode, teaCode);
            TeaDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<TeaDTO> getByName(String tenantCode, String teaName) {
        LangTuoResult<TeaDTO> langTuoResult = null;
        try {
            TeaPO po = teaAccessor.selectOneByName(tenantCode, teaName);
            TeaDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<TeaDTO>> list(String tenantCode) {
        LangTuoResult<List<TeaDTO>> langTuoResult = null;
        try {
            List<TeaPO> teaPOList = teaAccessor.selectList(tenantCode);
            List<TeaDTO> teaDTOList = teaPOList.stream()
                    .map(po -> {
                        TeaDTO dto = convert(po);
//                        List<TeaToppingRelPO> relPOList = teaToppingRelAccessor.selectList(
//                                dto.getTenantCode(), dto.getTeaCode());
//                        if (!CollectionUtils.isEmpty(relPOList)) {
//                            dto.setTeaToppingRelList(relPOList.stream()
//                                    .map(relPO -> convert(relPO))
//                                    .collect(Collectors.toList()));
//                        }
                        return dto;
                    })
                    .collect(Collectors.toList());
            langTuoResult = LangTuoResult.success(teaDTOList);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<TeaDTO>> search(String tenantName, String teaCode, String teaName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<TeaDTO>> langTuoResult = null;
        try {
            PageInfo<TeaPO> pageInfo = teaAccessor.search(tenantName, teaCode, teaName,
                    pageNum, pageSize);
            List<TeaDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> {
                        TeaDTO dto = convert(po);
//                        List<TeaToppingRelPO> relPOList = teaToppingRelAccessor.selectList(
//                                dto.getTenantCode(), dto.getTeaCode());
//                        if (!CollectionUtils.isEmpty(relPOList)) {
//                            dto.setTeaToppingRelList(relPOList.stream()
//                                    .map(relPO -> convert(relPO))
//                                    .collect(Collectors.toList()));
//                        }
                        return dto;
                    })
                    .collect(Collectors.toList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(TeaPutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaPO teaPO = convertToTeaPO(request);
        List<TeaUnitPO> teaUnitPOList = convertToTeaUnitPO(request);
        List<ToppingAdjustRulePO> toppingAdjustRulePOList = convertToToppingAdjustRulePO(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            TeaPO exist = teaAccessor.selectOneByCode(teaPO.getTenantCode(), teaPO.getTeaCode());
            if (exist != null) {
                int updated = teaAccessor.update(teaPO);
            } else {
                int inserted = teaAccessor.insert(teaPO);
            }

            int deleted4TeaUnit = teaUnitAccessor.delete(teaPO.getTenantCode(), teaPO.getTeaCode());
            teaUnitPOList.forEach(item -> {
                int inserted4TeaUnit = teaUnitAccessor.insert(item);
            });

            int deleted4ToppingAdjustRule = toppingAdjustRuleAccessor.delete(teaPO.getTenantCode(), teaPO.getTeaCode());
            toppingAdjustRulePOList.forEach(item -> {
                int inserted4ToppingAdjustRule = toppingAdjustRuleAccessor.insert(item);
            });

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String teaCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = teaAccessor.delete(tenantCode, teaCode);
            int deleted4TeaUnit = teaUnitAccessor.delete(tenantCode, teaCode);
            int deleted4ToppingAdjustRule = toppingAdjustRuleAccessor.delete(tenantCode, teaCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private TeaDTO convert(TeaPO po) {
        if (po == null) {
            return null;
        }

        TeaDTO dto = new TeaDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTeaCode(po.getTeaCode());
        dto.setTeaName(po.getTeaName());
        dto.setState(po.getState());
        dto.setTeaTypeCode(po.getTeaTypeCode());
        dto.setOuterTeaCode(po.getOuterTeaCode());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());

        List<TeaUnitPO> teaUnitDTOList = teaUnitAccessor.selectList(po.getTenantCode(), po.getTeaCode());
        injectTeaUnitDTO(dto, teaUnitDTOList);
        return dto;
    }

    private void injectTeaUnitDTO(TeaDTO teaDTO, List<TeaUnitPO> teaUnitPOList) {
        if (CollectionUtils.isEmpty(teaUnitPOList)) {
            return;
        }

        Map<String, SpecRuleDTO> specRuleDTOMap = Maps.newHashMap();
        Map<String, ToppingBaseRuleDTO> topppingBaseRuleDTOMap = Maps.newHashMap();
        Map<String, TeaUnitDTO> teaUnitDTOMap = Maps.newHashMap();
        Set<String> selectedSpecItemCodeSet = Sets.newHashSet();
        for (TeaUnitPO teaUnitPO : teaUnitPOList) {
            TeaUnitDTO teaUnitDTO = teaUnitDTOMap.get(teaUnitPO.getTeaUnitCode());
            if (teaUnitDTO == null) {
                teaUnitDTO = new TeaUnitDTO();
                teaUnitDTO.setId(teaUnitPO.getId());
                teaUnitDTO.setGmtCreated(teaUnitPO.getGmtCreated());
                teaUnitDTO.setGmtModified(teaUnitPO.getGmtModified());
                teaUnitDTO.setTeaUnitCode(teaUnitPO.getTeaUnitCode());
                teaUnitDTO.setTeaUnitName(teaUnitPO.getTeaUnitName());
                teaUnitDTOMap.put(teaUnitPO.getTeaUnitCode(), teaUnitDTO);
            }

            // 设置规格项规则，多行对应一个TeaUnit，每行是独立的specItemRule，都要初始化
            SpecItemRuleDTO specItemRuleDTO = new SpecItemRuleDTO();
            specItemRuleDTO.setId(teaUnitPO.getId());
            specItemRuleDTO.setGmtCreated(teaUnitPO.getGmtCreated());
            specItemRuleDTO.setGmtModified(teaUnitPO.getGmtModified());
            specItemRuleDTO.setSpecItemCode(teaUnitPO.getSpecItemCode());
            SpecItemPO specItemPO = specItemAccessor.selectOneByCode(teaUnitPO.getTenantCode(),
                    teaUnitPO.getSpecItemCode());
            if (specItemPO != null) {
                specItemRuleDTO.setSpecItemName(specItemPO.getSpecItemName());
                specItemRuleDTO.setOuterSpecItemCode(specItemPO.getOuterSpecItemCode());
            }
            if (teaUnitDTO.getSpecItemRuleList() == null) {
                teaUnitDTO.setSpecItemRuleList(Lists.newArrayList());
            }
            teaUnitDTO.getSpecItemRuleList().add(specItemRuleDTO);
            selectedSpecItemCodeSet.add(teaUnitPO.getSpecItemCode());

            // 初始化规格，跟着Tea走
            SpecRuleDTO specRuleDTO = specRuleDTOMap.get(specItemPO.getSpecCode());
            if (specRuleDTO == null) {
                SpecPO specPO = specAccessor.selectOneByCode(specItemPO.getTenantCode(), specItemPO.getSpecCode());
                specRuleDTO = new SpecRuleDTO();
                specRuleDTO.setId(specPO.getId());
                specRuleDTO.setGmtCreated(specPO.getGmtCreated());
                specRuleDTO.setGmtModified(specPO.getGmtModified());
                specRuleDTO.setSpecCode(specPO.getSpecCode());
                specRuleDTO.setSpecName(specPO.getSpecName());
                List<SpecItemPO> specItemPOList = specItemAccessor.selectList(teaUnitPO.getTenantCode(),
                        specRuleDTO.getSpecCode());
                for (SpecItemPO po : specItemPOList) {
                    SpecItemRuleDTO dto = new SpecItemRuleDTO();
                    dto.setId(po.getId());
                    dto.setGmtCreated(po.getGmtCreated());
                    dto.setGmtModified(po.getGmtModified());
                    dto.setSpecItemCode(po.getSpecItemCode());
                    dto.setSpecItemName(po.getSpecItemName());
                    dto.setOuterSpecItemCode(po.getOuterSpecItemCode());
                    if (specRuleDTO.getSpecItemRuleList() == null) {
                        specRuleDTO.setSpecItemRuleList(Lists.newArrayList());
                    }
                    specRuleDTO.getSpecItemRuleList().add(dto);
                }
                specRuleDTOMap.put(specItemPO.getSpecCode(), specRuleDTO);
            }

            // 初始化物料，跟着TeaUnit走
            if (CollectionUtils.isEmpty(teaUnitDTO.getToppingAdjustRuleList())) {
                List<ToppingAdjustRulePO> toppingAdjustRulePOList = toppingAdjustRuleAccessor.selectList(
                        teaUnitPO.getTenantCode(), teaDTO.getTeaCode(), teaUnitDTO.getTeaUnitCode());
                List<ToppingAdjustRuleDTO> toppingAdjustRuleDTOList = Lists.newArrayList();
                for (ToppingAdjustRulePO toppingAdjustRulePO : toppingAdjustRulePOList) {
                    ToppingAdjustRuleDTO toppingAdjustRuleDTO = new ToppingAdjustRuleDTO();
                    toppingAdjustRuleDTO.setId(toppingAdjustRulePO.getId());
                    toppingAdjustRuleDTO.setGmtCreated(toppingAdjustRulePO.getGmtCreated());
                    toppingAdjustRuleDTO.setGmtModified(toppingAdjustRulePO.getGmtModified());
                    toppingAdjustRuleDTO.setStepIndex(toppingAdjustRulePO.getStepIndex());
                    toppingAdjustRuleDTO.setToppingCode(toppingAdjustRulePO.getToppingCode());
                    toppingAdjustRuleDTO.setBaseAmount(toppingAdjustRulePO.getBaseAmount());
                    toppingAdjustRuleDTO.setAdjustMode(toppingAdjustRulePO.getAdjustMode());
                    toppingAdjustRuleDTO.setAdjustUnit(toppingAdjustRulePO.getAdjustUnit());
                    toppingAdjustRuleDTO.setAdjustAmount(toppingAdjustRulePO.getAdjustAmount());
                    toppingAdjustRuleDTO.setActualAmount(toppingAdjustRulePO.getActualAmount());

                    ToppingPO toppingPO = toppingAccessor.selectOneByCode(
                            teaUnitPO.getTenantCode(), toppingAdjustRulePO.getToppingCode());
                    toppingAdjustRuleDTO.setToppingName(toppingPO.getToppingName());
                    toppingAdjustRuleDTO.setToppingTypeCode(toppingPO.getToppingTypeCode());
                    toppingAdjustRuleDTO.setMeasureUnit(toppingPO.getMeasureUnit());
                    toppingAdjustRuleDTO.setState(toppingPO.getState());
                    toppingAdjustRuleDTOList.add(toppingAdjustRuleDTO);

                    ToppingBaseRuleDTO toppingBaseRuleDTO = topppingBaseRuleDTOMap.get(
                            toppingAdjustRulePO.getToppingCode());
                    if (toppingBaseRuleDTO == null) {
                        toppingBaseRuleDTO = new ToppingBaseRuleDTO();
                        toppingBaseRuleDTO.setToppingCode(toppingPO.getToppingCode());
                        toppingBaseRuleDTO.setToppingName(toppingPO.getToppingName());
                        toppingBaseRuleDTO.setToppingTypeCode(toppingPO.getToppingTypeCode());
                        toppingBaseRuleDTO.setMeasureUnit(toppingPO.getMeasureUnit());
                        toppingBaseRuleDTO.setState(toppingPO.getState());
                        toppingBaseRuleDTO.setStepIndex(toppingAdjustRulePO.getStepIndex());
                        toppingBaseRuleDTO.setBaseAmount(toppingAdjustRulePO.getBaseAmount());
                        topppingBaseRuleDTOMap.put(toppingAdjustRulePO.getToppingCode(), toppingBaseRuleDTO);
                    }
                }
                teaUnitDTO.setToppingAdjustRuleList(toppingAdjustRuleDTOList);
            }
        }

        List<TeaUnitDTO> teaUnitDTOList = Lists.newArrayList();
        for (Map.Entry<String, TeaUnitDTO> entry : teaUnitDTOMap.entrySet()) {
            teaUnitDTOList.add(entry.getValue());
        }
        teaDTO.setTeaUnitList(teaUnitDTOList);

        List<ToppingBaseRuleDTO> toppingBaseRuleDTOList = Lists.newArrayList();
        for (Map.Entry<String, ToppingBaseRuleDTO> entry : topppingBaseRuleDTOMap.entrySet()) {
            toppingBaseRuleDTOList.add(entry.getValue());
        }
        Map<Integer, List<ToppingBaseRuleDTO>> actStepMap = Maps.newHashMap();
        for (ToppingBaseRuleDTO toppingBaseRuleDTO : toppingBaseRuleDTOList) {
            List<ToppingBaseRuleDTO> list = actStepMap.get(toppingBaseRuleDTO.getStepIndex());
            if (list == null) {
                list = Lists.newArrayList();
                actStepMap.put(toppingBaseRuleDTO.getStepIndex(), list);
            }
            list.add(toppingBaseRuleDTO);
        }
        List<ActStepDTO> actStepList = Lists.newArrayList();
        for (Map.Entry<Integer, List<ToppingBaseRuleDTO>> entry : actStepMap.entrySet()) {
            ActStepDTO actStepDTO = new ActStepDTO();
            actStepDTO.setStepIndex(entry.getKey());
            actStepDTO.setToppingBaseRuleList(entry.getValue());
            actStepList.add(actStepDTO);
        }
        teaDTO.setActStepList(actStepList);

        List<SpecRuleDTO> specRuleList = Lists.newArrayList();
        for (Map.Entry<String, SpecRuleDTO> entry : specRuleDTOMap.entrySet()) {
            SpecRuleDTO specRuleDTO = entry.getValue();
            for (SpecItemRuleDTO specItemRuleDTO : specRuleDTO.getSpecItemRuleList()) {
                if (selectedSpecItemCodeSet.contains(specItemRuleDTO.getSpecItemCode())) {
                    specItemRuleDTO.setSelected(1);
                }
            }
            specRuleList.add(specRuleDTO);
        }
        teaDTO.setSpecRuleList(specRuleList);
    }

    private TeaPO convertToTeaPO(TeaPutRequest request) {
        if (request == null) {
            return null;
        }

        TeaPO po = new TeaPO();
        po.setTeaCode(request.getTeaCode());
        po.setTeaName(request.getTeaName());
        po.setOuterTeaCode(request.getOuterTeaCode());
        po.setState(request.getState());
        po.setTeaTypeCode(request.getTeaTypeCode());
        po.setComment(request.getComment());
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        return po;
    }

    private List<TeaUnitPO> convertToTeaUnitPO(TeaPutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getTeaUnitList())) {
            return null;
        }

        List<TeaUnitPO> teaUnitPOList = Lists.newArrayList();
        List<TeaUnitPutRequest> teaUnitPutRequestList = request.getTeaUnitList();
        for (TeaUnitPutRequest teaUnitPutRequest : teaUnitPutRequestList) {
            List<SpecItemRulePutRequest> specItemRulePutRequestList = teaUnitPutRequest.getSpecItemRuleList();
            for (SpecItemRulePutRequest specItemRulePutRequest : specItemRulePutRequestList) {
                TeaUnitPO po = new TeaUnitPO();
                po.setTenantCode(request.getTenantCode());
                po.setTeaCode(request.getTeaCode());
                po.setTeaUnitCode(teaUnitPutRequest.getTeaUnitCode());
                po.setTeaUnitName(teaUnitPutRequest.getTeaUnitName());
                po.setSpecItemCode(specItemRulePutRequest.getSpecItemCode());
                teaUnitPOList.add(po);
            }
        }
        return teaUnitPOList;
    }

    private List<ToppingAdjustRulePO> convertToToppingAdjustRulePO(TeaPutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getTeaUnitList())) {
            return null;
        }

        List<ToppingAdjustRulePO> toppingAdjustRulePOList = Lists.newArrayList();
        List<TeaUnitPutRequest> teaUnitPutRequestList = request.getTeaUnitList();
        for (TeaUnitPutRequest teaUnitPutRequest : teaUnitPutRequestList) {
            List<ToppingAdjustRulePutRequest> toppingAdjustRuleList = teaUnitPutRequest.getToppingAdjustRuleList();
            for (ToppingAdjustRulePutRequest toppingAdjustRulePutRequest : toppingAdjustRuleList) {
                ToppingAdjustRulePO po = new ToppingAdjustRulePO();
                po.setTenantCode(request.getTenantCode());
                po.setTeaCode(request.getTeaCode());
                po.setTeaUnitCode(teaUnitPutRequest.getTeaUnitCode());
                po.setStepIndex(toppingAdjustRulePutRequest.getStepIndex());
                po.setToppingCode(toppingAdjustRulePutRequest.getToppingCode());
                po.setBaseAmount(toppingAdjustRulePutRequest.getBaseAmount());
                po.setAdjustMode(toppingAdjustRulePutRequest.getAdjustMode());
                po.setAdjustUnit(toppingAdjustRulePutRequest.getAdjustUnit());
                po.setAdjustAmount(toppingAdjustRulePutRequest.getAdjustAmount());
                po.setActualAmount(toppingAdjustRulePutRequest.getActualAmount());
                toppingAdjustRulePOList.add(po);
            }
        }
        return toppingAdjustRulePOList;
    }
}
