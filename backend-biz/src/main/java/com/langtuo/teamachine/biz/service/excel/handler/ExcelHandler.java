package com.langtuo.teamachine.biz.service.excel.handler;

import com.langtuo.teamachine.biz.service.excel.parser.DeployParser;
import com.langtuo.teamachine.biz.service.excel.parser.TeaParser;

public interface ExcelHandler {
    /**
     * 获取部署信息处理器
     * @return
     */
    DeployParser getDeployHandler();

    /**
     * 获取茶品信息处理器
     * @return
     */
    TeaParser getTeaHandler();
}
