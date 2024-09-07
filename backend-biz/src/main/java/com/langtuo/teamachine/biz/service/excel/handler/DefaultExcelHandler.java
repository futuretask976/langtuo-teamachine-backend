package com.langtuo.teamachine.biz.service.excel.handler;

import com.langtuo.teamachine.biz.service.excel.parser.DefaultDeployParser;
import com.langtuo.teamachine.biz.service.excel.parser.DefaultTeaParser;
import com.langtuo.teamachine.biz.service.excel.parser.DeployParser;
import com.langtuo.teamachine.biz.service.excel.parser.TeaParser;

public class DefaultExcelHandler implements ExcelHandler {
    @Override
    public DeployParser getDeployHandler() {
        return new DefaultDeployParser();
    }

    @Override
    public TeaParser getTeaHandler() {
        return new DefaultTeaParser();
    }
}
