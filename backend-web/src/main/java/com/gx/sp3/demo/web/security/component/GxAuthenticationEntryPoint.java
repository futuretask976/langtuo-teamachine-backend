package com.gx.sp3.demo.web.security.component;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义未登录或者token失效时的返回结果
 */
public class GxAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException e) throws IOException {
        System.out.printf("!!! GxAuthenticationEntryPoint#commence entering: %s， %s\n", e.getMessage(), e.toString());
        System.out.printf("!!! GxAuthenticationEntryPoint#commence path: %s， %s\n", request.getContextPath(), request.getServletPath());

        // 如果是前后端分离项目，这里可以返回JSON字符串提示前端登录失败
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JSONObject responseBody = new JSONObject();
        responseBody.put("loginSuccess", "false");
        response.getWriter().println(responseBody.toJSONString());
        response.getWriter().flush();
        // 如果不是前后端分离项目，这里返回/login渲染thymeleaf模板
        // 设置状态码为302
        // response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        // 设置Location头部，指定重定向的URL
        // response.setHeader("Location", request.getContextPath() + "/login");
    }
}
