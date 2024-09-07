package com.langtuo.teamachine.biz.service.excel.handler;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface DeployExportHandler {
    /**
     * 导出部署信息
     * @param tenantCode
     * @return
     */
    XSSFWorkbook export(String tenantCode);
}
