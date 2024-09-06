package com.langtuo.teamachine.web.security.component;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.web.constant.WebConsts;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义未登录或者token失效时的返回结果（登录后，如果重启服务器，访问需要授权的页面，变回被重定向到这里）
 */
public class TeaMachineAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException e) throws IOException {
        // 如果是前后端分离项目，这里可以返回JSON字符串提示前端登录失败
        response.setHeader(WebConsts.RESP_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, WebConsts.RESP_HEADER_VAL_ALL);
        response.setHeader(WebConsts.RESP_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS, WebConsts.RESP_HEADER_VAL_TRUE);
        response.setHeader(WebConsts.RESP_HEADER_CACHE_CONTROL,WebConsts.RESP_HEADER_VAL_NO_CACHE);
        response.setCharacterEncoding(WebConsts.RESP_HEADER_VAL_ENCODING_UTF8);
        response.setContentType(WebConsts.RESP_HEADER_VAL_CONT_TYPE_APPLICATIONJSON);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseBody = new JSONObject();
        responseBody.put(WebConsts.JSON_KEY_LOGIN_SUCCESS, WebConsts.JSON_VAL_FALSE);
        response.getWriter().println(responseBody.toJSONString());
        response.getWriter().flush();
        // 如果不是前后端分离项目，这里返回/login渲染thymeleaf模板
        // 设置状态码为302
        // response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        // 设置Location头部，指定重定向的URL
        // response.setHeader("Location", request.getContextPath() + "/login");
    }
}
