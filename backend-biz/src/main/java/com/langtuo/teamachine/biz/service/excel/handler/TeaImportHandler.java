package com.langtuo.teamachine.biz.service.excel.handler;

import com.langtuo.teamachine.api.request.drink.TeaPutRequest;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface TeaImportHandler {
    /**
     * 导出茶品信息
     * @param tenantCode
     * @return
     */
    TeaPutRequest upload(String tenantCode, XSSFWorkbook workbook);
}
