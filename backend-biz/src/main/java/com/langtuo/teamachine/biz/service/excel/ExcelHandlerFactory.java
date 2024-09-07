package com.langtuo.teamachine.biz.service.excel;

import com.google.common.collect.Maps;
import com.langtuo.teamachine.biz.service.excel.handler.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ExcelHandlerFactory implements InitializingBean {
    /**
     * 部署信息导出处理器映射，key 是 tenantCode，val 是处理器
     */
    private Map<String, DeployExportHandler> deployExportHandlerMap;

    /**
     * 茶品信息导出处理器映射，key 是 tenantCode，val 是处理器
     */
    private Map<String, TeaExportHandler> teaExportHandlerMap;

    /**
     * 茶品信息导入处理器映射，key 是 tenantCode，val 是处理器
     */
    private Map<String, TeaImportHandler> teaImportHandlerMap;

    @Override
    public void afterPropertiesSet() {
        // 初始化部署导出映射
        if (deployExportHandlerMap == null) {
            synchronized (ExcelHandlerFactory.class) {
                if (deployExportHandlerMap == null) {
                    initDeployExportHandlerMap();
                }
            }
        }
        // 初始化茶品导出映射
        if (teaExportHandlerMap == null) {
            synchronized (ExcelHandlerFactory.class) {
                if (teaExportHandlerMap == null) {
                    initTeaExportHandlerMap();
                }
            }
        }
        // 初始化茶品导入映射
        if (teaImportHandlerMap == null) {
            synchronized (ExcelHandlerFactory.class) {
                if (teaImportHandlerMap == null) {
                    initTeaImportHandlerMap();
                }
            }
        }
    }

    public DeployExportHandler getDeployExportHandler(String tenantCode) {
        return new DefaultDeployExportHandler();
    }

    public TeaExportHandler getTeaExportHandler(String tenantCode) {
        return new DefaultTeaExportHandler();
    }

    public TeaImportHandler getTeaImportHandler(String tenantCode) {
        return new DefaultTeaImportHandler();
    }

    private void initDeployExportHandlerMap() {
        deployExportHandlerMap = Maps.newHashMap();
        // TODO
    }

    private void initTeaExportHandlerMap() {
        teaExportHandlerMap = Maps.newHashMap();
        // TODO
    }

    private void initTeaImportHandlerMap() {
        teaImportHandlerMap = Maps.newHashMap();
        // TODO
    }
}
