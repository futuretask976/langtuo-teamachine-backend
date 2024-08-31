package com.langtuo.teamachine.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
        basePackages = {"com.langtuo.teamachine"}
)
@MapperScan("com.langtuo.teamachine.dao.mapper")
public class TeaMachineBackendApplication {
    public static void main(String args[]) {
        SpringApplication.run(TeaMachineBackendApplication.class, args);
    }
}
