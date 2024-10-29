package com.langtuo.teamachine.api.model.security;

import lombok.Data;

@Data
public class MqttTokenDTO {
    /**
     * MQTT 连接用户名
     */
    private String accessKey;

    /**
     * MQTT 令牌
     */
    private String accessToken;
}
