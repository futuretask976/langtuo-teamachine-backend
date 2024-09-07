package com.langtuo.teamachine.biz.service.excel.parser;

import com.langtuo.teamachine.api.request.drink.TeaPutRequest;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface TeaParser {
    /**
     * 导出茶品信息
     * @param tenantCode
     * @return
     */
    XSSFWorkbook export(String tenantCode);

    /**
     * 导出茶品信息
     * @param tenantCode
     * @return
     */
    TeaPutRequest upload(String tenantCode, XSSFWorkbook workbook);
}
