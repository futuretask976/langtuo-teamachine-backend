package com.langtuo.teamachine.biz.service.impl.drink;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.*;
import com.langtuo.teamachine.api.model.drink.*;
import com.langtuo.teamachine.api.request.drink.*;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.drink.SpecMgtService;
import com.langtuo.teamachine.api.service.drink.TeaMgtService;
import com.langtuo.teamachine.api.service.drink.ToppingMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.excel.*;
import com.langtuo.teamachine.biz.service.util.ExcelUtils;
import com.langtuo.teamachine.dao.accessor.drink.*;
import com.langtuo.teamachine.dao.po.drink.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.TeaMachineResult.*;

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
    private SpecMgtService specMgtService;

    @Resource
    private ToppingMgtService toppingMgtService;

    @Override
    public TeaMachineResult<TeaDTO> getByCode(String tenantCode, String teaCode) {
        TeaMachineResult<TeaDTO> teaMachineResult;
        try {
            TeaPO po = teaAccessor.selectOneByCode(tenantCode, teaCode);
            TeaDTO dto = convertToTeaDTO(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<TeaDTO> getByName(String tenantCode, String teaName) {
        TeaMachineResult<TeaDTO> teaMachineResult;
        try {
            TeaPO po = teaAccessor.selectOneByName(tenantCode, teaName);
            TeaDTO dto = convertToTeaDTO(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
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
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
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
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(TeaPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaPO teaPO = convertToTeaPO(request);
        List<ToppingBaseRulePO> toppingBaseRulePOList = convertToToppingBaseRulePO(request);
        List<TeaUnitPO> teaUnitPOList = convertToTeaUnitPO(request);
        List<ToppingAdjustRulePO> toppingAdjustRulePOList = convertToToppingAdjustRulePO(request);


        TeaMachineResult<Void> teaMachineResult;
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

            int deleted4ToppingBaseRule = toppingBaseRuleAccessor.delete(teaPO.getTenantCode(), teaPO.getTeaCode());
            toppingBaseRulePOList.forEach(item -> {
                int inserted4ToppingAdjustRule = toppingBaseRuleAccessor.insert(item);
            });

            int deleted4ToppingAdjustRule = toppingAdjustRuleAccessor.delete(teaPO.getTenantCode(), teaPO.getTeaCode());
            toppingAdjustRulePOList.forEach(item -> {
                int inserted4ToppingAdjustRule = toppingAdjustRuleAccessor.insert(item);
            });

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String teaCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted4Tea = teaAccessor.delete(tenantCode, teaCode);
            int deleted4TeaUnit = teaUnitAccessor.delete(tenantCode, teaCode);
            int deleted4ToppingBaseRule = toppingBaseRuleAccessor.delete(tenantCode, teaCode);
            int deleted4ToppingAdjustRule = toppingAdjustRuleAccessor.delete(tenantCode, teaCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Integer> countByTeaTypeCode(String tenantCode, String teaTypeCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(teaTypeCode)) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaMachineResult<Integer> teaMachineResult;
        try {
            int cnt = teaAccessor.countByTeaTypeCode(tenantCode, teaTypeCode);
            teaMachineResult = TeaMachineResult.success(cnt);
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<XSSFWorkbook> exportByExcel(String tenantCode) {
        // 获取数据
        List<TeaExcel> teaExcelList = fetchData4Export(tenantCode);

        // 创建一个新的工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 创建一个工作表
        Sheet sheet = workbook.createSheet(BizConsts.SHEET_NAME_4_TEA_EXPORT);
        // 创建标题行（0基索引）
        Row row = sheet.createRow(BizConsts.ROW_NUM_4_TITLE);
        // 创建单元格并设置值
        for (int i = 0; i < BizConsts.TITLE_LIST_4_TEA_EXPORT.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(BizConsts.TITLE_LIST_4_TEA_EXPORT.get(i));
        }

        int rowStartIndex4Tea = BizConsts.ROW_START_NUM_4_TEA;
        for (TeaExcel teaExcel : teaExcelList) {
            TeaInfoExcel teaInfoExcel = teaExcel.getTeaInfoExcel();
            int size4BaseRuleList = teaExcel.getToppingBaseRuleExcelList().size();
            int size4AdjustRuleList = teaExcel.getToppingAdjustRuleExcelList().size();

            Row dataRow = ExcelUtils.createRowIfAbsent(sheet, rowStartIndex4Tea);
            int columnNum = BizConsts.COL_START_NUM_4_TEA_INFO;
            // 添加茶品编码
            Cell cell4TeaCode = dataRow.createCell(columnNum++);
            cell4TeaCode.setCellValue(teaInfoExcel.getTeaCode());
            addMergedRegion4RowRange(sheet, cell4TeaCode, size4AdjustRuleList);
            // 添加茶品名称
            Cell cell4TeaName = dataRow.createCell(columnNum++);
            cell4TeaName.setCellValue(teaInfoExcel.getTeaName());
            addMergedRegion4RowRange(sheet, cell4TeaName, size4AdjustRuleList);
            // 添加外部茶品编码
            Cell cell4OuterTeaCode = dataRow.createCell(columnNum++);
            cell4OuterTeaCode.setCellValue(teaInfoExcel.getOuterTeaCode());
            addMergedRegion4RowRange(sheet, cell4OuterTeaCode, size4AdjustRuleList);
            // 添加茶品状态
            Cell cell4State = dataRow.createCell(columnNum++);
            cell4State.setCellValue(teaInfoExcel.getState() == BizConsts.STATE_DISABLED
                    ? BizConsts.STATE_DISABLED_LABEL : BizConsts.STATE_ENABLED_LABEL);
            addMergedRegion4RowRange(sheet, cell4State, size4AdjustRuleList);
            // 添加图片茶品类型
            Cell cell4TeaType = dataRow.createCell(columnNum++);
            cell4TeaType.setCellValue(teaInfoExcel.getTeaTypeCode());
            addMergedRegion4RowRange(sheet, cell4TeaType, size4AdjustRuleList);
            // 添加图片链接
            Cell cell4ImgLink = dataRow.createCell(columnNum++);
            cell4ImgLink.setCellValue(teaInfoExcel.getImgLink());
            addMergedRegion4RowRange(sheet, cell4ImgLink, size4AdjustRuleList);

            List<ToppingBaseRuleExcel> toppingBaseRuleExcelList = teaExcel.getToppingBaseRuleExcelList();
            int rowRange4BaseRule = size4AdjustRuleList / size4BaseRuleList;
            int rowIndex4BaseRule = rowStartIndex4Tea;
            for (ToppingBaseRuleExcel toppingBaseRuleExcel : toppingBaseRuleExcelList) {
                Row row4BaseRule = ExcelUtils.createRowIfAbsent(sheet, rowIndex4BaseRule);
                int columnNum4BaseRule = columnNum;
                // 添加步骤序号
                Cell cell4StepIndex = row4BaseRule.createCell(columnNum4BaseRule++);
                cell4StepIndex.setCellValue(toppingBaseRuleExcel.getStepIndex());
                addMergedRegion4RowRange(sheet, cell4StepIndex, rowRange4BaseRule);
                // 添加物料名称
                Cell cell4ToppingName = row4BaseRule.createCell(columnNum4BaseRule++);
                cell4ToppingName.setCellValue(toppingBaseRuleExcel.getToppingName());
                addMergedRegion4RowRange(sheet, cell4ToppingName, rowRange4BaseRule);
                // 添加基础用量
                Cell cell4BaseAmount = row4BaseRule.createCell(columnNum4BaseRule++);
                cell4BaseAmount.setCellValue(toppingBaseRuleExcel.getBaseAmount());
                addMergedRegion4RowRange(sheet, cell4BaseAmount, rowRange4BaseRule);

                rowIndex4BaseRule = row4BaseRule.getRowNum() + rowRange4BaseRule;
            }

            List<TeaUnitExcel> teaUnitExcelList = teaExcel.getTeaUnitExcelList();
            int rowRange4Unit = teaExcel.getToppingAdjustRuleExcelList().size() / teaExcel.getTeaUnitExcelList().size();
            int rowIndex4Unit = rowStartIndex4Tea;
            for (TeaUnitExcel teaUnitExcel : teaUnitExcelList) {
                Row row4TeaUnit = ExcelUtils.createRowIfAbsent(sheet, rowIndex4Unit);
                int columnIndex4TeaUnit = BizConsts.COL_START_NUM_4_UNIT;

                // 添加unit名称
                Cell cell4UnitName = row4TeaUnit.createCell(columnIndex4TeaUnit++);
                cell4UnitName.setCellValue(teaUnitExcel.getTeaUnitName());
                addMergedRegion4RowRange(sheet, cell4UnitName, rowRange4Unit);

                rowIndex4Unit = rowIndex4Unit + rowRange4Unit;
            }

            List<ToppingAdjustRuleExcel> toppingAdjustRuleExcelList = teaExcel.getToppingAdjustRuleExcelList();
            int rowIndex4AdjustRule = rowStartIndex4Tea;
            for (ToppingAdjustRuleExcel toppingAdjustRuleExcel : toppingAdjustRuleExcelList) {
                Row row4AdjustRule = ExcelUtils.createRowIfAbsent(sheet, rowIndex4AdjustRule++);
                int columnIndex4AdjustRule = BizConsts.COL_START_NUM_4_ADJUST_RULE;
                // 添加步骤序号
                Cell cell4StepIndex = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell4StepIndex.setCellValue(toppingAdjustRuleExcel.getStepIndex());
                // 添加物料名称
                Cell cell4ToppingName = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell4ToppingName.setCellValue(toppingAdjustRuleExcel.getToppingName());
                // 添加调整类型
                Cell cell4AdjustType = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell4AdjustType.setCellValue(
                        toppingAdjustRuleExcel.getAdjustType() == BizConsts.TOPPING_ADJUST_TYPE_REDUCE
                                ? BizConsts.TOPPING_ADJUST_TYPE_REDUCE_LABEL
                                        : BizConsts.TOPPING_ADJUST_TYPE_INCRESE_LABEL);
                // 添加调整模式
                Cell cell4AdjustMode = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell4AdjustMode.setCellValue(
                        toppingAdjustRuleExcel.getAdjustMode() == BizConsts.TOPPING_ADJUST_MODE_FIX
                                ? BizConsts.TOPPING_ADJUST_MODE_FIX_LABEL
                                        : BizConsts.TOPPING_ADJUST_MODE_PER_LABEL);
                // 添加调整数量
                Cell cell4AdjustAmount = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell4AdjustAmount.setCellValue(toppingAdjustRuleExcel.getAdjustAmount());
            }

            rowStartIndex4Tea = rowStartIndex4Tea + size4AdjustRuleList;
        }

        return TeaMachineResult.success(workbook);
    }

    private void addMergedRegion4RowRange(Sheet sheet, Cell cell, int rowRange) {
        ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(),
                cell.getRowIndex() + rowRange - 1, cell.getColumnIndex(), cell.getColumnIndex());
    }

    private List<TeaExcel> fetchData4Export(String tenantCode) {
        List<TeaExcel> teaExcelList = Lists.newArrayList();
        List<TeaPO> teaPOList = teaAccessor.selectList(tenantCode);
        for (TeaPO teaPO : teaPOList) {
            TeaExcel teaExcel = new TeaExcel();
            teaExcel.setTeaInfoExcel(convertToTeaInfoExcel(teaPO));
            teaExcelList.add(teaExcel);

            List<ToppingBaseRulePO> toppingBaseRulePOList = toppingBaseRuleAccessor.selectList(teaPO.getTenantCode(),
                    teaPO.getTeaCode());
            teaExcel.setToppingBaseRuleExcelList(convertToToppingBaseRuleExcel(toppingBaseRulePOList));

            List<TeaUnitPO> teaUnitPOList = filterTeaUnitPO(teaUnitAccessor.selectList(teaPO.getTenantCode(),
                    teaPO.getTeaCode()));
            teaExcel.setTeaUnitExcelList(convertToTeaUnitExcel(teaUnitPOList));

            for (TeaUnitExcel teaUnitExcel : teaExcel.getTeaUnitExcelList()) {
                List<ToppingAdjustRulePO> toppingAdjustRulePOList = toppingAdjustRuleAccessor.selectList(
                        teaPO.getTenantCode(), teaPO.getTeaCode(), teaUnitExcel.getTeaUnitCode());
                teaExcel.addAll(convertToToppingAdjustRuleExcel(toppingAdjustRulePOList));
            }
        }
        return teaExcelList;
    }

    private static List<TeaUnitPO> filterTeaUnitPO(List<TeaUnitPO> teaUnitPOList) {
        Set<TeaUnitPO> result = Sets.newHashSet();
        for (TeaUnitPO teaUnitPO : teaUnitPOList) {
            result.add(teaUnitPO);
        }
        return result.stream().collect(Collectors.toList());
    }

    private List<ToppingAdjustRuleExcel> convertToToppingAdjustRuleExcel(List<ToppingAdjustRulePO> list) {
        List<ToppingAdjustRuleExcel> resultList = com.google.common.collect.Lists.newArrayList();
        for (ToppingAdjustRulePO toppingAdjustRulePO : list) {
            ToppingAdjustRuleExcel toppingAdjustRuleExcel = new ToppingAdjustRuleExcel();
            toppingAdjustRuleExcel.setStepIndex(toppingAdjustRulePO.getStepIndex());
            toppingAdjustRuleExcel.setAdjustMode(toppingAdjustRulePO.getAdjustMode());
            toppingAdjustRuleExcel.setAdjustType(toppingAdjustRulePO.getAdjustType());
            toppingAdjustRuleExcel.setAdjustAmount(toppingAdjustRulePO.getAdjustAmount());

            ToppingDTO toppingDTO = getModel(toppingMgtService.getByCode(toppingAdjustRulePO.getTenantCode(),
                    toppingAdjustRulePO.getToppingCode()));
            if (toppingDTO != null) {
                toppingAdjustRuleExcel.setToppingName(toppingDTO.getToppingName());
            }

            resultList.add(toppingAdjustRuleExcel);
        }
        return resultList;
    }

    private List<TeaUnitExcel> convertToTeaUnitExcel(List<TeaUnitPO> list) {
        Set<TeaUnitExcel> resultSet = Sets.newHashSet();
        for (TeaUnitPO teaUnitPO : list) {
            TeaUnitExcel teaUnitExcel = new TeaUnitExcel();
            teaUnitExcel.setTeaUnitCode(teaUnitPO.getTeaUnitCode());
            teaUnitExcel.setTeaUnitName(teaUnitPO.getTeaUnitName());
            resultSet.add(teaUnitExcel);
        }
        return resultSet.stream().collect(Collectors.toList());
    }

    private List<ToppingBaseRuleExcel> convertToToppingBaseRuleExcel(List<ToppingBaseRulePO> list) {
        List<ToppingBaseRuleExcel> resultList = Lists.newArrayList();
        for (ToppingBaseRulePO toppingBaseRulePO : list) {
            ToppingBaseRuleExcel toppingBaseRuleExcel = new ToppingBaseRuleExcel();
            toppingBaseRuleExcel.setStepIndex(toppingBaseRulePO.getStepIndex());
            toppingBaseRuleExcel.setBaseAmount(toppingBaseRulePO.getBaseAmount());

            ToppingDTO toppingDTO = getModel(toppingMgtService.getByCode(toppingBaseRulePO.getTenantCode(),
                    toppingBaseRulePO.getToppingCode()));
            if (toppingDTO != null) {
                toppingBaseRuleExcel.setToppingName(toppingDTO.getToppingName());
            }

            resultList.add(toppingBaseRuleExcel);
        }
        return resultList;
    }

    private TeaInfoExcel convertToTeaInfoExcel(TeaPO teaPO) {
        TeaInfoExcel teaInfoExcel = new TeaInfoExcel();
        teaInfoExcel.setTeaCode(teaPO.getTeaCode());
        teaInfoExcel.setTeaName(teaPO.getTeaName());
        teaInfoExcel.setTeaTypeCode(teaPO.getTeaTypeCode());
        teaInfoExcel.setOuterTeaCode(teaPO.getOuterTeaCode());
        teaInfoExcel.setImgLink(teaPO.getImgLink());
        teaInfoExcel.setState(teaPO.getState());
        return teaInfoExcel;
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
        List<TeaUnitPO> teaUnitPOList = teaUnitAccessor.selectList(tenantCode, teaDTO.getTeaCode());
        if (CollectionUtils.isEmpty(teaUnitPOList)) {
            // teaUnitPOList 不应该为空
            return;
        }
        List<ToppingBaseRulePO> toppingBaseRulePOList = toppingBaseRuleAccessor.selectList(tenantCode,
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
        SpecDTO specDTO = getModel(specMgtService.getByCode(teaUnitPO.getTenantCode(), teaUnitPO.getSpecCode()));
        SpecRuleDTO specRuleDTO = new SpecRuleDTO();
        specRuleDTO.setSpecCode(specDTO.getSpecCode());
        specRuleDTO.setSpecName(specDTO.getSpecName());

        List<SpecItemDTO> specItemDTOList = getListModel(specMgtService.listSpecItemBySpecCode(
                teaUnitPO.getTenantCode(), teaUnitPO.getSpecCode()));
        for (SpecItemDTO ite : specItemDTOList) {
            SpecItemRuleDTO dto = new SpecItemRuleDTO();
            dto.setSpecCode(ite.getSpecCode());
            dto.setSpecItemCode(ite.getSpecItemCode());
            dto.setSpecItemName(ite.getSpecItemName());
            dto.setOuterSpecItemCode(ite.getOuterSpecItemCode());
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

        SpecItemDTO specItemDTO = getModel(specMgtService.getSpecItemBySpecItemCode(teaUnitPO.getTenantCode(),
                teaUnitPO.getSpecCode(), teaUnitPO.getSpecItemCode()));
        if (specItemDTO != null) {
            specItemRuleDTO.setSpecItemName(specItemDTO.getSpecItemName());
            specItemRuleDTO.setOuterSpecItemCode(specItemDTO.getOuterSpecItemCode());
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

        List<ToppingAdjustRulePO> toppingAdjustRulePOList = toppingAdjustRuleAccessor.selectList(
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

            ToppingDTO toppingDTO = getModel(toppingMgtService.getByCode(tenantCode,
                    toppingAdjustRulePO.getToppingCode()));
            if (toppingDTO != null) {
                toppingAdjustRuleDTO.setToppingName(toppingDTO.getToppingName());
                toppingAdjustRuleDTO.setMeasureUnit(toppingDTO.getMeasureUnit());
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

        ToppingDTO toppingDTO = getModel(toppingMgtService.getByCode(po.getTenantCode(), po.getToppingCode()));
        if (toppingDTO != null) {
            dto.setToppingName(toppingDTO.getToppingName());
            dto.setMeasureUnit(toppingDTO.getMeasureUnit());
            dto.setState(toppingDTO.getState());
        }
        return dto;
    }
}
