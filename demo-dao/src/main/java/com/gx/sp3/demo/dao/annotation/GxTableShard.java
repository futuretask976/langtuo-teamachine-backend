package com.gx.sp3.demo.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface GxTableShard {
    // 是否开启建表操作
    boolean tableShardOpen() default false;

    // 表名
    String shardName();

    // 对应参数
    String[] columns();

    // 默认表名
    String defaultName();
}
