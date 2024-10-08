package com.langtuo.teamachine.web.security.component;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import com.langtuo.teamachine.web.constant.WebConsts;
import com.langtuo.teamachine.web.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TeaMachineAuthFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException e) throws IOException, ServletException {
        // 设置 resp 头部信息
        response.setHeader(WebConsts.RESP_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, WebConsts.RESP_HEADER_VAL_ALL);
        response.setHeader(WebConsts.RESP_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS, WebConsts.RESP_HEADER_VAL_TRUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        TeaMachineResult<Void> result = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.LOGIN_ERR_UNAUTHENTICATED));
        response.getWriter().println(JSONObject.toJSONString(result));
        response.getWriter().flush();
    }
}
