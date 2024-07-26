package com.langtuo.teamachine.web.security.component;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.LoginSuccessDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.web.helper.GxJwtTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class TeaMachineAuthSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private GxJwtTokenHelper gxJwtTokenHelper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        System.out.printf("!!! GxAuthSuccessHandler#onAuthenticationSuccess entering, %s\n", gxJwtTokenHelper);
        HttpSession session = request.getSession();
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        session.setAttribute("user", authUser);
        session.setAttribute("username", authUser.getUsername());
        session.setAttribute("authorities", authentication.getAuthorities());

        // Set our response to OK status
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);

        // 如果是前后端分离项目，这里可以返回JSON字符串提示前端登录成功
        LoginSuccessDTO dto = new LoginSuccessDTO();
        dto.setToken(gxJwtTokenHelper.generateToken(authUser));
        LangTuoResult<LoginSuccessDTO> result = LangTuoResult.success(dto);
        response.getWriter().println(JSONObject.toJSONString(result));
        response.getWriter().flush();
        // 如果不是前后端分离项目，这里返回/welcome渲染thymeleaf模板
        // Since we have created our custom success handler, its up to us,
        // to where we will redirect the user after successfully login
        // SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        // response.sendRedirect(savedRequest == null || savedRequest.getRedirectUrl().isEmpty() ? "/welcome" : savedRequest.getRedirectUrl()); //requestUrl!=null?requestUrl:
    }
}
