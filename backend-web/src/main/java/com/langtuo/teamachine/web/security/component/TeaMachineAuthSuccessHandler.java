package com.langtuo.teamachine.web.security.component;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.web.constant.WebConsts;
import com.langtuo.teamachine.web.helper.JwtTokenHelper;
import com.langtuo.teamachine.web.model.LoginSuccessDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class TeaMachineAuthSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${teamachine.jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        HttpSession session = request.getSession();
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        session.setAttribute(WebConsts.SESSION_ATTR_USER, authUser);
        session.setAttribute(WebConsts.SESSION_ATTR_USERNAME, authUser.getUsername());
        session.setAttribute(WebConsts.SESSION_ATTR_AUTHORITIES, authentication.getAuthorities());

        // Set our response to OK status
        response.setHeader(WebConsts.RESP_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, WebConsts.RESP_HEADER_VAL_ALL);
        response.setHeader(WebConsts.RESP_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS, WebConsts.RESP_HEADER_VAL_TRUE);
        response.setStatus(HttpServletResponse.SC_OK);

        // 如果是前后端分离项目，这里可以返回JSON字符串提示前端登录成功
        LoginSuccessDTO dto = new LoginSuccessDTO();
        dto.setJwtToken(tokenHead + jwtTokenHelper.generateToken(authUser));
        dto.setLoginName(authUser.getUsername());
        TeaMachineResult<LoginSuccessDTO> result = TeaMachineResult.success(dto);
        response.getWriter().println(JSONObject.toJSONString(result));
        response.getWriter().flush();
        // 如果不是前后端分离项目，这里返回/welcome渲染thymeleaf模板
        // Since we have created our custom success handler, its up to us,
        // to where we will redirect the user after successfully login
        // SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        // response.sendRedirect(savedRequest == null || savedRequest.getRedirectUrl().isEmpty() ? "/welcome" : savedRequest.getRedirectUrl()); //requestUrl!=null?requestUrl:
    }
}
