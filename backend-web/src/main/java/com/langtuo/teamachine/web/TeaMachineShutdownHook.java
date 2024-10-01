package com.langtuo.teamachine.web;

import com.langtuo.teamachine.biz.aync.threadpool.AsyncExeService;
import com.langtuo.teamachine.mqtt.threadpool.ConsumeExeService;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

@Component
public class TeaMachineShutdownHook implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("$$$$$ teaMachineShutdownHook|onApplicationEvent|entering");

        AsyncExeService.destroy();
        ConsumeExeService.destroy();

        AbandonedConnectionCleanupThread.uncheckedShutdown();
        Enumeration<Driver> enumeration = DriverManager.getDrivers();
        while (enumeration.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(enumeration.nextElement());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("$$$$$ teaMachineShutdownHook|onApplicationEvent|exiting");
    }
}
