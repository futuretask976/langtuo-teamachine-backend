package com.langtuo.teamachine.biz.service.excel.parser;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.langtuo.teamachine.api.request.drink.*;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.excel.model.TeaExcel;
import com.langtuo.teamachine.biz.service.excel.model.TeaInfoPart;
import com.langtuo.teamachine.biz.service.excel.model.TeaUnitPart;
import com.langtuo.teamachine.biz.service.excel.model.ToppingAdjustRulePart;
import com.langtuo.teamachine.biz.service.util.ExcelUtils;
import com.langtuo.teamachine.biz.service.util.SpringUtils;
import com.langtuo.teamachine.dao.po.drink.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultTeaParser implements TeaParser {
    @Override
    public XSSFWorkbook export(String tenantCode) {
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

            List<TeaUnitPart> teaUnitPartList = teaExcel.getTeaUnitPartList();
            int rowRange4Unit = teaExcel.getToppingAdjustRulePartList().size() / teaExcel.getTeaUnitPartList().size();
            int rowIndex4Unit = rowStartIndex4Tea;
            for (TeaUnitPart teaUnitPart : teaUnitPartList) {
                Row row4TeaUnit = ExcelUtils.createRowIfAbsent(sheet, rowIndex4Unit);
                int columnIndex4TeaUnit = BizConsts.COL_START_NUM_4_TEA_UNIT;
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
                // 添加物料编码
                Cell cell4ToppingCode = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell4ToppingCode.setCellValue(toppingAdjustRulePart.getToppingCode());
                // 添加物料名称
                Cell cell4ToppingName = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell4ToppingName.setCellValue(toppingAdjustRulePart.getToppingName());
                // 添加实际用量
                Cell cell4ActualAmount = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell4ActualAmount.setCellValue(toppingAdjustRulePart.getActualAmount());
            }

            rowStartIndex4Tea = rowStartIndex4Tea + size4AdjustRuleList;
        }
        return workbook;
    }

    @Override
    public List<TeaPutRequest> upload(String tenantCode, XSSFWorkbook workbook) {
        Sheet sheet = workbook.getSheetAt(BizConsts.SHEET_NUM_4_DATA);

        List<TeaPutRequest> teaPutRequestList = Lists.newArrayList();
        TeaPutRequest lastTeaPutRequest = null;
        TeaUnitPutRequest lastTeaUnitPutRequest = null;
        int rowIndex4Tea = BizConsts.ROW_START_NUM_4_DATA;
        while (true) {
            Row row = sheet.getRow(rowIndex4Tea);
            if (row == null) {
                break;
            }

            Cell cell4TeaCode = row.getCell(BizConsts.TITLE_TEA_CODE_INDEX);
            if (cell4TeaCode != null) {
                // 表示来到了新的 tea,开始新的茶品部分
                lastTeaPutRequest = new TeaPutRequest();
                lastTeaPutRequest.setTenantCode(tenantCode);
                teaPutRequestList.add(lastTeaPutRequest);

                // 设置茶品编码
                String teaCode = cell4TeaCode.getStringCellValue();
                lastTeaPutRequest.setTeaCode(teaCode);
                // 设置茶品名称
                Cell cell4TeaName = row.getCell(BizConsts.TITLE_TEA_NAME_INDEX);
                String teaName = cell4TeaName.getStringCellValue();
                lastTeaPutRequest.setTeaName(teaName);
                // 设置外部茶品编码
                Cell cell4OuterTeaName = row.getCell(BizConsts.TITLE_OUTER_TEA_CODE_INDEX);
                String outerTeaCode = cell4OuterTeaName.getStringCellValue();
                lastTeaPutRequest.setOuterTeaCode(outerTeaCode);
                // 设置状态
                Cell cell4State = row.getCell(BizConsts.TITLE_STATE_INDEX);
                String state = cell4State.getStringCellValue();
                lastTeaPutRequest.setState(BizConsts.STATE_DISABLED_LABEL.equals(state) ? BizConsts.STATE_DISABLED : BizConsts.STATE_ENABLED);
                // 设置类型编码
                Cell cell4TeaTypeCode = row.getCell(BizConsts.TITLE_TEA_TYPE_CODE_INDEX);
                String teaTypeCode = cell4TeaTypeCode.getStringCellValue();
                lastTeaPutRequest.setTeaTypeCode(teaTypeCode);
                // 设置图片链接
                Cell cell4ImgLink = row.getCell(BizConsts.TITLE_IMG_LINK_INDEX);
                String imgLink = cell4ImgLink.getStringCellValue();
                lastTeaPutRequest.setImgLink(imgLink);
            }

            Cell cell4TeaUnitName = row.getCell(BizConsts.COL_START_NUM_4_TEA_UNIT);
            if (cell4TeaUnitName != null) {
                // 表示来到了新的 teaUnit，开始新的 unit 部分
                lastTeaUnitPutRequest = new TeaUnitPutRequest();
                lastTeaPutRequest.addTeaUnit(lastTeaUnitPutRequest);

                // 设置 teaUnitName
                String teaUnitName = cell4TeaUnitName.getStringCellValue();
                lastTeaUnitPutRequest.setTeaUnitName(teaUnitName);
            }

            // 设置步骤序号
            Cell cell4AdjustStepIndex = row.getCell(BizConsts.TITLE_STEP_INDEX_INDEX);
            if (cell4AdjustStepIndex == null) {
                break;
            }
            int adjustStepIndex = Double.valueOf(cell4AdjustStepIndex.getNumericCellValue()).intValue();
            // 设置物料编码
            Cell cell4AdjustToppingCode = row.getCell(BizConsts.TITLE_TOPPING_CODE_INDEX);
            String adjustToppingCode = cell4AdjustToppingCode.getStringCellValue();
            // 设置调整用量
            Cell cell4AdjustAmount = row.getCell(BizConsts.TITLE_ACTUAL_AMOUNT_INDEX);
            int actualAmount = Double.valueOf(cell4AdjustAmount.getNumericCellValue()).intValue();
            // 每一行都是单独的 toppingAdjustRulePutRequest
            ToppingAdjustRulePutRequest adjustRulePutRequest = new ToppingAdjustRulePutRequest();
            adjustRulePutRequest.setBaseAmount(0);
            adjustRulePutRequest.setAdjustType(BizConsts.TOPPING_ADJUST_TYPE_INCRESE);
            adjustRulePutRequest.setAdjustMode(BizConsts.TOPPING_ADJUST_MODE_FIX);
            adjustRulePutRequest.setStepIndex(adjustStepIndex);
            adjustRulePutRequest.setToppingCode(adjustToppingCode);
            adjustRulePutRequest.setAdjustAmount(actualAmount);
            // 添加到 teaUnit 中
            lastTeaUnitPutRequest.addToppingAdjustRulePutRequest(adjustRulePutRequest);

            rowIndex4Tea++;
        }
        for (TeaPutRequest teaPutRequest : teaPutRequestList) {
            // 需要构造 actStepList
            List<TeaUnitPutRequest> teaUnitList = teaPutRequest.getTeaUnitList();
            for (TeaUnitPutRequest teaUnitPutRequest : teaUnitList) {
                String teaUnitName = teaUnitPutRequest.getTeaUnitName();
                String[] teaUnitNameParts = teaUnitName.split("-");

                List<SpecItemPO> specItemPOList = Lists.newArrayList();
                for (String specItemName : teaUnitNameParts) {
                    SpecItemPO specItemPO = SpringUtils.getSpecItemAccessor().selectOneBySpecItemName(tenantCode,
                            specItemName);
                    specItemPOList.add(specItemPO);
                }
                specItemPOList.sort((o1, o2) -> o1.getSpecCode().compareTo(o2.getSpecCode()));
                StringBuffer newTeaUnitCode = new StringBuffer();
                StringBuffer newTeaUnitName = new StringBuffer();
                List<SpecItemRulePutRequest> specItemRulePutRequestList = Lists.newArrayList();
                for (SpecItemPO specItemPO : specItemPOList) {
                    if (newTeaUnitCode.length() > BizConsts.NUM_ZERO) {
                        newTeaUnitCode.append(BizConsts.STR_HORIZONTAL_BAR);
                    }
                    newTeaUnitCode.append(specItemPO.getSpecItemCode());
                    if (newTeaUnitName.length() > BizConsts.NUM_ZERO) {
                        newTeaUnitName.append(BizConsts.STR_HORIZONTAL_BAR);
                    }
                    newTeaUnitName.append(specItemPO.getSpecItemName());

                    SpecItemRulePutRequest specItemRulePutRequest = new SpecItemRulePutRequest();
                    specItemRulePutRequest.setSpecCode(specItemPO.getSpecCode());
                    specItemRulePutRequest.setSpecItemCode(specItemPO.getSpecItemCode());
                    specItemRulePutRequestList.add(specItemRulePutRequest);
                }
                teaUnitPutRequest.setTeaUnitCode(newTeaUnitCode.toString());
                teaUnitPutRequest.setTeaUnitName(newTeaUnitName.toString());
                teaUnitPutRequest.setSpecItemRuleList(specItemRulePutRequestList);

                if (teaPutRequest.getActStepList() == null) {
                    List<ToppingAdjustRulePutRequest> adjustRuleList = teaUnitPutRequest.getToppingAdjustRuleList();
                    for (ToppingAdjustRulePutRequest adjustRule : adjustRuleList) {
                        ToppingBaseRulePutRequest baseRulePutRequest = new ToppingBaseRulePutRequest();
                        baseRulePutRequest.setToppingCode(adjustRule.getToppingCode());
                        baseRulePutRequest.setBaseAmount(BizConsts.NUM_ZERO);
                        // 通过添加 toppingBaseRule，会自动构造 actStepList
                        teaPutRequest.addToppingBaseRule(adjustRule.getStepIndex(), baseRulePutRequest);
                    }
                }
            }
        }
        return teaPutRequestList;
    }

    private void addMergedRegion4RowRange(Sheet sheet, Cell cell, int rowRange) {
        ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + rowRange - BizConsts.NUM_ONE,
                cell.getColumnIndex(), cell.getColumnIndex());
    }

    private List<TeaExcel> fetchData4Export(String tenantCode) {
        List<TeaExcel> teaExcelList = Lists.newArrayList();
        List<TeaPO> teaPOList = SpringUtils.getTeaAccessor().selectList(tenantCode);
        for (TeaPO teaPO : teaPOList) {
            TeaExcel teaExcel = new TeaExcel();
            teaExcel.setTeaInfoPart(convertToTeaInfoExcel(teaPO));
            teaExcelList.add(teaExcel);

            List<ToppingBaseRulePO> toppingBaseRulePOList = SpringUtils.getToppingBaseRuleAccessor()
                    .selectListByTeaCode(teaPO.getTenantCode(), teaPO.getTeaCode());
            Map<Integer, Map<String, ToppingBaseRulePO>> baseRuleMapByStepIndex = convertToBaseRuleMap(
                    toppingBaseRulePOList);

            List<TeaUnitPart> teaUnitPartList = convertToTeaUnitPart(SpringUtils.getTeaUnitAccessor()
                    .selectListByTeaCode(teaPO.getTenantCode(), teaPO.getTeaCode()));
            teaExcel.addTeaUnit(teaUnitPartList);

            for (TeaUnitPart teaUnitPart : teaUnitPartList) {
                List<ToppingAdjustRulePO> toppingAdjustRulePOList = SpringUtils.getToppingAdjustRuleAccessor()
                        .selectListByTeaUnitCode(teaPO.getTenantCode(), teaPO.getTeaCode(),
                                teaUnitPart.getTeaUnitCode());
                teaExcel.addAdjustRulePart(convertToAdjustRulePart(baseRuleMapByStepIndex, toppingAdjustRulePOList));
            }
        }
        return teaExcelList;
    }

    private List<ToppingAdjustRulePart> convertToAdjustRulePart(
            Map<Integer, Map<String, ToppingBaseRulePO>> baseRuleMapByStepIndex,
            List<ToppingAdjustRulePO> adjustRulePOList) {
        List<ToppingAdjustRulePart> resultList = Lists.newArrayList();
        for (ToppingAdjustRulePO adjustRulePO : adjustRulePOList) {
            ToppingAdjustRulePart adjustRulePart = new ToppingAdjustRulePart();
            adjustRulePart.setStepIndex(adjustRulePO.getStepIndex());

            ToppingPO toppingPO = SpringUtils.getToppingAccessor().selectOneByToppingCode(
                    adjustRulePO.getTenantCode(), adjustRulePO.getToppingCode());
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

    private Map<Integer, Map<String, ToppingBaseRulePO>> convertToBaseRuleMap(List<ToppingBaseRulePO> poList) {
        if (poList == null) {
            return null;
        }

        Map<Integer, Map<String, ToppingBaseRulePO>> mapByStepIndex = Maps.newHashMap();
        for (ToppingBaseRulePO po : poList) {
            int stepIndex = po.getStepIndex();
            String toppingCode = po.getToppingCode();

            Map<String, ToppingBaseRulePO> mapByToppingCode = mapByStepIndex.get(stepIndex);
            if (mapByToppingCode == null) {
                mapByToppingCode = Maps.newHashMap();
                mapByStepIndex.put(stepIndex, mapByToppingCode);
            }
            mapByToppingCode.put(toppingCode, po);
        }
        return mapByStepIndex;
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
}