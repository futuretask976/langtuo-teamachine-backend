package com.langtuo.teamachine.web;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 当前类用于在Tomcat中启动
 * @author miya
 */
public class TeaMachineBackendServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TeaMachineBackendApplication.class);
    }
}
