package com.langtuo.teamachine.web;

import cn.hutool.extra.spring.SpringUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class TeaMachineDisposableBean implements DisposableBean {
    @Override
    public void destroy() throws Exception {
        System.out.println("$$$$$ ## teaMachineDisposableBean|destroy|entering");

        HikariDataSource hikariDataSource = SpringUtil.getBean(HikariDataSource.class);
        System.out.println("$$$$$ ## teaMachineDisposableBean|destroy|hikariDataSource|" + hikariDataSource);
        if (hikariDataSource != null) {
            hikariDataSource.getConnection().close();
            hikariDataSource.close();
        }
        System.out.println("$$$$$ ## teaMachineDisposableBean|destroy|close|after");

        System.out.println("$$$$$ ## teaMachineDisposableBean|destroy|exiting");
    }
}
