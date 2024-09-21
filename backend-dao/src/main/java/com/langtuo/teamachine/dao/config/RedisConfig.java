package com.langtuo.teamachine.dao.config;

import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@Slf4j
public class RedisConfig {
    /**
     * redis 命令超时时间
     */
    private static final int REDIS_COMMAND_TIMEOUT = 10;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    // @Value("${spring.redis.username}")
    // private String userName;

    @Value("${spring.redis.password}")
    private String password;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(ClientResources.class)
    public DefaultClientResources lettuceClientResources() {
        return DefaultClientResources.create();
    }

    /**
     * GenericObjectPoolConfig 连接池配置
     *
     * @return
     */
    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(30);
        genericObjectPoolConfig.setMinIdle(1);
        genericObjectPoolConfig.setMaxTotal(1000);
        genericObjectPoolConfig.setMaxWaitMillis(5000);
        return genericObjectPoolConfig;
    }

    /**
     * 配置redis连接工厂
     * @return
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(DefaultClientResources defaultClientResources,
            GenericObjectPoolConfig genericObjectPoolingConfig) {
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        // configuration.setUsername(userName);
        serverConfig.setPassword(password);

//        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
//                .clientResources(defaultClientResources)
//                .commandTimeout(Duration.ofSeconds(REDIS_COMMAND_TIMEOUT))
//                .shutdownTimeout(Duration.ZERO)
////                .useSsl()
////                .disablePeerVerification()
//                .build();

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .clientResources(defaultClientResources)
                .commandTimeout(Duration.ofSeconds(REDIS_COMMAND_TIMEOUT))
                .shutdownTimeout(Duration.ZERO)
                .poolConfig(genericObjectPoolingConfig)
//                .useSsl()
//                .disablePeerVerification()
                .build();

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(serverConfig, clientConfig);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}

