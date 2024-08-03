package com.langtuo.teamachine.dao.config;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.interceptor.LangTuoTableShardInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 *
 */
@Configuration
@MapperScan(
    basePackages = "com.langtuo.teamachine.dao.mapper", // mapper接口扫描的包
    annotationClass = TeaMachineSQLScan.class, // mapper接口扫描的注解
    sqlSessionFactoryRef = SQLConfig.SESSION_FACTORY_NAME// mapper接口使用的session工厂
)
@Slf4j
public class SQLConfig {
    public static final String DATA_SOURCE_NAME = "teaMachineDataSource";
    public static final String JDBC_TEMPLATE_NAME = "teaMachineJdbcTemplate";
    public static final String SESSION_FACTORY_NAME = "teaMachineSessionFactory";
    public static final String TABLE_SHARD_INTERCEPTOR_NAME = "teaMachineTableShardInterceptor";

    @Bean(name = DATA_SOURCE_NAME)
    @Primary
    public DataSource gxSQLDatasource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://rm-cn-28t3ppz9e0001yho.rwlb.rds.aliyuncs.com:3306/gx_mysql_demo?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true")
                .username("miya")
                .password("password@1")
                .build();
    }

    @Bean(name = SESSION_FACTORY_NAME)
    @Primary
    public SqlSessionFactory gxSQLSessionFactory(@Qualifier(DATA_SOURCE_NAME) DataSource mysqlDataSource)
            throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(mysqlDataSource);
        factory.setVfs(SpringBootVFS.class);

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        factory.setConfigLocation(resourceLoader.getResource("mybatis/mybatis-config.xml"));
        return factory.getObject();
    }

    @Bean(name = JDBC_TEMPLATE_NAME)
    public JdbcTemplate gxSQLJdbcTemplate(@Qualifier(DATA_SOURCE_NAME) DataSource mysqlDataSource) {
        return new JdbcTemplate(mysqlDataSource);
    }

    @Bean(name = TABLE_SHARD_INTERCEPTOR_NAME)
    public Interceptor gxTableShardInterceptor() {
        return new LangTuoTableShardInterceptor();
    }
}

