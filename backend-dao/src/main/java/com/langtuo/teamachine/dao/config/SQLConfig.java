package com.langtuo.teamachine.dao.config;

import com.langtuo.teamachine.dao.interceptor.LangTuoTableShardInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
@Slf4j
public class SQLConfig {
    public static final String TABLE_SHARD_INTERCEPTOR_NAME = "teaMachineTableShardInterceptor";

    @Bean(name = TABLE_SHARD_INTERCEPTOR_NAME)
    public Interceptor getTableShardInterceptor() {
        return new LangTuoTableShardInterceptor();
    }
}

