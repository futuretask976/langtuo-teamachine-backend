package com.langtuo.teamachine.biz.service.testor.drink;

import com.google.common.collect.Lists;
import com.langtuo.teamachine.biz.service.excel.*;
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
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.util.Sets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TeaTestor {
    public static void main(String args[]) {
        select();
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
            teaExcel.setTeaInfoExcel(convertToTeaInfoExcel(teaPO));
            teaExcelList.add(teaExcel);

            List<ToppingBaseRulePO> toppingBaseRulePOList = toppingBaseRuleMapper.selectList(teaPO.getTenantCode(),
                    teaPO.getTeaCode());
            teaExcel.setToppingBaseRuleExcelList(convertToToppingBaseRuleExcel(toppingBaseRulePOList));

            List<TeaUnitPO> teaUnitPOList = filterTeaUnitPO(teaUnitMapper.selectList(teaPO.getTenantCode(),
                    teaPO.getTeaCode()));
            teaExcel.setTeaUnitExcelList(convertToTeaUnitExcel(teaUnitPOList));

            for (TeaUnitExcel teaUnitExcel : teaExcel.getTeaUnitExcelList()) {
                List<ToppingAdjustRulePO> toppingAdjustRulePOList = toppingAdjustRuleMapper.selectList(
                        teaPO.getTenantCode(), teaPO.getTeaCode(), teaUnitExcel.getTeaUnitCode());
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
            TeaInfoExcel teaInfoExcel = teaExcel.getTeaInfoExcel();
            Row dataRow = ExcelUtils.createRowIfAbsent(sheet, rowStartIndex4Tea);
            int columnIndex = 0;
            Cell cell = null;

            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(teaInfoExcel.getTeaCode());
            ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + teaExcel.getToppingAdjustRuleExcelList().size() - 1, cell.getColumnIndex(), cell.getColumnIndex());

            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(teaInfoExcel.getTeaName());
            ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + teaExcel.getToppingAdjustRuleExcelList().size() - 1, cell.getColumnIndex(), cell.getColumnIndex());

            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(teaInfoExcel.getOuterTeaCode());
            ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + teaExcel.getToppingAdjustRuleExcelList().size() - 1, cell.getColumnIndex(), cell.getColumnIndex());

            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(teaInfoExcel.getState());
            ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + teaExcel.getToppingAdjustRuleExcelList().size() - 1, cell.getColumnIndex(), cell.getColumnIndex());

            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(teaInfoExcel.getTeaTypeCode());
            ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + teaExcel.getToppingAdjustRuleExcelList().size() - 1, cell.getColumnIndex(), cell.getColumnIndex());

            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(teaInfoExcel.getImgLink());
            ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + teaExcel.getToppingAdjustRuleExcelList().size() - 1, cell.getColumnIndex(), cell.getColumnIndex());


            List<ToppingBaseRuleExcel> ToppingBaseRuleExcelList = teaExcel.getToppingBaseRuleExcelList();
            int rowRange4BaseRule = teaExcel.getToppingAdjustRuleExcelList().size() / teaExcel.getToppingBaseRuleExcelList().size();
            Row row4BaseRule = null;
            for (ToppingBaseRuleExcel toppingBaseRuleExcel : ToppingBaseRuleExcelList) {
                if (row4BaseRule == null) {
                    row4BaseRule = dataRow;
                } else {
                    row4BaseRule = ExcelUtils.createRowIfAbsent(sheet, row4BaseRule.getRowNum() + rowRange4BaseRule);
                }
                int columnIndex4BaseRule = columnIndex;

                cell = row4BaseRule.createCell(columnIndex4BaseRule++);
                cell.setCellValue(toppingBaseRuleExcel.getStepIndex());
                ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + rowRange4BaseRule - 1, cell.getColumnIndex(), cell.getColumnIndex());

                cell = row4BaseRule.createCell(columnIndex4BaseRule++);
                cell.setCellValue(toppingBaseRuleExcel.getToppingName());
                ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + rowRange4BaseRule - 1, cell.getColumnIndex(), cell.getColumnIndex());

                cell = row4BaseRule.createCell(columnIndex4BaseRule++);
                cell.setCellValue(toppingBaseRuleExcel.getBaseAmount());
                ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + rowRange4BaseRule - 1, cell.getColumnIndex(), cell.getColumnIndex());
            }

            List<TeaUnitExcel> teaUnitExcelList = teaExcel.getTeaUnitExcelList();
            int rowRange4TeaUnit = teaExcel.getToppingAdjustRuleExcelList().size() / teaExcel.getTeaUnitExcelList().size();
            int rowIndex4TeaUnit = rowStartIndex4Tea;
            for (TeaUnitExcel teaUnitExcel : teaUnitExcelList) {
                Row row4TeaUnit = ExcelUtils.createRowIfAbsent(sheet, rowIndex4TeaUnit);
                int columnIndex4TeaUnit = 9;

                cell = row4TeaUnit.createCell(columnIndex4TeaUnit++);
                cell.setCellValue(teaUnitExcel.getTeaUnitName());
                ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + rowRange4TeaUnit - 1, cell.getColumnIndex(), cell.getColumnIndex());

                rowIndex4TeaUnit = rowIndex4TeaUnit + rowRange4TeaUnit;
            }

            List<ToppingAdjustRuleExcel> toppingAdjustRuleExcelList = teaExcel.getToppingAdjustRuleExcelList();
            int rowIndex4AdjustRule = rowStartIndex4Tea;
            for (ToppingAdjustRuleExcel toppingAdjustRuleExcel : toppingAdjustRuleExcelList) {
                Row row4AdjustRule = ExcelUtils.createRowIfAbsent(sheet, rowIndex4AdjustRule++);
                int columnIndex4AdjustRule = 10;

                cell = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell.setCellValue(toppingAdjustRuleExcel.getStepIndex());

                cell = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell.setCellValue(toppingAdjustRuleExcel.getToppingName());

                cell = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell.setCellValue(toppingAdjustRuleExcel.getAdjustType());

                cell = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell.setCellValue(toppingAdjustRuleExcel.getAdjustMode());

                cell = row4AdjustRule.createCell(columnIndex4AdjustRule++);
                cell.setCellValue(toppingAdjustRuleExcel.getAdjustAmount());
            }

            rowStartIndex4Tea = rowStartIndex4Tea + teaExcel.getToppingAdjustRuleExcelList().size();
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

    private static List<ToppingAdjustRuleExcel> convertToToppingAdjustRuleExcel(List<ToppingAdjustRulePO> list) {
        List<ToppingAdjustRuleExcel> resultList = Lists.newArrayList();
        for (ToppingAdjustRulePO toppingAdjustRulePO : list) {
            ToppingAdjustRuleExcel toppingAdjustRuleExcel = new ToppingAdjustRuleExcel();
            toppingAdjustRuleExcel.setStepIndex(toppingAdjustRulePO.getStepIndex());
            toppingAdjustRuleExcel.setToppingName(toppingAdjustRulePO.getToppingCode());
            toppingAdjustRuleExcel.setAdjustMode(toppingAdjustRulePO.getAdjustMode());
            toppingAdjustRuleExcel.setAdjustType(toppingAdjustRulePO.getAdjustType());
            toppingAdjustRuleExcel.setAdjustAmount(toppingAdjustRulePO.getAdjustAmount());
            resultList.add(toppingAdjustRuleExcel);
        }
        return resultList;
    }

    private static List<TeaUnitExcel> convertToTeaUnitExcel(List<TeaUnitPO> list) {
        Set<TeaUnitExcel> resultSet = Sets.newHashSet();
        for (TeaUnitPO teaUnitPO : list) {
            TeaUnitExcel teaUnitExcel = new TeaUnitExcel();
            teaUnitExcel.setTeaUnitCode(teaUnitPO.getTeaUnitCode());
            teaUnitExcel.setTeaUnitName(teaUnitPO.getTeaUnitName());
            resultSet.add(teaUnitExcel);
        }
        return resultSet.stream().collect(Collectors.toList());
    }

    private static List<ToppingBaseRuleExcel> convertToToppingBaseRuleExcel(List<ToppingBaseRulePO> list) {
        List<ToppingBaseRuleExcel> resultList = Lists.newArrayList();
        for (ToppingBaseRulePO toppingBaseRulePO : list) {
            ToppingBaseRuleExcel toppingBaseRuleExcel = new ToppingBaseRuleExcel();
            toppingBaseRuleExcel.setStepIndex(toppingBaseRulePO.getStepIndex());
            toppingBaseRuleExcel.setToppingName(toppingBaseRulePO.getToppingCode());
            toppingBaseRuleExcel.setBaseAmount(toppingBaseRulePO.getBaseAmount());
            resultList.add(toppingBaseRuleExcel);
        }
        return resultList;
    }

    private static TeaInfoExcel convertToTeaInfoExcel(TeaPO teaPO) {
        TeaInfoExcel teaInfoExcel = new TeaInfoExcel();
        teaInfoExcel.setTeaCode(teaPO.getTeaCode());
        teaInfoExcel.setTeaName(teaPO.getTeaName());
        teaInfoExcel.setTeaTypeCode(teaPO.getTeaTypeCode());
        teaInfoExcel.setOuterTeaCode(teaPO.getOuterTeaCode());
        teaInfoExcel.setImgLink(teaPO.getImgLink());
        teaInfoExcel.setState(teaPO.getState());
        return teaInfoExcel;
    }
}
