package com.gx.sp3.demo.web;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 当前类用于在Tomcat中启动
 * @author miya
 */
public class GxSpringBoot2DemoServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        System.out.printf("!!! GxSpringBoot3DemoServletInitializer#configure entering\n");
        return builder.sources(GxSpringBoot2DemoApplication.class);
    }
}
