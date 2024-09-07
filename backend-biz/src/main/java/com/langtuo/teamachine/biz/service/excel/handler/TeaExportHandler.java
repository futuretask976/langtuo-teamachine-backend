package com.langtuo.teamachine.biz.service.excel.handler;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface TeaExportHandler {
    /**
     * 导出茶品信息
     * @param tenantCode
     * @return
     */
    XSSFWorkbook export(String tenantCode);
}
