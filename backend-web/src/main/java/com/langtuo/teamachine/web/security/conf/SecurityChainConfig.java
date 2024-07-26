package com.langtuo.teamachine.web.security.conf;

import com.langtuo.teamachine.web.security.component.TeaMachineJwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * SpringSecurity相关配置，仅用于配置SecurityFilterChain
 * Created by macro on 2019/11/5.
 */
@Configuration
@EnableWebSecurity
public class SecurityChainConfig {
    @Autowired
    private AccessDeniedHandler teaMachineAccessDeniedHandler;

    @Autowired
    private AuthenticationEntryPoint teaMachineAuthenticationEntryPoint;

    @Autowired
    private LogoutSuccessHandler teaMachineLogoutSuccessHandler;

    @Autowired
    private UserDetailsService teaMachineUserDetailsService;

    @Autowired
    private AuthenticationSuccessHandler teaMachineAuthSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler teaMachineAuthFailureHandler;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Autowired
    private TeaMachineJwtAuthenticationTokenFilter teaMachineJwtAuthenticationTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

        // 登录设置
        httpSecurity.formLogin(formLogin -> {
            formLogin
                    .loginPage("/login") // Login page will be accessed through this endpoint. We will create a controller method for this.
                    .loginProcessingUrl("/login-processing") // This endpoint will be mapped internally. This URL will be our Login form post action.
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll() // We re permitting all for login page
                    .successHandler(teaMachineAuthSuccessHandler) // .defaultSuccessUrl("/welcome") // If the login is successful, user will be redirected to this URL.
                    .failureHandler(teaMachineAuthFailureHandler);
                    // .failureUrl("/login?error=true"); // If the user fails to login, application will redirect the user to this endpoint
        });

        // 登出设置
        httpSecurity.logout(logout ->
                logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(teaMachineLogoutSuccessHandler)
                        //.logoutSuccessUrl("/bye")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID") // 如果你使用cookie来传递session id
        );

        // 允许跨域请求的OPTIONS请求
        httpSecurity.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll();

        // 不需要保护的资源路径允许访问
        for (String ignoreUrl : ignoreUrlsConfig.getUrls()) {
            System.out.printf("$$$$$ SecurityChainConfig#filterChain ignoreUrl=%s\n", ignoreUrl);
            httpSecurity.authorizeRequests().antMatchers(ignoreUrl).permitAll();
        }

        // token简单验证
        httpSecurity.addFilterBefore(teaMachineJwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 配置授权处理，授权管理器可以配置多个
        httpSecurity.authorizeRequests()
                .antMatchers("/test002").hasRole("USER")
                .antMatchers("/test003").hasAnyRole("USER", "ADMIN")
                .antMatchers("/test004").hasAnyRole("ADMIN")
                .anyRequest().authenticated();

        // 异常处理
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(teaMachineAccessDeniedHandler)
                .authenticationEntryPoint(teaMachineAuthenticationEntryPoint);

        return httpSecurity.build();
    }
}
