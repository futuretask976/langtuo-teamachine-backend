package com.langtuo.teamachine.biz.service.excel.parser;

import com.langtuo.teamachine.api.request.drink.TeaPutRequest;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DefaultTeaParser implements TeaParser {
    @Override
    public XSSFWorkbook export(String tenantCode) {
        return null;
    }

    @Override
    public TeaPutRequest upload(String tenantCode, XSSFWorkbook workbook) {
        return null;
    }
}
