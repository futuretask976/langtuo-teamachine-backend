package com.langtuo.teamachine.web.security.component;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.web.helper.GxJwtTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class TeaMachineAuthFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private GxJwtTokenHelper gxJwtTokenHelper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException e) throws IOException, ServletException {
        System.out.printf("!!! TeaMachineAuthFailureHandler#onAuthenticationFailure entering, %s\n", gxJwtTokenHelper);

        response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        LangTuoResult<Void> result = LangTuoResult.error(ErrorEnum.LOGIN_ERR_UNAUTHENTICATED);
        response.getWriter().println(JSONObject.toJSONString(result));
        response.getWriter().flush();
    }
}
