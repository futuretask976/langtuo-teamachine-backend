package com.langtuo.teamachine.api.model.securityset;

import lombok.Data;

@Data
public class OSSTokenDTO {
    /**
     *
     */
    private String accessKeyId;

    /**
     *
     */
    private String accessKeySecret;

    /**
     *
     */
    private String securityToken;

    /**
     *
     */
    private String requestId;

    /**
     *
     */
    private String expiration;
}
