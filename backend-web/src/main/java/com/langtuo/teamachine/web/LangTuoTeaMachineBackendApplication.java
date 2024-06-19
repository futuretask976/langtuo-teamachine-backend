package com.langtuo.teamachine.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
        basePackages = {"com.langtuo.teamachine"}
)
@MapperScan("com.langtuo.teamachine.dao.mapper")
public class LangTuoTeaMachineBackendApplication {
    public static void main(String args[]) {
        SpringApplication.run(LangTuoTeaMachineBackendApplication.class, args);
    }
}
