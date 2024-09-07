package com.langtuo.teamachine.biz.service.impl.drink;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import com.langtuo.teamachine.api.model.*;
import com.langtuo.teamachine.api.model.drink.*;
import com.langtuo.teamachine.api.request.drink.*;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.drink.TeaMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.excel.ExcelHandlerFactory;
import com.langtuo.teamachine.biz.service.excel.model.*;
import com.langtuo.teamachine.biz.service.util.ApiUtils;
import com.langtuo.teamachine.biz.service.util.ExcelUtils;
import com.langtuo.teamachine.dao.accessor.drink.*;
import com.langtuo.teamachine.dao.accessor.menu.SeriesTeaRelAccessor;
import com.langtuo.teamachine.dao.po.drink.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TeaMgtServiceImpl implements TeaMgtService {
    @Resource
    private TeaAccessor teaAccessor;

    @Resource
    private TeaUnitAccessor teaUnitAccessor;

    @Resource
    private ToppingAdjustRuleAccessor toppingAdjustRuleAccessor;

    @Resource
    private ToppingBaseRuleAccessor toppingBaseRuleAccessor;

    @Resource
    private SpecAccessor specAccessor;

    @Resource
    private SpecItemAccessor specItemAccessor;

    @Resource
    private ToppingAccessor toppingAccessor;

    @Resource
    private SeriesTeaRelAccessor seriesTeaRelAccessor;

    @Resource
    private ExcelHandlerFactory excelHandlerFactory;

    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<TeaDTO> getByCode(String tenantCode, String teaCode) {
        TeaMachineResult<TeaDTO> teaMachineResult;
        try {
            TeaPO po = teaAccessor.selectOneByTeaCode(tenantCode, teaCode);
            TeaDTO dto = convertToTeaDTO(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<TeaDTO> getByName(String tenantCode, String teaName) {
        TeaMachineResult<TeaDTO> teaMachineResult;
        try {
            TeaPO po = teaAccessor.selectOneByTeaName(tenantCode, teaName);
            TeaDTO dto = convertToTeaDTO(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<TeaDTO>> list(String tenantCode) {
        TeaMachineResult<List<TeaDTO>> teaMachineResult;
        try {
            List<TeaPO> teaPOList = teaAccessor.selectList(tenantCode);
            List<TeaDTO> teaDTOList = convertToTeaDTO(teaPOList);
            teaMachineResult = TeaMachineResult.success(teaDTOList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<TeaDTO>> search(String tenantName, String teaCode, String teaName,
            int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<TeaDTO>> teaMachineResult;
        try {
            PageInfo<TeaPO> pageInfo = teaAccessor.search(tenantName, teaCode, teaName,
                    pageNum, pageSize);
            List<TeaDTO> dtoList = convertToTeaDTO(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(TeaPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        TeaPO teaPO = convertToTeaPO(request);
        List<ToppingBaseRulePO> toppingBaseRulePOList = convertToToppingBaseRulePO(request);
        List<TeaUnitPO> teaUnitPOList = convertToTeaUnitPO(request);
        List<ToppingAdjustRulePO> toppingAdjustRulePOList = convertToToppingAdjustRulePO(request);


        TeaMachineResult<Void> teaMachineResult;
        try {
            TeaPO exist = teaAccessor.selectOneByTeaCode(teaPO.getTenantCode(), teaPO.getTeaCode());
            if (exist != null) {
                int updated = teaAccessor.update(teaPO);
            } else {
                int inserted = teaAccessor.insert(teaPO);
            }

            int deleted4TeaUnit = teaUnitAccessor.deleteByTeaCode(teaPO.getTenantCode(), teaPO.getTeaCode());
            teaUnitPOList.forEach(item -> {
                int inserted4TeaUnit = teaUnitAccessor.insert(item);
            });

            int deleted4ToppingBaseRule = toppingBaseRuleAccessor.deleteByTeaCode(teaPO.getTenantCode(), teaPO.getTeaCode());
            toppingBaseRulePOList.forEach(item -> {
                int inserted4ToppingAdjustRule = toppingBaseRuleAccessor.insert(item);
            });

            int deleted4ToppingAdjustRule = toppingAdjustRuleAccessor.deleteByTeaCode(teaPO.getTenantCode(), teaPO.getTeaCode());
            toppingAdjustRulePOList.forEach(item -> {
                int inserted4ToppingAdjustRule = toppingAdjustRuleAccessor.insert(item);
            });

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String teaCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int count = seriesTeaRelAccessor.countByTeaCode(tenantCode, teaCode);
            if (count == BizConsts.DB_SELECT_RESULT_EMPTY) {
                int deleted4Tea = teaAccessor.deleteByTeaCode(tenantCode, teaCode);
                int deleted4TeaUnit = teaUnitAccessor.deleteByTeaCode(tenantCode, teaCode);
                int deleted4ToppingBaseRule = toppingBaseRuleAccessor.deleteByTeaCode(tenantCode, teaCode);
                int deleted4ToppingAdjustRule = toppingAdjustRuleAccessor.deleteByTeaCode(tenantCode, teaCode);
                teaMachineResult = TeaMachineResult.success();
            } else {
                teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_TEA,
                    messageSource));
            }
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<XSSFWorkbook> exportByExcel(String tenantCode) {
        XSSFWorkbook xssfWorkbook = excelHandlerFactory.getExcelHandler(tenantCode)
                .getTeaHandler()
                .export(tenantCode);
        return TeaMachineResult.success(xssfWorkbook);
    }

    @Override
    public TeaMachineResult<Void> importByExcel(String tenantCode, XSSFWorkbook workbook) {
        if (StringUtils.isBlank(tenantCode) || workbook == null) {
            return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        List<TeaPutRequest> teaPutRequestList = excelHandlerFactory.getExcelHandler(tenantCode)
                .getTeaHandler()
                .upload(tenantCode, workbook);

        for (TeaPutRequest teaPutRequest : teaPutRequestList) {
            TeaMachineResult<Void> result = this.put(teaPutRequest);
            if (!result.isSuccess()) {
                return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
            }
        }
        return TeaMachineResult.success();
    }

    private static List<TeaUnitPart> convertToTeaUnitPart(List<TeaUnitPO> teaUnitPOList) {
        Set<TeaUnitPart> result = Sets.newHashSet();
        for (TeaUnitPO teaUnitPO : teaUnitPOList) {
            TeaUnitPart teaUnitPart = new TeaUnitPart();
            teaUnitPart.setTeaUnitName(teaUnitPO.getTeaUnitName());
            teaUnitPart.setTeaUnitCode(teaUnitPO.getTeaUnitCode());
            result.add(teaUnitPart);
        }
        return result.stream().collect(Collectors.toList());
    }

    private List<ToppingAdjustRulePart> convertToAdjustRulePart(
            Map<Integer, Map<String, ToppingBaseRulePO>> baseRuleMapByStepIndex,
            List<ToppingAdjustRulePO> adjustRulePOList) {
        List<ToppingAdjustRulePart> resultList = Lists.newArrayList();
        for (ToppingAdjustRulePO adjustRulePO : adjustRulePOList) {
            ToppingAdjustRulePart adjustRulePart = new ToppingAdjustRulePart();
            adjustRulePart.setStepIndex(adjustRulePO.getStepIndex());

            ToppingPO toppingPO = toppingAccessor.selectOneByToppingCode(adjustRulePO.getTenantCode(),
                    adjustRulePO.getToppingCode());
            if (toppingPO != null) {
                adjustRulePart.setToppingCode(toppingPO.getToppingCode());
                adjustRulePart.setToppingName(toppingPO.getToppingName());
            }

            Map<String, ToppingBaseRulePO> baseRuleMapByToppingCode = baseRuleMapByStepIndex.get(
                    adjustRulePO.getStepIndex());
            ToppingBaseRulePO baseRulePO = baseRuleMapByToppingCode.get(adjustRulePO.getToppingCode());
            int baseAmount = baseRulePO.getBaseAmount();
            int adjustAmount = adjustRulePO.getAdjustAmount();
            int adjustType = adjustRulePO.getAdjustType();
            int adjustMode = adjustRulePO.getAdjustMode();
            int actualAmount = 0;
            if (BizConsts.TOPPING_ADJUST_TYPE_REDUCE == adjustType) {
                if (BizConsts.TOPPING_ADJUST_MODE_FIX == adjustMode) {
                    actualAmount = baseAmount - adjustAmount;
                } else {
                    actualAmount = baseAmount * (BizConsts.NUM_ONE - adjustAmount);
                }
            } else {
                if (BizConsts.TOPPING_ADJUST_MODE_FIX == adjustMode) {
                    actualAmount = baseAmount + adjustAmount;
                } else {
                    actualAmount = baseAmount + (BizConsts.NUM_ONE - adjustAmount);
                }
            }
            adjustRulePart.setActualAmount(actualAmount < BizConsts.NUM_ZERO ? BizConsts.NUM_ZERO : actualAmount);

            resultList.add(adjustRulePart);
        }
        return resultList;
    }

    private List<TeaDTO> convertToTeaDTO(List<TeaPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<TeaDTO> list = poList.stream()
                .map(po -> convertToTeaDTO(po))
                .collect(Collectors.toList());
        return list;
    }

    private TeaDTO convertToTeaDTO(TeaPO po) {
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

        fillTeaDTO(po.getTenantCode(), dto);
        return dto;
    }

    private void fillTeaDTO(String tenantCode, TeaDTO teaDTO) {
        List<TeaUnitPO> teaUnitPOList = teaUnitAccessor.selectListByTeaCode(tenantCode, teaDTO.getTeaCode());
        if (CollectionUtils.isEmpty(teaUnitPOList)) {
            // teaUnitPOList 不应该为空
            return;
        }
        List<ToppingBaseRulePO> toppingBaseRulePOList = toppingBaseRuleAccessor.selectListByTeaCode(tenantCode,
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

    private Map<Integer, Map<String, ToppingBaseRulePO>> getToppingBaseRulePOMapByStepIndex(
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

    private List<TeaUnitDTO> getTeaUnitDTOList(Map<String, TeaUnitDTO> teaUnitDTOMap) {
        List<TeaUnitDTO> teaUnitDTOList = Lists.newArrayList();
        for (Map.Entry<String, TeaUnitDTO> entry : teaUnitDTOMap.entrySet()) {
            teaUnitDTOList.add(entry.getValue());
        }
        return teaUnitDTOList;
    }

    private List<SpecRuleDTO> getSpecRuleDTOList(Map<String, SpecRuleDTO> specRuleDTOMap,
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

    private List<ActStepDTO> getActStepDTOList(List<ToppingBaseRulePO> toppingBaseRulePOList) {
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

    private SpecRuleDTO getSpecRuleDTO(TeaUnitPO teaUnitPO) {
        SpecPO specPO = specAccessor.selectOneBySpecCode(teaUnitPO.getTenantCode(), teaUnitPO.getSpecCode());
        SpecRuleDTO specRuleDTO = new SpecRuleDTO();
        specRuleDTO.setSpecCode(specPO.getSpecCode());
        specRuleDTO.setSpecName(specPO.getSpecName());

        List<SpecItemPO> specItemPOList = specItemAccessor.selectListBySpecCode(
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

    private SpecItemRuleDTO getSpecItemRuleDTO(TeaUnitPO teaUnitPO) {
        SpecItemRuleDTO specItemRuleDTO = new SpecItemRuleDTO();
        specItemRuleDTO.setSpecCode(teaUnitPO.getSpecCode());
        specItemRuleDTO.setSpecItemCode(teaUnitPO.getSpecItemCode());

        SpecItemPO specItemPO = specItemAccessor.selectOneBySpecItemCode(teaUnitPO.getTenantCode(),
                teaUnitPO.getSpecItemCode());
        if (specItemPO != null) {
            specItemRuleDTO.setSpecItemName(specItemPO.getSpecItemName());
            specItemRuleDTO.setOuterSpecItemCode(specItemPO.getOuterSpecItemCode());
        }
        return specItemRuleDTO;
    }

    private TeaUnitDTO getTeaUnitDTO(TeaUnitPO teaUnitPO,
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

    private List<ToppingAdjustRuleDTO> getToppingAdjustRuleDTOList(String tenantCode, String teaCode,
            String teaUnitCode, Map<Integer, Map<String, ToppingBaseRulePO>> toppingBaseRulePOMapByStepIndex) {
        List<ToppingAdjustRuleDTO> toppingAdjustRuleDTOList = Lists.newArrayList();

        List<ToppingAdjustRulePO> toppingAdjustRulePOList = toppingAdjustRuleAccessor.selectListByTeaUnitCode(
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

            ToppingPO toppingPO = toppingAccessor.selectOneByToppingCode(tenantCode, toppingAdjustRulePO.getToppingCode());
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
        po.setImgLink(request.getImgLink());
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
                po.setSpecCode(specItemRulePutRequest.getSpecCode());
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
                po.setAdjustType(toppingAdjustRulePutRequest.getAdjustType());
                po.setAdjustMode(toppingAdjustRulePutRequest.getAdjustMode());
                po.setAdjustAmount(toppingAdjustRulePutRequest.getAdjustAmount());
                toppingAdjustRulePOList.add(po);
            }
        }
        return toppingAdjustRulePOList;
    }

    private List<ToppingBaseRulePO> convertToToppingBaseRulePO(TeaPutRequest request) {
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

    private ToppingBaseRuleDTO convertToToppingBaseRulePO(ToppingBaseRulePO po) {
        if (po == null) {
            return null;
        }

        ToppingBaseRuleDTO dto = new ToppingBaseRuleDTO();
        dto.setStepIndex(po.getStepIndex());
        dto.setToppingCode(po.getToppingCode());
        dto.setBaseAmount(po.getBaseAmount());

        ToppingPO toppingPO = toppingAccessor.selectOneByToppingCode(po.getTenantCode(), po.getToppingCode());
        if (toppingPO != null) {
            dto.setToppingName(toppingPO.getToppingName());
            dto.setMeasureUnit(toppingPO.getMeasureUnit());
            dto.setState(toppingPO.getState());
        }
        return dto;
    }
}
