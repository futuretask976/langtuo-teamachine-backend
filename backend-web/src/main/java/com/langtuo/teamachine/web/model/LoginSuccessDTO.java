package com.langtuo.teamachine.web.model;

import lombok.Data;

/**
 * @author Jiaqing
 */
@Data
public class LoginSuccessDTO {
    /**
     * 登录用 JWT 令牌
     */
    private String jwtToken;

    /**
     * 登录名称
     */
    private String loginName;
}
