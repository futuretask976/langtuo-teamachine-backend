package com.langtuo.teamachine.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TeaMachineShutdownHook implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("teaMachineShutdownHook|onApplicationEvent|entering");
        System.out.println("teaMachineShutdownHook|onApplicationEvent|entering");
        log.info("teaMachineShutdownHook|onApplicationEvent|exiting");
    }
}
