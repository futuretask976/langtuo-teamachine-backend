package com.langtuo.teamachine.web;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
@Slf4j
public class TeaMachineServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 应用启动时的操作
        log.info("teaMachineServletContextListener|contextInitialized|entering");
        log.info("teaMachineServletContextListener|contextInitialized|exiting");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 应用停止时的操作
        // 例如: 关闭数据库连接，停止后台线程等
        log.info("teaMachineServletContextListener|contextDestroyed|entering");
        System.out.println("teaMachineServletContextListener|contextDestroyed|entering");
        log.info("teaMachineServletContextListener|contextDestroyed|exiting");
    }
}
