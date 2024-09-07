package com.langtuo.teamachine.biz.service.excel.handler;

import com.langtuo.teamachine.api.request.drink.TeaPutRequest;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DefaultTeaImportHandler implements  TeaImportHandler {
    @Override
    public TeaPutRequest upload(String tenantCode, XSSFWorkbook workbook) {
        return null;
    }
}
