package com.langtuo.teamachine.biz.service.testor.drink;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.excel.export.*;
import com.langtuo.teamachine.biz.service.util.ExcelUtils;
import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.drink.TeaMapper;
import com.langtuo.teamachine.dao.mapper.drink.TeaUnitMapper;
import com.langtuo.teamachine.dao.mapper.drink.ToppingAdjustRuleMapper;
import com.langtuo.teamachine.dao.mapper.drink.ToppingBaseRuleMapper;
import com.langtuo.teamachine.dao.po.drink.TeaPO;
import com.langtuo.teamachine.dao.po.drink.TeaUnitPO;
import com.langtuo.teamachine.dao.po.drink.ToppingAdjustRulePO;
import com.langtuo.teamachine.dao.po.drink.ToppingBaseRulePO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.util.Sets;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TeaTestor {
    public static void main(String args[]) {
        insert();
    }

    public static void insert() {
        File file = new File("/Users/Miya/Downloads/tea_export.xlsx");

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Map<String, Integer> titleColumnIndexMap = Maps.newHashMap();

            Sheet sheet = workbook.getSheetAt(0);
            Row titleRow = sheet.getRow(0);
            for (int i = 0; i < BizConsts.TITLE_LIST_4_TEA_EXPORT.size(); i++) {
                Cell cell = titleRow.getCell(i);
                if (BizConsts.TITLE_TEA_CODE.equals(cell.getStringCellValue())) {
                    titleColumnIndexMap.put(BizConsts.TITLE_TEA_CODE, i);
                } else if (BizConsts.TITLE_TEA_NAME.equals(cell.getStringCellValue())) {
                    titleColumnIndexMap.put(BizConsts.TITLE_TEA_NAME, i);
                } else if (BizConsts.TITLE_OUTER_TEA_CODE.equals(cell.getStringCellValue())) {
                    titleColumnIndexMap.put(BizConsts.TITLE_OUTER_TEA_CODE, i);
                } else if (BizConsts.TITLE_STATE.equals(cell.getStringCellValue())) {
                    titleColumnIndexMap.put(BizConsts.TITLE_STATE, i);
                } else if (BizConsts.TITLE_TEA_TYPE_CODE.equals(cell.getStringCellValue())) {
                    titleColumnIndexMap.put(BizConsts.TITLE_TEA_TYPE_CODE, i);
                } else if (BizConsts.TITLE_IMG_LINK.equals(cell.getStringCellValue())) {
                    titleColumnIndexMap.put(BizConsts.TITLE_IMG_LINK, i);
                } else if (BizConsts.TITLE_BASE_STEP_INDEX.equals(cell.getStringCellValue())) {
                    titleColumnIndexMap.put(BizConsts.TITLE_BASE_STEP_INDEX, i);
                } else if (BizConsts.TITLE_BASE_TOPPING_CODE.equals(cell.getStringCellValue())) {
                    titleColumnIndexMap.put(BizConsts.TITLE_BASE_TOPPING_CODE, i);
                } else if (BizConsts.TITLE_BASE_AMOUNT.equals(cell.getStringCellValue())) {
                    titleColumnIndexMap.put(BizConsts.TITLE_BASE_AMOUNT, i);
                } else if (BizConsts.TITLE_SPEC_GROUP_NAME.equals(cell.getStringCellValue())) {
                    titleColumnIndexMap.put(BizConsts.TITLE_SPEC_GROUP_NAME, i);
                } else if (BizConsts.TITLE_ADJUST_STEP_INDEX.equals(cell.getStringCellValue())) {
                    titleColumnIndexMap.put(BizConsts.TITLE_ADJUST_STEP_INDEX, i);
                } else if (BizConsts.TITLE_ADJUST_TOPPING_CODE.equals(cell.getStringCellValue())) {
                    titleColumnIndexMap.put(BizConsts.TITLE_ADJUST_TOPPING_CODE, i);
                } else if (BizConsts.TITLE_ADJUST_TYPE.equals(cell.getStringCellValue())) {
                    titleColumnIndexMap.put(BizConsts.TITLE_ADJUST_TYPE, i);
                } else if (BizConsts.TITLE_ADJUST_MODE.equals(cell.getStringCellValue())) {
                    titleColumnIndexMap.put(BizConsts.TITLE_ADJUST_MODE, i);
                } else if (BizConsts.TITLE_ADJUST_AMOUNT.equals(cell.getStringCellValue())) {
                    titleColumnIndexMap.put(BizConsts.TITLE_ADJUST_AMOUNT, i);
                }
            }

            List<TeaExcel> teaExcelList = Lists.newArrayList();
            TeaInfoPart teaInfoPart = null;
            List<ToppingBaseRulePart> baseRuleList4Tea = Lists.newArrayList();
            List<ToppingAdjustRulePart> adjustRuleList4Tea = Lists.newArrayList();
            int rowIndex4Tea = 1;
            String lastTeaCode = null;
            do {
                Row row = sheet.getRow(rowIndex4Tea);
                Cell cell4TeaCode = row.getCell(titleColumnIndexMap.get(BizConsts.TITLE_TEA_CODE));
                String teaCode = cell4TeaCode.getStringCellValue();
                if (StringUtils.isBlank(teaCode)) {
                    break;
                }
                if (!teaCode.equals(lastTeaCode)) {
                    if (!StringUtils.isBlank(lastTeaCode)) {
                        TeaExcel teaExcel = new TeaExcel();
                        teaExcel.setTeaInfoPart(teaInfoPart);
                        teaExcel.setToppingAdjustRulePartList(adjustRuleList4Tea);
                        teaExcelList.add(teaExcel);
                    }

                    lastTeaCode = teaCode;

                    Cell cell4TeaName = row.getCell(titleColumnIndexMap.get(BizConsts.TITLE_TEA_CODE));
                    String teaName = cell4TeaName.getStringCellValue();

                    Cell cell4OuterTeaName = row.getCell(titleColumnIndexMap.get(BizConsts.TITLE_OUTER_TEA_CODE));
                    String outerTeaName = cell4OuterTeaName.getStringCellValue();

                    Cell cell4State = row.getCell(titleColumnIndexMap.get(BizConsts.TITLE_STATE));
                    String state = cell4State.getStringCellValue();

                    Cell cell4TeaTypeCode = row.getCell(titleColumnIndexMap.get(BizConsts.TITLE_TEA_TYPE_CODE));
                    String teaTypeCode = cell4TeaTypeCode.getStringCellValue();

                    Cell cell4ImgLink = row.getCell(titleColumnIndexMap.get(BizConsts.TITLE_IMG_LINK));
                    String imgLink = cell4ImgLink.getStringCellValue();

                    teaInfoPart = new TeaInfoPart();
                    teaInfoPart.setTeaCode(teaCode);
                    teaInfoPart.setTeaName(teaName);
                    teaInfoPart.setOuterTeaCode(outerTeaName);
                    teaInfoPart.setState("禁用".equals(state) ? 0 : 1);
                    teaInfoPart.setTeaTypeCode(teaTypeCode);
                    teaInfoPart.setImgLink(imgLink);
                }

                Cell cell4AdjustStepIndex = row.getCell(titleColumnIndexMap.get(BizConsts.TITLE_ADJUST_STEP_INDEX));
                int adjustStepIndex = Double.valueOf(cell4AdjustStepIndex.getNumericCellValue()).intValue();

                Cell cell4AdjustToppingCode = row.getCell(titleColumnIndexMap.get(BizConsts.TITLE_ADJUST_TOPPING_CODE));
                String adjustToppingCode = cell4AdjustToppingCode.getStringCellValue();

                Cell cell4AdjustType = row.getCell(titleColumnIndexMap.get(BizConsts.TITLE_ADJUST_TYPE));
                String adjustType = cell4AdjustType.getStringCellValue();

                Cell cell4AdjustMode = row.getCell(titleColumnIndexMap.get(BizConsts.TITLE_ADJUST_MODE));
                String adjustMode = cell4AdjustMode.getStringCellValue();

                Cell cell4AdjustAmount = row.getCell(titleColumnIndexMap.get(BizConsts.TITLE_ADJUST_AMOUNT));
                int adjustAmount = Double.valueOf(cell4AdjustAmount.getNumericCellValue()).intValue();

                ToppingAdjustRulePart toppingAdjustRulePart = new ToppingAdjustRulePart();
                toppingAdjustRulePart.setStepIndex(Integer.valueOf(adjustStepIndex));
                toppingAdjustRulePart.setToppingCode(adjustToppingCode);
                toppingAdjustRulePart.setAdjustType(adjustType == "减少" ? 0 : 1);
                toppingAdjustRulePart.setAdjustMode(adjustMode == "固定值" ? 0 : 1);
                toppingAdjustRulePart.setAdjustAmount(adjustAmount);
                adjustRuleList4Tea.add(toppingAdjustRulePart);

                rowIndex4Tea++;
            } while(true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaMapper teaMapper = sqlSession.getMapper(TeaMapper.class);
        ToppingBaseRuleMapper toppingBaseRuleMapper = sqlSession.getMapper(ToppingBaseRuleMapper.class);
        TeaUnitMapper teaUnitMapper = sqlSession.getMapper(TeaUnitMapper.class);
        ToppingAdjustRuleMapper toppingAdjustRuleMapper = sqlSession.getMapper(ToppingAdjustRuleMapper.class);

        List<TeaExcel> teaExcelList = Lists.newArrayList();
        List<TeaPO> teaPOList = teaMapper.selectList("tenant_001");
        for (TeaPO teaPO : teaPOList) {
            TeaExcel teaExcel = new TeaExcel();
            teaExcel.setTeaInfoPart(convertToTeaInfoExcel(teaPO));
            teaExcelList.add(teaExcel);

            List<ToppingBaseRulePO> toppingBaseRulePOList = toppingBaseRuleMapper.selectList(teaPO.getTenantCode(),
                    teaPO.getTeaCode());
            teaExcel.setToppingBaseRulePartList(convertToToppingBaseRuleExcel(toppingBaseRulePOList));

            List<TeaUnitPO> teaUnitPOList = filterTeaUnitPO(teaUnitMapper.selectList(teaPO.getTenantCode(),
                    teaPO.getTeaCode()));
            teaExcel.setTeaUnitPartList(convertToTeaUnitExcel(teaUnitPOList));

            for (TeaUnitPart teaUnitPart : teaExcel.getTeaUnitPartList()) {
                List<ToppingAdjustRulePO> toppingAdjustRulePOList = toppingAdjustRuleMapper.selectList(
                        teaPO.getTenantCode(), teaPO.getTeaCode(), teaUnitPart.getTeaUnitCode());
                teaExcel.addAll(convertToToppingAdjustRuleExcel(toppingAdjustRulePOList));
            }
        }

        // 创建一个新的工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 创建一个工作表
        Sheet sheet = workbook.createSheet("菜单信息导出");
        // 标题内容
        List<String> titleList = Lists.newArrayList(
                "茶品编码",
                "茶品名称",
                "外部茶品编码",
                "状态",
                "茶品类型编码",
                "图片链接",
                "步骤序号",
                "物料编码",
                "基础用量",
                "规格名称",
                "步骤序号",
                "物料编码",
                "调整类型",
                "调整模式",
                "调整用量");
        // 创建标题行（0基索引）
        Row row = sheet.createRow(0);
        // 创建单元格并设置值
        for (int i = 0; i < titleList.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titleList.get(i));
        }

        int rowStartIndex4Tea = 1;
        for (TeaExcel teaExcel : teaExcelList) {
            TeaInfoPart teaInfoPart = teaExcel.getTeaInfoPart();
            Row dataRow = ExcelUtils.createRowIfAbsent(sheet, rowStartIndex4Tea);
            int columnIndex = 0;
            Cell cell = null;

            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(teaInfoPart.getTeaCode());
            ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + teaExcel.getToppingAdjustRulePartList().size() - 1, cell.getColumnIndex(), cell.getColumnIndex());

            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(teaInfoPart.getTeaName());
            ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + teaExcel.getToppingAdjustRulePartList().size() - 1, cell.getColumnIndex(), cell.getColumnIndex());

            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(teaInfoPart.getOuterTeaCode());
            ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + teaExcel.getToppingAdjustRulePartList().size() - 1, cell.getColumnIndex(), cell.getColumnIndex());

            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(teaInfoPart.getState());
            ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + teaExcel.getToppingAdjustRulePartList().size() - 1, cell.getColumnIndex(), cell.getColumnIndex());

            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(teaInfoPart.getTeaTypeCode());
            ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + teaExcel.getToppingAdjustRulePartList().size() - 1, cell.getColumnIndex(), cell.getColumnIndex());

            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(teaInfoPart.getImgLink());
            ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + teaExcel.getToppingAdjustRulePartList().size() - 1, cell.getColumnIndex(), cell.getColumnIndex());


            List<ToppingBaseRulePart> toppingBaseRulePartList = teaExcel.getToppingBaseRulePartList();
            int rowRange4BaseRule = teaExcel.getToppingAdjustRulePartList().size() / teaExcel.getToppingBaseRulePartList().size();
            Row row4BaseRule = null;
            for (ToppingBaseRulePart toppingBaseRulePart : toppingBaseRulePartList) {
                if (row4BaseRule == null) {
                    row4BaseRule = dataRow;
                } else {
                    row4BaseRule = ExcelUtils.createRowIfAbsent(sheet, row4BaseRule.getRowNum() + rowRange4BaseRule);
                }
                int columnIndex4BaseRule = columnIndex;

                cell = row4BaseRule.createCell(columnIndex4BaseRule++);
                cell.setCellValue(toppingBaseRulePart.getStepIndex());
                ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + rowRange4BaseRule - 1, cell.getColumnIndex(), cell.getColumnIndex());

                cell = row4BaseRule.createCell(columnIndex4BaseRule++);
                cell.setCellValue(toppingBaseRulePart.getToppingName());
                ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + rowRange4BaseRule - 1, cell.getColumnIndex(), cell.getColumnIndex());

                cell = row4BaseRule.createCell(columnIndex4BaseRule++);
                cell.setCellValue(toppingBaseRulePart.getBaseAmount());
                ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + rowRange4BaseRule - 1, cell.getColumnIndex(), cell.getColumnIndex());
            }

            List<TeaUnitPart> teaUnitPartList = teaExcel.getTeaUnitPartList();
            int rowRange4TeaUnit = teaExcel.getToppingAdjustRulePartList().size() / teaExcel.getTeaUnitPartList().size();
            int rowIndex4TeaUnit = rowStartIndex4Tea;
            for (TeaUnitPart teaUnitPart : teaUnitPartList) {
                Row row4TeaUnit = ExcelUtils.createRowIfAbsent(sheet, rowIndex4TeaUnit);
                int columnIndex4TeaUnit = 9;

                cell = row4TeaUnit.createCell(columnIndex4TeaUnit++);
                cell.setCellValue(teaUnitPart.getTeaUnitName());
                ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + rowRange4TeaUnit - 1, cell.getColumnIndex(), cell.getColumnIndex());

                rowIndex4TeaUnit = rowIndex4TeaUnit + rowRange4TeaUnit;
            }

            List<ToppingAdjustRulePart> toppingAdjustRulePartList = teaExcel.getToppingAdjustRulePartList();
            int rowIndex4AdjustRule = rowStartIndex4Tea;
            for (ToppingAdjustRulePart toppingAdjustRulePart : toppingAdjustRulePartList) {
                Row row4AdjustRule = ExcelUtils.createRowIfAbsent(sheet, rowIndex4AdjustRule++);
                int columnIndex4AdjustRule = 10;

                cell = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell.setCellValue(toppingAdjustRulePart.getStepIndex());

                cell = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell.setCellValue(toppingAdjustRulePart.getToppingName());

                cell = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell.setCellValue(toppingAdjustRulePart.getAdjustType());

                cell = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell.setCellValue(toppingAdjustRulePart.getAdjustMode());

                cell = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell.setCellValue(toppingAdjustRulePart.getAdjustAmount());
            }

            rowStartIndex4Tea = rowStartIndex4Tea + teaExcel.getToppingAdjustRulePartList().size();
        }

        File exportFile = new File("test/export_test.xlsx");
        exportFile.getParentFile().mkdirs();
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(exportFile);
            workbook.write(fileOutputStream);
            workbook.close();
            fileOutputStream.flush();
        } catch (IOException e) {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        sqlSession.commit();
        sqlSession.close();
    }

    private static List<TeaUnitPO> filterTeaUnitPO(List<TeaUnitPO> teaUnitPOList) {
        Set<TeaUnitPO> result = Sets.newHashSet();
        for (TeaUnitPO teaUnitPO : teaUnitPOList) {
            result.add(teaUnitPO);
        }
        return result.stream().collect(Collectors.toList());
    }

    private static List<ToppingAdjustRulePart> convertToToppingAdjustRuleExcel(List<ToppingAdjustRulePO> list) {
        List<ToppingAdjustRulePart> resultList = Lists.newArrayList();
        for (ToppingAdjustRulePO toppingAdjustRulePO : list) {
            ToppingAdjustRulePart toppingAdjustRulePart = new ToppingAdjustRulePart();
            toppingAdjustRulePart.setStepIndex(toppingAdjustRulePO.getStepIndex());
            toppingAdjustRulePart.setToppingName(toppingAdjustRulePO.getToppingCode());
            toppingAdjustRulePart.setAdjustMode(toppingAdjustRulePO.getAdjustMode());
            toppingAdjustRulePart.setAdjustType(toppingAdjustRulePO.getAdjustType());
            toppingAdjustRulePart.setAdjustAmount(toppingAdjustRulePO.getAdjustAmount());
            resultList.add(toppingAdjustRulePart);
        }
        return resultList;
    }

    private static List<TeaUnitPart> convertToTeaUnitExcel(List<TeaUnitPO> list) {
        Set<TeaUnitPart> resultSet = Sets.newHashSet();
        for (TeaUnitPO teaUnitPO : list) {
            TeaUnitPart teaUnitPart = new TeaUnitPart();
            teaUnitPart.setTeaUnitCode(teaUnitPO.getTeaUnitCode());
            teaUnitPart.setTeaUnitName(teaUnitPO.getTeaUnitName());
            resultSet.add(teaUnitPart);
        }
        return resultSet.stream().collect(Collectors.toList());
    }

    private static List<ToppingBaseRulePart> convertToToppingBaseRuleExcel(List<ToppingBaseRulePO> list) {
        List<ToppingBaseRulePart> resultList = Lists.newArrayList();
        for (ToppingBaseRulePO toppingBaseRulePO : list) {
            ToppingBaseRulePart toppingBaseRulePart = new ToppingBaseRulePart();
            toppingBaseRulePart.setStepIndex(toppingBaseRulePO.getStepIndex());
            toppingBaseRulePart.setToppingName(toppingBaseRulePO.getToppingCode());
            toppingBaseRulePart.setBaseAmount(toppingBaseRulePO.getBaseAmount());
            resultList.add(toppingBaseRulePart);
        }
        return resultList;
    }

    private static TeaInfoPart convertToTeaInfoExcel(TeaPO teaPO) {
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
