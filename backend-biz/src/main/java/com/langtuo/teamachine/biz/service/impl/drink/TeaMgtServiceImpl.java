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
import com.langtuo.teamachine.api.service.drink.TeaMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.excel.export.*;
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

    @Override
    public TeaMachineResult<TeaDTO> getByCode(String tenantCode, String teaCode) {
        TeaMachineResult<TeaDTO> teaMachineResult;
        try {
            TeaPO po = teaAccessor.selectOneByTeaCode(tenantCode, teaCode);
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
            TeaPO po = teaAccessor.selectOneByTeaName(tenantCode, teaName);
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
            int count = seriesTeaRelAccessor.countByTeaCode(tenantCode, teaCode);
            if (count == 0) {
                int deleted4Tea = teaAccessor.deleteByTeaCode(tenantCode, teaCode);
                int deleted4TeaUnit = teaUnitAccessor.deleteByTeaCode(tenantCode, teaCode);
                int deleted4ToppingBaseRule = toppingBaseRuleAccessor.deleteByTeaCode(tenantCode, teaCode);
                int deleted4ToppingAdjustRule = toppingAdjustRuleAccessor.deleteByTeaCode(tenantCode, teaCode);
                teaMachineResult = TeaMachineResult.success();
            } else {
                teaMachineResult = TeaMachineResult.error(ErrorEnum.BIZ_ERR_CANNOT_DELETE_USING_TEA);
            }
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
            TeaInfoPart teaInfoPart = teaExcel.getTeaInfoPart();
            int size4BaseRuleList = teaExcel.getToppingBaseRulePartList().size();
            int size4AdjustRuleList = teaExcel.getToppingAdjustRulePartList().size();

            Row dataRow = ExcelUtils.createRowIfAbsent(sheet, rowStartIndex4Tea);
            int columnNum = BizConsts.COL_START_NUM_4_TEA_INFO;
            // 添加茶品编码
            Cell cell4TeaCode = dataRow.createCell(columnNum++);
            cell4TeaCode.setCellValue(teaInfoPart.getTeaCode());
            addMergedRegion4RowRange(sheet, cell4TeaCode, size4AdjustRuleList);
            // 添加茶品名称
            Cell cell4TeaName = dataRow.createCell(columnNum++);
            cell4TeaName.setCellValue(teaInfoPart.getTeaName());
            addMergedRegion4RowRange(sheet, cell4TeaName, size4AdjustRuleList);
            // 添加外部茶品编码
            Cell cell4OuterTeaCode = dataRow.createCell(columnNum++);
            cell4OuterTeaCode.setCellValue(teaInfoPart.getOuterTeaCode());
            addMergedRegion4RowRange(sheet, cell4OuterTeaCode, size4AdjustRuleList);
            // 添加茶品状态
            Cell cell4State = dataRow.createCell(columnNum++);
            cell4State.setCellValue(teaInfoPart.getState() == BizConsts.STATE_DISABLED
                    ? BizConsts.STATE_DISABLED_LABEL : BizConsts.STATE_ENABLED_LABEL);
            addMergedRegion4RowRange(sheet, cell4State, size4AdjustRuleList);
            // 添加图片茶品类型
            Cell cell4TeaType = dataRow.createCell(columnNum++);
            cell4TeaType.setCellValue(teaInfoPart.getTeaTypeCode());
            addMergedRegion4RowRange(sheet, cell4TeaType, size4AdjustRuleList);
            // 添加图片链接
            Cell cell4ImgLink = dataRow.createCell(columnNum++);
            cell4ImgLink.setCellValue(teaInfoPart.getImgLink());
            addMergedRegion4RowRange(sheet, cell4ImgLink, size4AdjustRuleList);

            List<ToppingBaseRulePart> toppingBaseRulePartList = teaExcel.getToppingBaseRulePartList();
            int rowRange4BaseRule = size4AdjustRuleList / size4BaseRuleList;
            int rowIndex4BaseRule = rowStartIndex4Tea;
            for (ToppingBaseRulePart toppingBaseRulePart : toppingBaseRulePartList) {
                Row row4BaseRule = ExcelUtils.createRowIfAbsent(sheet, rowIndex4BaseRule);
                int columnNum4BaseRule = columnNum;
                // 添加步骤序号
                Cell cell4StepIndex = row4BaseRule.createCell(columnNum4BaseRule++);
                cell4StepIndex.setCellValue(toppingBaseRulePart.getStepIndex());
                addMergedRegion4RowRange(sheet, cell4StepIndex, rowRange4BaseRule);
                // 添加物料名称
                Cell cell4ToppingName = row4BaseRule.createCell(columnNum4BaseRule++);
                cell4ToppingName.setCellValue(toppingBaseRulePart.getToppingName());
                addMergedRegion4RowRange(sheet, cell4ToppingName, rowRange4BaseRule);
                // 添加基础用量
                Cell cell4BaseAmount = row4BaseRule.createCell(columnNum4BaseRule++);
                cell4BaseAmount.setCellValue(toppingBaseRulePart.getBaseAmount());
                addMergedRegion4RowRange(sheet, cell4BaseAmount, rowRange4BaseRule);

                rowIndex4BaseRule = row4BaseRule.getRowNum() + rowRange4BaseRule;
            }

            List<TeaUnitPart> teaUnitPartList = teaExcel.getTeaUnitPartList();
            int rowRange4Unit = teaExcel.getToppingAdjustRulePartList().size() / teaExcel.getTeaUnitPartList().size();
            int rowIndex4Unit = rowStartIndex4Tea;
            for (TeaUnitPart teaUnitPart : teaUnitPartList) {
                Row row4TeaUnit = ExcelUtils.createRowIfAbsent(sheet, rowIndex4Unit);
                int columnIndex4TeaUnit = BizConsts.COL_START_NUM_4_UNIT;

                // 添加unit名称
                Cell cell4UnitName = row4TeaUnit.createCell(columnIndex4TeaUnit++);
                cell4UnitName.setCellValue(teaUnitPart.getTeaUnitName());
                addMergedRegion4RowRange(sheet, cell4UnitName, rowRange4Unit);

                rowIndex4Unit = rowIndex4Unit + rowRange4Unit;
            }

            List<ToppingAdjustRulePart> toppingAdjustRulePartList = teaExcel.getToppingAdjustRulePartList();
            int rowIndex4AdjustRule = rowStartIndex4Tea;
            for (ToppingAdjustRulePart toppingAdjustRulePart : toppingAdjustRulePartList) {
                Row row4AdjustRule = ExcelUtils.createRowIfAbsent(sheet, rowIndex4AdjustRule++);
                int columnIndex4AdjustRule = BizConsts.COL_START_NUM_4_ADJUST_RULE;
                // 添加步骤序号
                Cell cell4StepIndex = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell4StepIndex.setCellValue(toppingAdjustRulePart.getStepIndex());
                // 添加物料名称
                Cell cell4ToppingName = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell4ToppingName.setCellValue(toppingAdjustRulePart.getToppingName());
                // 添加调整类型
                Cell cell4AdjustType = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell4AdjustType.setCellValue(
                        toppingAdjustRulePart.getAdjustType() == BizConsts.TOPPING_ADJUST_TYPE_REDUCE
                                ? BizConsts.TOPPING_ADJUST_TYPE_REDUCE_LABEL
                                        : BizConsts.TOPPING_ADJUST_TYPE_INCRESE_LABEL);
                // 添加调整模式
                Cell cell4AdjustMode = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell4AdjustMode.setCellValue(
                        toppingAdjustRulePart.getAdjustMode() == BizConsts.TOPPING_ADJUST_MODE_FIX
                                ? BizConsts.TOPPING_ADJUST_MODE_FIX_LABEL
                                        : BizConsts.TOPPING_ADJUST_MODE_PER_LABEL);
                // 添加调整数量
                Cell cell4AdjustAmount = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell4AdjustAmount.setCellValue(toppingAdjustRulePart.getAdjustAmount());
            }

            rowStartIndex4Tea = rowStartIndex4Tea + size4AdjustRuleList;
        }

        return TeaMachineResult.success(workbook);
    }

    @Override
    public TeaMachineResult<Void> parseUpload(String tenantCode, XSSFWorkbook workbook) {
        if (StringUtils.isBlank(tenantCode) || workbook == null) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        return TeaMachineResult.success();
    }



    private void addMergedRegion4RowRange(Sheet sheet, Cell cell, int rowRange) {
        ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + rowRange - 1,
                cell.getColumnIndex(), cell.getColumnIndex());
    }

    private List<TeaExcel> fetchData4Export(String tenantCode) {
        List<TeaExcel> teaExcelList = Lists.newArrayList();
        List<TeaPO> teaPOList = teaAccessor.selectList(tenantCode);
        for (TeaPO teaPO : teaPOList) {
            TeaExcel teaExcel = new TeaExcel();
            teaExcel.setTeaInfoPart(convertToTeaInfoExcel(teaPO));
            teaExcelList.add(teaExcel);

            List<ToppingBaseRulePO> toppingBaseRulePOList = toppingBaseRuleAccessor.selectListByTeaCode(teaPO.getTenantCode(),
                    teaPO.getTeaCode());
            teaExcel.setToppingBaseRulePartList(convertToToppingBaseRuleExcel(toppingBaseRulePOList));

            List<TeaUnitPO> teaUnitPOList = filterTeaUnitPO(teaUnitAccessor.selectListByTeaCode(teaPO.getTenantCode(),
                    teaPO.getTeaCode()));
            teaExcel.setTeaUnitPartList(convertToTeaUnitExcel(teaUnitPOList));

            for (TeaUnitPart teaUnitPart : teaExcel.getTeaUnitPartList()) {
                List<ToppingAdjustRulePO> toppingAdjustRulePOList = toppingAdjustRuleAccessor.selectListByTeaUnitCode(
                        teaPO.getTenantCode(), teaPO.getTeaCode(), teaUnitPart.getTeaUnitCode());
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

    private List<ToppingAdjustRulePart> convertToToppingAdjustRuleExcel(List<ToppingAdjustRulePO> list) {
        List<ToppingAdjustRulePart> resultList = com.google.common.collect.Lists.newArrayList();
        for (ToppingAdjustRulePO toppingAdjustRulePO : list) {
            ToppingAdjustRulePart toppingAdjustRulePart = new ToppingAdjustRulePart();
            toppingAdjustRulePart.setStepIndex(toppingAdjustRulePO.getStepIndex());
            toppingAdjustRulePart.setAdjustMode(toppingAdjustRulePO.getAdjustMode());
            toppingAdjustRulePart.setAdjustType(toppingAdjustRulePO.getAdjustType());
            toppingAdjustRulePart.setAdjustAmount(toppingAdjustRulePO.getAdjustAmount());

            ToppingPO toppingPO = toppingAccessor.selectOneByToppingCode(toppingAdjustRulePO.getTenantCode(),
                    toppingAdjustRulePO.getToppingCode());
            if (toppingPO != null) {
                toppingAdjustRulePart.setToppingName(toppingPO.getToppingName());
            }

            resultList.add(toppingAdjustRulePart);
        }
        return resultList;
    }

    private List<TeaUnitPart> convertToTeaUnitExcel(List<TeaUnitPO> list) {
        Set<TeaUnitPart> resultSet = Sets.newHashSet();
        for (TeaUnitPO teaUnitPO : list) {
            TeaUnitPart teaUnitPart = new TeaUnitPart();
            teaUnitPart.setTeaUnitCode(teaUnitPO.getTeaUnitCode());
            teaUnitPart.setTeaUnitName(teaUnitPO.getTeaUnitName());
            resultSet.add(teaUnitPart);
        }
        return resultSet.stream().collect(Collectors.toList());
    }

    private List<ToppingBaseRulePart> convertToToppingBaseRuleExcel(List<ToppingBaseRulePO> list) {
        List<ToppingBaseRulePart> resultList = Lists.newArrayList();
        for (ToppingBaseRulePO toppingBaseRulePO : list) {
            ToppingBaseRulePart toppingBaseRulePart = new ToppingBaseRulePart();
            toppingBaseRulePart.setStepIndex(toppingBaseRulePO.getStepIndex());
            toppingBaseRulePart.setBaseAmount(toppingBaseRulePO.getBaseAmount());

            ToppingPO toppingPO = toppingAccessor.selectOneByToppingCode(toppingBaseRulePO.getTenantCode(),
                    toppingBaseRulePO.getToppingCode());
            if (toppingPO != null) {
                toppingBaseRulePart.setToppingName(toppingPO.getToppingName());
            }

            resultList.add(toppingBaseRulePart);
        }
        return resultList;
    }

    private TeaInfoPart convertToTeaInfoExcel(TeaPO teaPO) {
        TeaInfoPart teaInfoPart = new TeaInfoPart();
        teaInfoPart.setTeaCode(teaPO.getTeaCode());
        teaInfoPart.setTeaName(teaPO.getTeaName());
        teaInfoPart.setTeaTypeCode(teaPO.getTeaTypeCode());
        teaInfoPart.setOuterTeaCode(teaPO.getOuterTeaCode());
        teaInfoPart.setImgLink(teaPO.getImgLink());
        teaInfoPart.setState(teaPO.getState());
        return teaInfoPart;
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
                teaUnitPO.getSpecCode(), teaUnitPO.getSpecItemCode());
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
