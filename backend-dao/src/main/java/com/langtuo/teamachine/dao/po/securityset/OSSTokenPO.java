package com.langtuo.teamachine.dao.po.securityset;

import lombok.Data;

@Data
public class OSSTokenPO {
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

    @Override
    public String toString() {
        return "STSPO{" +
                "accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='" + accessKeySecret + '\'' +
                ", securityToken='" + securityToken + '\'' +
                ", requestId='" + requestId + '\'' +
                ", expiration='" + expiration + '\'' +
                '}';
    }
}
