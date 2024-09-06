package com.langtuo.teamachine.api.model.security;

import lombok.Data;

@Data
public class OSSTokenDTO {
    /**
     *
     */
    private String region;

    /**
     *
     */
    private String bucketName;

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
