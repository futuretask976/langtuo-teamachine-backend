package com.gx.sp3.demo.web;

import com.gx.sp3.demo.gtmf.annotation.AbilityImpl;
import com.gx.sp3.demo.gtmf.annotation.Activity;
import com.gx.sp3.demo.gtmf.annotation.BusinessDef;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
        basePackages = {"com.gx"},
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Activity.class, AbilityImpl.class, BusinessDef.class})
        }
)
@MapperScan("com.gx.sp3.demo.dao.mapper")
public class GxSpringBoot2DemoApplication {
    public static void main(String args[]) {
        System.out.printf("!!! GxSpringBoot3DemoApplication#main entering");
        SpringApplication.run(GxSpringBoot2DemoApplication.class, args);
    }
}
