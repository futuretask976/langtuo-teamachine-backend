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
            httpSecurity.authorizeRequests().antMatchers(ignoreUrl).permitAll();
        }

        // token简单验证
        httpSecurity.addFilterBefore(teaMachineJwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 配置授权处理，授权管理器可以配置多个
        httpSecurity.authorizeRequests()
                // 设备
                .antMatchers("/deviceset/model/**").hasAnyRole("model_mgt", "machine")
                .antMatchers("/deviceset/deploy/**").hasAnyRole("deploy_mgt")
                .antMatchers("/deviceset/machine/**").hasAnyRole("machine_mgt")
                // 用户
                .antMatchers("/userset/role/list/**").hasAnyRole("role_mgt", "org_mgt", "admin_mgt")
                .antMatchers("/userset/permitact/list/**").hasAnyRole("role_mgt", "org_mgt", "admin_mgt")
                .antMatchers("/userset/org/list/**").hasAnyRole("role_mgt", "org_mgt", "admin_mgt")
                .antMatchers("/userset/tenant/**").hasAnyRole("tenant_mgt")
                .antMatchers("/userset/org/**").hasAnyRole("org_mgt")
                .antMatchers("/userset/permitact/**").hasAnyRole("permit_act_mgt")
                .antMatchers("/userset/role/**").hasAnyRole("role_mgt")
                .antMatchers("/userset/admin/**").hasAnyRole("admin_mgt")
                // 店铺
                .antMatchers("/shopset/group/list/**").hasAnyRole("shop_group_mgt", "shop_mgt")
                .antMatchers("/shopset/group/**").hasAnyRole("shop_group_mgt")
                .antMatchers("/shopset/shop/**").hasAnyRole("shop_mgt")
                // 饮品生产
                .antMatchers("/drinkset/topping/type/list/**").hasAnyRole("topping_type_mgt", "topping_mgt")
                .antMatchers("/drinkset/tea/type/list/**").hasAnyRole("tea_type_mgt", "tea_mgt")
                .antMatchers("/drinkset/topping/type/**").hasAnyRole("topping_type_mgt")
                .antMatchers("/drinkset/topping/**").hasAnyRole("topping_mgt")
                .antMatchers("/drinkset/spec/**").hasAnyRole("spec_mgt")
                .antMatchers("/drinkset/tea/type/**").hasAnyRole("tea_type_mgt")
                .antMatchers("/drinkset/tea/**").hasAnyRole("tea_mgt")
                .antMatchers("/drinkset/accuracy/**").hasAnyRole("accuracy_mgt", "machine")
                // 菜单
                .antMatchers("/menuset/series/list/**").hasAnyRole("series_mgt", "menu_mgt")
                .antMatchers("/menuset/series/**").hasAnyRole("series_mgt")
                .antMatchers("/menuset/menu/**").hasAnyRole("menu_mgt", "machine")
                // 食安规则
                .antMatchers("/ruleset/drain/**").hasAnyRole("drain_rule_mgt", "machine")
                .antMatchers("/ruleset/clean/**").hasAnyRole("clean_rule_mgt", "machine")
                .antMatchers("/ruleset/warning/**").hasAnyRole("warning_rule_mgt", "machine")
                // 日常记录
                .antMatchers("/recordset/clean/**").hasAnyRole("clean_rec_mgt")
                .antMatchers("/recordset/invalid/**").hasAnyRole("invalid_rec_mgt")
                .antMatchers("/recordset/order/**").hasAnyRole("order_rec_mgt")
                .antMatchers("/recordset/supply/**").hasAnyRole("supply_rec_mgt")
                .anyRequest().authenticated();

        // 异常处理
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(teaMachineAccessDeniedHandler)
                .authenticationEntryPoint(teaMachineAuthenticationEntryPoint);

        return httpSecurity.build();
    }
}
