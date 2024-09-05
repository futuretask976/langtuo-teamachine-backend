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
import com.langtuo.teamachine.biz.service.excel.export.*;
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

        return TeaMachineResult.success(workbook);
    }

    @Override
    public TeaMachineResult<Void> importByExcel(String tenantCode, XSSFWorkbook workbook) {
        if (StringUtils.isBlank(tenantCode) || workbook == null) {
            return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

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
                    SpecItemPO specItemPO = specItemAccessor.selectOneBySpecItemName(tenantCode, specItemName);
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

        for (TeaPutRequest teaPutRequest : teaPutRequestList) {
            TeaMachineResult<Void> result = this.put(teaPutRequest);
            if (!result.isSuccess()) {
                return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
            }
        }
        return TeaMachineResult.success();
    }

    private void addMergedRegion4RowRange(Sheet sheet, Cell cell, int rowRange) {
        ExcelUtils.addMergedRegion(sheet, cell.getRowIndex(), cell.getRowIndex() + rowRange - BizConsts.NUM_ONE,
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
            Map<Integer, Map<String, ToppingBaseRulePO>> baseRuleMapByStepIndex = convertToBaseRuleMap(
                    toppingBaseRulePOList);

            List<TeaUnitPart> teaUnitPartList = convertToTeaUnitPart(teaUnitAccessor.selectListByTeaCode(teaPO.getTenantCode(),
                    teaPO.getTeaCode()));
            teaExcel.addTeaUnit(teaUnitPartList);

            for (TeaUnitPart teaUnitPart : teaUnitPartList) {
                List<ToppingAdjustRulePO> toppingAdjustRulePOList = toppingAdjustRuleAccessor.selectListByTeaUnitCode(
                        teaPO.getTenantCode(), teaPO.getTeaCode(), teaUnitPart.getTeaUnitCode());
                teaExcel.addAdjustRulePart(convertToAdjustRulePart(baseRuleMapByStepIndex, toppingAdjustRulePOList));
            }
        }
        return teaExcelList;
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

    private ToppingBaseRulePO getBaseRuleByMap(int stepIndex, String toppingCode,
            Map<Integer, Map<String, ToppingBaseRulePO>> mapByStepIndex) {
        Map<String, ToppingBaseRulePO> mapByToppingCode = mapByStepIndex.get(stepIndex);
        if (mapByToppingCode == null) {
            return null;
        }
        return mapByToppingCode.get(toppingCode);
    }
}
