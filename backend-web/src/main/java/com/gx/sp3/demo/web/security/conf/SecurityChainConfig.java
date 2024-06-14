package com.gx.sp3.demo.web.security.conf;

import com.gx.sp3.demo.web.security.component.GxAuthSuccessHandler;
import com.gx.sp3.demo.web.security.component.GxJwtAuthenticationTokenFilter;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * SpringSecurity相关配置，仅用于配置SecurityFilterChain
 * Created by macro on 2019/11/5.
 */
@Configuration
@EnableWebSecurity
public class SecurityChainConfig {
    @Autowired
    private AccessDeniedHandler gxAccessDeniedHandler;

    @Autowired
    private AuthenticationEntryPoint gxAuthenticationEntryPoint;

    @Autowired
    private LogoutSuccessHandler gxLogoutSuccessHandler;

    @Autowired
    private UserDetailsService gxUserDetailsService;

    @Autowired
    private AuthenticationSuccessHandler gxAuthSuccessHandler;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Autowired
    private GxJwtAuthenticationTokenFilter gxJwtAuthenticationTokenFilter;

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
                    .successHandler(gxAuthSuccessHandler) // .defaultSuccessUrl("/welcome") // If the login is successful, user will be redirected to this URL.
                    .failureUrl("/login?error=true"); // If the user fails to login, application will redirect the user to this endpoint
        });

        // 登出设置
        httpSecurity.logout(logout ->
                logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(gxLogoutSuccessHandler)
                        //.logoutSuccessUrl("/bye")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID") // 如果你使用cookie来传递session id
        );

        // 允许跨域请求的OPTIONS请求
        httpSecurity.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll();

        // 不需要保护的资源路径允许访问
        for (String url : ignoreUrlsConfig.getUrls()) {
            httpSecurity.authorizeRequests().antMatchers(url).permitAll();
        }

        // token简单验证
        httpSecurity.addFilterBefore(gxJwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 配置授权处理，授权管理器可以配置多个
        httpSecurity.authorizeRequests()
                .antMatchers("/test002").hasRole("USER")
                .antMatchers("/test003").hasAnyRole("USER", "ADMIN")
                .antMatchers("/test004").hasAnyRole("ADMIN")
                .anyRequest().authenticated();

        // 异常处理
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(gxAccessDeniedHandler)
                .authenticationEntryPoint(gxAuthenticationEntryPoint);

        return httpSecurity.build();
    }
}
