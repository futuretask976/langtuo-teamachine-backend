package com.langtuo.teamachine.web.security.component;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.web.model.LogoutSuccessDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TeaMachineLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.printf("!!! TeaMachineLogoutSuccessHandler#onLogoutSuccess entering\n");
        // 清除会话
        request.getSession().invalidate();

        // 删除cookie
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);

        // 如果是前后端分离项目，这里可以返回JSON字符串提示前端注销成功
        LogoutSuccessDTO dto = new LogoutSuccessDTO();
        LangTuoResult<LogoutSuccessDTO> result = LangTuoResult.success(dto);
        response.getWriter().println(JSONObject.toJSONString(result));
        response.getWriter().flush();
        // 如果不是前后端分离项目，这里返回/bye渲染thymeleaf模板
        // Since we have created our custom success handler, its up to us,
        // to where we will redirect the user after successfully login
        // SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        // response.sendRedirect("/bye");
    }
}
