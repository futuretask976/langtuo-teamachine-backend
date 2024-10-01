package com.langtuo.teamachine.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

@Slf4j
public class TeaMachineDisposableBean implements DisposableBean {
    @Override
    public void destroy() throws Exception {
        log.info("teaMachineDisposableBean|destroy|entering");
        log.info("teaMachineDisposableBean|destroy|exiting");
    }
}
