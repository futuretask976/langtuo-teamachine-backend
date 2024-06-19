package com.langtuo.teamachine.dao.source;

import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.interceptor.GxTableShardInterceptor;
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
    annotationClass = MySQLScan.class, // mapper接口扫描的注解
    sqlSessionFactoryRef = SQLSourceConfig.SQL_SESSION_FACTORY_NAME// mapper接口使用的session工厂
)
@Slf4j
public class SQLSourceConfig {
    public static final String DATA_SOURCE_NAME = "gxSQLDataSource";
    public static final String JDBC_TEMPLATE_NAME = "gxSQLJdbcTemplate";
    public static final String SQL_SESSION_FACTORY_NAME = "gxSQLSessionFactory";
    public static final String INTERCEPTOR_NAME = "gxTableShardInterceptor";

    @Bean(name = DATA_SOURCE_NAME)
    @Primary
    public DataSource gxSQLDatasource() {
        System.out.println("$$$$$$$$$$ GxSQLSourceConfig#gxSQLDatasource entering");
        return DataSourceBuilder.create()
                .url("jdbc:mysql://rm-cn-28t3ppz9e0001yho.rwlb.rds.aliyuncs.com:3306/gx_mysql_demo?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true")
                .username("miya")
                .password("password@1")
                .build();
    }

    @Bean(name = SQL_SESSION_FACTORY_NAME)
    @Primary
    public SqlSessionFactory gxSQLSessionFactory(@Qualifier(DATA_SOURCE_NAME) DataSource mysqlDataSource)
            throws Exception {
        System.out.println("$$$$$$$$$$ GxSQLSourceConfig#gxSQLSessionFactory entering");
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(mysqlDataSource);
        factory.setVfs(SpringBootVFS.class);

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        factory.setConfigLocation(resourceLoader.getResource("mybatis/mybatis-config.xml"));
        return factory.getObject();
    }

    @Bean(name = JDBC_TEMPLATE_NAME)
    public JdbcTemplate gxSQLJdbcTemplate(@Qualifier(DATA_SOURCE_NAME) DataSource mysqlDataSource) {
        System.out.println("$$$$$$$$$$ GxSQLSourceConfig#gxSQLJdbcTemplate entering");
        return new JdbcTemplate(mysqlDataSource);
    }

    @Bean(name = INTERCEPTOR_NAME)
    public Interceptor gxTableShardInterceptor() {
        return new GxTableShardInterceptor();
    }
}

