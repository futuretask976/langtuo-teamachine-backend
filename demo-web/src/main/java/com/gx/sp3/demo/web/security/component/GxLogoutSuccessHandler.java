package com.gx.sp3.demo.web.security.component;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GxLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.printf("!!! GxLogoutSuccessHandler#onLogoutSuccess entering\n");
        // 清除会话
        request.getSession().invalidate();

        // 删除cookie
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        // 如果是前后端分离项目，这里可以返回JSON字符串提示前端注销成功
        JSONObject responseBody = new JSONObject();
        responseBody.put("logoutSuccess", "true");
        response.getWriter().println(responseBody.toJSONString());
        response.flushBuffer();
        // 如果不是前后端分离项目，这里返回/bye渲染thymeleaf模板
        // Since we have created our custom success handler, its up to us,
        // to where we will redirect the user after successfully login
        // SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        // response.sendRedirect("/bye");
    }
}
