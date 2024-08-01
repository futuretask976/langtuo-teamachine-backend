package com.langtuo.teamachine.web.security.component;

import com.langtuo.teamachine.web.helper.JwtTokenHelper;
import com.langtuo.teamachine.web.security.service.TeaMachineUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT登录授权过滤器
 */
@Slf4j
public class TeaMachineJwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private TeaMachineUserDetailService teaMachineUserDetailService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead4Admin}")
    private String tokenHead4Admin;

    @Value("${jwt.tokenHead4Machine}")
    private String tokenHead4Machine;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(this.tokenHeader);
        log.info("$$$$$ authHeader=" + authHeader + ", url=" + request.getRequestURL());
        if (authHeader != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            boolean isAdminToken = false;
            if (authHeader.startsWith(this.tokenHead4Admin)) {
                isAdminToken = true;
            }

            String authToken = isAdminToken ? authHeader.substring(this.tokenHead4Admin.length())
                    : authHeader.substring(this.tokenHead4Machine.length());
            String tenantCode = jwtTokenHelper.getTenantCodeFromToken(authToken);
            String userName = jwtTokenHelper.getUserNameFromToken(authToken);
            UserDetails userDetails = isAdminToken
                    ? this.teaMachineUserDetailService.loadAdminUserDetails(tenantCode, userName)
                            : this.teaMachineUserDetailService.loadMachineUserDetails(tenantCode, userName);

            if (jwtTokenHelper.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
