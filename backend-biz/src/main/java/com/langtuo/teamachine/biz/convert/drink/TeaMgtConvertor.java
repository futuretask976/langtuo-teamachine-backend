package com.langtuo.teamachine.biz.convert.drink;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.langtuo.teamachine.api.model.drink.*;
import com.langtuo.teamachine.api.request.drink.*;
import com.langtuo.teamachine.dao.accessor.drink.*;
import com.langtuo.teamachine.dao.po.drink.*;
import com.langtuo.teamachine.dao.util.SpringAccessorUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TeaMgtConvertor {
    public static List<TeaDTO> convertToTeaDTO(List<TeaPO> poList, boolean needDetail) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<TeaDTO> list = poList.stream()
                .map(po -> convertToTeaDTO(po, needDetail))
                .collect(Collectors.toList());
        return list;
    }

    public static TeaDTO convertToTeaDTO(TeaPO po, boolean needDetail) {
        if (po == null) {
            return null;
        }

        TeaDTO dto = new TeaDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTeaCode(po.getTeaCode());
        dto.setTeaName(po.getTeaName());
        dto.setState(po.getState());
        dto.setTeaTypeCode(po.getTeaTypeCode());
        dto.setOuterTeaCode(po.getOuterTeaCode());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setImgLink(po.getImgLink());

        if (needDetail) {
            fillTeaDTO(po.getTenantCode(), dto);
        }
        return dto;
    }

    public static void fillTeaDTO(String tenantCode, TeaDTO teaDTO) {
        TeaUnitAccessor teaUnitAccessor = SpringAccessorUtils.getTeaUnitAccessor();
        List<TeaUnitPO> teaUnitPOList = teaUnitAccessor.listByTeaCode(tenantCode, teaDTO.getTeaCode());
        if (CollectionUtils.isEmpty(teaUnitPOList)) {
            // teaUnitPOList 不应该为空
            return;
        }

        ToppingBaseRuleAccessor toppingBaseRuleAccessor = SpringAccessorUtils.getToppingBaseRuleAccessor();
        List<ToppingBaseRulePO> toppingBaseRulePOList = toppingBaseRuleAccessor.listByTeaCode(tenantCode,
                teaDTO.getTeaCode());
        if (CollectionUtils.isEmpty(toppingBaseRulePOList)) {
            // toppingBaseRulePOList 不应该为空
            return;
        }
        // 用于在构建 toppingAdjustRule 时，填充 baseAmount
        Map<Integer, Map<String, ToppingBaseRulePO>> toppingBaseRulePOMapByStepIndex =
                getToppingBaseRulePOMapByStepIndex(toppingBaseRulePOList);

        // tea 下有 3 个主要字段，分别是：teaUnitList，actStepList，specRuleList，这3个信息都存储在 tea_unit 表中
        // 用于构建 teaUnitList，存储在 tea_unit 表中，多行对应一个
        Map<String, TeaUnitDTO> teaUnitDTOMap = Maps.newHashMap();
        // 用于构建 specRuleList，冗余存储在 tea_unit 表中
        Map<String, SpecRuleDTO> specRuleDTOMap = Maps.newHashMap();
        // 用于构建已选择的规格项编码（即当前饮品有哪些规格），冗余存储在 tea_unit 表中
        Set<String> selectedSpecItemCodeSet = Sets.newHashSet();

        // 开始构建
        for (TeaUnitPO teaUnitPO : teaUnitPOList) {
            // 是 tea 的子项，表示当前茶品有哪些规格项组合可选，初始化 teaUnit，因为 tea_unit 表中单行表示一个规格项，所以需要先判断是否已经初始化过
            TeaUnitDTO teaUnitDTO = teaUnitDTOMap.get(teaUnitPO.getTeaUnitCode());
            if (teaUnitDTO == null) {
                teaUnitDTO = getTeaUnitDTO(teaUnitPO, toppingBaseRulePOMapByStepIndex);
                teaUnitDTOMap.put(teaUnitPO.getTeaUnitCode(), teaUnitDTO);
            }

            // 是 teaUnit 的子项，表示当前teaUnit下选择了哪些规格项，因为 tea_unit 表中单行表示一个规格项，所以都要初始化
            teaUnitDTO.addSpecItemRule(getSpecItemRuleDTO(teaUnitPO));
            selectedSpecItemCodeSet.add(teaUnitPO.getSpecItemCode());

            // 是 tea 的子项，表示当前茶品有哪些规格可选，因为 tea_unit 表中单行表示一个规格项，所以需要先判断是否已经初始化过
            SpecRuleDTO specRuleDTO = specRuleDTOMap.get(teaUnitPO.getSpecCode());
            if (specRuleDTO == null) {
                specRuleDTO = getSpecRuleDTO(teaUnitPO);
                specRuleDTOMap.putIfAbsent(teaUnitPO.getSpecCode(), specRuleDTO);
            }
        }

        // 构建teaUnitList
        teaDTO.setTeaUnitList(getTeaUnitDTOList(teaUnitDTOMap));

        // 构建actStepList
        teaDTO.setActStepList(getActStepDTOList(toppingBaseRulePOList));

        // 构建specRuleList
        teaDTO.setSpecRuleList(getSpecRuleDTOList(specRuleDTOMap, selectedSpecItemCodeSet));
    }

    public static Map<Integer, Map<String, ToppingBaseRulePO>> getToppingBaseRulePOMapByStepIndex(
            List<ToppingBaseRulePO> toppingBaseRulePOList) {
        Map<Integer, Map<String, ToppingBaseRulePO>> toppingBaseRulePOMapByStepIndex = Maps.newHashMap();
        for (ToppingBaseRulePO toppingBaseRulePO : toppingBaseRulePOList) {
            Map<String, ToppingBaseRulePO> toppingBaseRulePOMapByToppingCode = toppingBaseRulePOMapByStepIndex.get(
                    toppingBaseRulePO.getStepIndex());
            if (toppingBaseRulePOMapByToppingCode == null) {
                toppingBaseRulePOMapByToppingCode = Maps.newHashMap();
                toppingBaseRulePOMapByStepIndex.put(toppingBaseRulePO.getStepIndex(),
                        toppingBaseRulePOMapByToppingCode);
            }
            toppingBaseRulePOMapByToppingCode.put(toppingBaseRulePO.getToppingCode(), toppingBaseRulePO);
        }
        return toppingBaseRulePOMapByStepIndex;
    }

    public static List<TeaUnitDTO> getTeaUnitDTOList(Map<String, TeaUnitDTO> teaUnitDTOMap) {
        List<TeaUnitDTO> teaUnitDTOList = Lists.newArrayList();
        for (Map.Entry<String, TeaUnitDTO> entry : teaUnitDTOMap.entrySet()) {
            teaUnitDTOList.add(entry.getValue());
        }
        return teaUnitDTOList;
    }

    public static List<SpecRuleDTO> getSpecRuleDTOList(Map<String, SpecRuleDTO> specRuleDTOMap,
                                                       Set<String> selectedSpecItemCodeSet) {
        List<SpecRuleDTO> specRuleDTOList = Lists.newArrayList();
        for (Map.Entry<String, SpecRuleDTO> entry : specRuleDTOMap.entrySet()) {
            SpecRuleDTO specRuleDTO = entry.getValue();
            for (SpecItemRuleDTO specItemRuleDTO : specRuleDTO.getSpecItemRuleList()) {
                if (selectedSpecItemCodeSet.contains(specItemRuleDTO.getSpecItemCode())) {
                    specItemRuleDTO.setSelected(1);
                }
            }
            specRuleDTOList.add(specRuleDTO);
        }
        return specRuleDTOList;
    }

    public static List<ActStepDTO> getActStepDTOList(List<ToppingBaseRulePO> toppingBaseRulePOList) {
        Map<Integer, ActStepDTO> actStepDTOMap = Maps.newHashMap();
        for (ToppingBaseRulePO po : toppingBaseRulePOList) {
            ActStepDTO actStepDTO = actStepDTOMap.get(po.getStepIndex());
            if (actStepDTO == null) {
                actStepDTO = new ActStepDTO();
                actStepDTO.setStepIndex(po.getStepIndex());
                actStepDTO.setToppingBaseRuleList(Lists.newArrayList());
            }
            actStepDTO.addToppingBaseRule(convertToToppingBaseRulePO(po));
            actStepDTOMap.put(actStepDTO.getStepIndex(), actStepDTO);
        }

        List<ActStepDTO> actStepDTOList = Lists.newArrayList();
        for (Map.Entry<Integer, ActStepDTO> entry : actStepDTOMap.entrySet()) {
            actStepDTOList.add(entry.getValue());
        }
        return actStepDTOList;
    }

    public static SpecRuleDTO getSpecRuleDTO(TeaUnitPO teaUnitPO) {
        SpecAccessor specAccessor = SpringAccessorUtils.getSpecAccessor();
        SpecPO specPO = specAccessor.getBySpecCode(teaUnitPO.getTenantCode(), teaUnitPO.getSpecCode());
        SpecRuleDTO specRuleDTO = new SpecRuleDTO();
        specRuleDTO.setSpecCode(specPO.getSpecCode());
        specRuleDTO.setSpecName(specPO.getSpecName());

        SpecItemAccessor specItemAccessor = SpringAccessorUtils.getSpecItemAccessor();
        List<SpecItemPO> specItemPOList = specItemAccessor.listBySpecCode(
                teaUnitPO.getTenantCode(), teaUnitPO.getSpecCode());
        for (SpecItemPO specItemPO : specItemPOList) {
            SpecItemRuleDTO dto = new SpecItemRuleDTO();
            dto.setSpecCode(specItemPO.getSpecCode());
            dto.setSpecItemCode(specItemPO.getSpecItemCode());
            dto.setSpecItemName(specItemPO.getSpecItemName());
            dto.setOuterSpecItemCode(specItemPO.getOuterSpecItemCode());
            if (specRuleDTO.getSpecItemRuleList() == null) {
                specRuleDTO.setSpecItemRuleList(Lists.newArrayList());
            }
            specRuleDTO.getSpecItemRuleList().add(dto);
        }
        return specRuleDTO;
    }

    public static SpecItemRuleDTO getSpecItemRuleDTO(TeaUnitPO teaUnitPO) {
        SpecItemRuleDTO specItemRuleDTO = new SpecItemRuleDTO();
        specItemRuleDTO.setSpecCode(teaUnitPO.getSpecCode());
        specItemRuleDTO.setSpecItemCode(teaUnitPO.getSpecItemCode());

        SpecItemAccessor specItemAccessor = SpringAccessorUtils.getSpecItemAccessor();
        SpecItemPO specItemPO = specItemAccessor.getBySpecItemCode(teaUnitPO.getTenantCode(),
                teaUnitPO.getSpecItemCode());
        if (specItemPO != null) {
            specItemRuleDTO.setSpecItemName(specItemPO.getSpecItemName());
            specItemRuleDTO.setOuterSpecItemCode(specItemPO.getOuterSpecItemCode());
        }
        return specItemRuleDTO;
    }

    public static TeaUnitDTO getTeaUnitDTO(TeaUnitPO teaUnitPO,
                                           Map<Integer, Map<String, ToppingBaseRulePO>> toppingBaseRulePOMapByStepIndex) {
        // 初始化teaUnit信息
        TeaUnitDTO teaUnitDTO = new TeaUnitDTO();
        teaUnitDTO.setTeaUnitCode(teaUnitPO.getTeaUnitCode());
        teaUnitDTO.setTeaUnitName(teaUnitPO.getTeaUnitName());
        teaUnitDTO.setSpecItemRuleList(Lists.newArrayList());

        // 查询当前teaUnit对应的物料调整规则
        teaUnitDTO.setToppingAdjustRuleList(getToppingAdjustRuleDTOList(teaUnitPO.getTenantCode(),
                teaUnitPO.getTeaCode(), teaUnitDTO.getTeaUnitCode(), toppingBaseRulePOMapByStepIndex));
        return teaUnitDTO;
    }

    public static List<ToppingAdjustRuleDTO> getToppingAdjustRuleDTOList(String tenantCode, String teaCode,
                                                                         String teaUnitCode, Map<Integer, Map<String, ToppingBaseRulePO>> toppingBaseRulePOMapByStepIndex) {
        List<ToppingAdjustRuleDTO> toppingAdjustRuleDTOList = Lists.newArrayList();

        ToppingAdjustRuleAccessor toppingAdjustRuleAccessor = SpringAccessorUtils.getToppingAdjustRuleAccessor();
        List<ToppingAdjustRulePO> toppingAdjustRulePOList = toppingAdjustRuleAccessor.listByTeaUnitCode(
                tenantCode, teaCode, teaUnitCode);
        if (CollectionUtils.isEmpty(toppingAdjustRulePOList)) {
            return toppingAdjustRuleDTOList;
        }
        for (ToppingAdjustRulePO toppingAdjustRulePO : toppingAdjustRulePOList) {
            ToppingAdjustRuleDTO toppingAdjustRuleDTO = new ToppingAdjustRuleDTO();
            toppingAdjustRuleDTO.setStepIndex(toppingAdjustRulePO.getStepIndex());
            toppingAdjustRuleDTO.setToppingCode(toppingAdjustRulePO.getToppingCode());
            toppingAdjustRuleDTO.setAdjustType(toppingAdjustRulePO.getAdjustType());
            toppingAdjustRuleDTO.setAdjustMode(toppingAdjustRulePO.getAdjustMode());
            toppingAdjustRuleDTO.setAdjustAmount(toppingAdjustRulePO.getAdjustAmount());

            ToppingAccessor toppingAccessor = SpringAccessorUtils.getToppingAccessor();
            ToppingPO toppingPO = toppingAccessor.getByToppingCode(tenantCode, toppingAdjustRulePO.getToppingCode());
            if (toppingPO != null) {
                toppingAdjustRuleDTO.setToppingName(toppingPO.getToppingName());
                toppingAdjustRuleDTO.setMeasureUnit(toppingPO.getMeasureUnit());
            }

            Map<String, ToppingBaseRulePO> toppingBaseRulePOMapByToppingCode = toppingBaseRulePOMapByStepIndex.get(
                    toppingAdjustRulePO.getStepIndex());
            ToppingBaseRulePO toppingBaseRulePO = toppingBaseRulePOMapByToppingCode.get(
                    toppingAdjustRulePO.getToppingCode());
            toppingAdjustRuleDTO.setBaseAmount(toppingBaseRulePO.getBaseAmount());

            toppingAdjustRuleDTOList.add(toppingAdjustRuleDTO);
        }
        return toppingAdjustRuleDTOList;
    }

    public static TeaPO convertToTeaPO(TeaPutRequest request) {
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
        po.setImgLink(request.getImgLink());
        return po;
    }

    public static List<TeaUnitPO> convertToTeaUnitPO(TeaPutRequest request) {
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
                po.setSpecCode(specItemRulePutRequest.getSpecCode());
                po.setSpecItemCode(specItemRulePutRequest.getSpecItemCode());
                teaUnitPOList.add(po);
            }
        }
        return teaUnitPOList;
    }

    public static List<ToppingAdjustRulePO> convertToToppingAdjustRulePO(TeaPutRequest request) {
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
                po.setAdjustType(toppingAdjustRulePutRequest.getAdjustType());
                po.setAdjustMode(toppingAdjustRulePutRequest.getAdjustMode());
                po.setAdjustAmount(toppingAdjustRulePutRequest.getAdjustAmount());
                toppingAdjustRulePOList.add(po);
            }
        }
        return toppingAdjustRulePOList;
    }

    public static List<ToppingBaseRulePO> convertToToppingBaseRulePO(TeaPutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getTeaUnitList())) {
            return null;
        }

        List<ToppingBaseRulePO> toppingBaseRulePOList = Lists.newArrayList();
        List<ActStepPutRequest> actStepList = request.getActStepList();
        for (ActStepPutRequest actStepPutRequest : actStepList) {
            int stepIndex = actStepPutRequest.getStepIndex();
            List<ToppingBaseRulePutRequest> toppingBaseRuleList = actStepPutRequest.getToppingBaseRuleList();
            for (ToppingBaseRulePutRequest toppingBaseRulePutRequest : toppingBaseRuleList) {
                ToppingBaseRulePO toppingBaseRulePO = new ToppingBaseRulePO();
                toppingBaseRulePO.setTenantCode(request.getTenantCode());
                toppingBaseRulePO.setTeaCode(request.getTeaCode());
                toppingBaseRulePO.setStepIndex(stepIndex);
                toppingBaseRulePO.setToppingCode(toppingBaseRulePutRequest.getToppingCode());
                toppingBaseRulePO.setBaseAmount(toppingBaseRulePutRequest.getBaseAmount());
                toppingBaseRulePOList.add(toppingBaseRulePO);
            }
        }
        return toppingBaseRulePOList;
    }

    public static ToppingBaseRuleDTO convertToToppingBaseRulePO(ToppingBaseRulePO po) {
        if (po == null) {
            return null;
        }

        ToppingBaseRuleDTO dto = new ToppingBaseRuleDTO();
        dto.setStepIndex(po.getStepIndex());
        dto.setToppingCode(po.getToppingCode());
        dto.setBaseAmount(po.getBaseAmount());

        ToppingAccessor toppingAccessor = SpringAccessorUtils.getToppingAccessor();
        ToppingPO toppingPO = toppingAccessor.getByToppingCode(po.getTenantCode(), po.getToppingCode());
        if (toppingPO != null) {
            dto.setToppingName(toppingPO.getToppingName());
            dto.setMeasureUnit(toppingPO.getMeasureUnit());
            dto.setState(toppingPO.getState());
        }
        return dto;
    }
}
