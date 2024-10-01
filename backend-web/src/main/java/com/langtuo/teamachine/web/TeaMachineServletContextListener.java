package com.langtuo.teamachine.web;

import com.langtuo.teamachine.biz.aync.threadpool.AsyncExeService;
import com.langtuo.teamachine.mqtt.threadpool.ConsumeExeService;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class TeaMachineServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 应用启动时的操作
        System.out.println("teaMachineServletContextListener|contextInitialized|entering");
        System.out.println("teaMachineServletContextListener|contextInitialized|exiting");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 应用停止时的操作
        // 例如: 关闭数据库连接，停止后台线程等
        System.out.println("$$$$$ ## teaMachineServletContextListener|contextDestroyed|entering");

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

        try {
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            //activeCount()返回当前正在活动的线程的数量
            int total = Thread.activeCount();
            Thread[] threads = new Thread[total];
            //enumerate(threads)将当前线程组中的active线程全部复制到传入的线程数组threads中
            // 并且返回数组中元素个数，即线程组中active线程数量
            threadGroup.enumerate(threads);
            for (Thread t:threads){
                System.out.println("Thread: " + t.getId() + " " + t.getName());
                try {
                    t.interrupt();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("$$$$$ ## teaMachineServletContextListener|fatal|" + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("$$$$$ ## teaMachineServletContextListener|contextDestroyed|exiting");
    }
}
