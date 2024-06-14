package com.gx.sp3.demo.web.security.conf;

import com.gx.sp3.demo.web.helper.GxJwtTokenHelper;
import com.gx.sp3.demo.web.security.component.*;
import com.gx.sp3.demo.web.security.encoder.MD5PasswordEncoder;
import com.gx.sp3.demo.web.security.service.GxUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class SecurityBeanConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.printf("!!! SecurityBaseConfig#passwordEncoder entering\n");
        return new MD5PasswordEncoder();
    }

    @Bean
    public UserDetailsService gxUserDetailService() {
        System.out.printf("!!! SecurityBaseConfig#userDetailsService entering\n");
        return new GxUserDetailService();
    }

    @Bean
    public AccessDeniedHandler restfulAccessDeniedHandler() {
        System.out.printf("!!! SecurityBaseConfig#restfulAccessDeniedHandler entering\n");
        return new GxAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint restfulAuthenticationEntryPoint() {
        System.out.printf("!!! SecurityBaseConfig#restAuthenticationEntryPoint entering\n");
        return new GxAuthenticationEntryPoint();
    }

    @Bean
    public IgnoreUrlsConfig ignoreUrlsConfig() {
        System.out.printf("!!! SecurityBaseConfig#ignoreUrlsConfig entering\n");
        return new IgnoreUrlsConfig();
    }

    @Bean
    public GxJwtAuthenticationTokenFilter gxJwtAuthenticationTokenFilter() {
        return new GxJwtAuthenticationTokenFilter();
    }

    @Bean
    public GxJwtTokenHelper gxJwtTokenHelper() {
        return new GxJwtTokenHelper();
    }

    /**
     * 允许跨域调用的过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许所有域名进行跨域调用
        config.addAllowedOriginPattern("*");
        // 允许跨越发送cookie
        config.setAllowCredentials(true);
        // 放行全部原始头信息
        config.addAllowedHeader("*");
        // 允许所有请求方法跨域调用
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public AuthenticationSuccessHandler authSuccessHandler() {
        return new GxAuthSuccessHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new GxLogoutSuccessHandler();
    }

//    @Bean
//    public JwtTokenHelper jwtTokenUtil() {
//        System.out.printf("!!! SecurityBaseConfig#jwtTokenUtil entering\n");
//        return new JwtTokenHelper();
//    }

//    @Bean
//    public GxAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
//        System.out.printf("!!! SecurityBaseConfig#jwtAuthenticationTokenFilter entering\n");
//        return new GxAuthenticationTokenFilter();
//    }

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        System.out.printf("!!! SecurityBaseConfig#authenticationProvider entering\n");
//        return new GxAuthenticationProvider();
//    }
}
