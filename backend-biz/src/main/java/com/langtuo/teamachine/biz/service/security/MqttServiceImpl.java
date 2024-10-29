package com.langtuo.teamachine.biz.service.security;

import com.langtuo.teamachine.api.model.security.MqttTokenDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.security.MqttService;
import com.langtuo.teamachine.mqtt.model.MqttToken;
import com.langtuo.teamachine.mqtt.util.MqttUtils;
import org.springframework.stereotype.Component;

@Component
public class MqttServiceImpl implements MqttService {
    @Override
    public TeaMachineResult<MqttTokenDTO> getMqttToken(String tenantCode) {
        MqttToken mqttToken = MqttUtils.getMqttToken(tenantCode);

        MqttTokenDTO dto = new MqttTokenDTO();
        dto.setAccessToken(mqttToken.getAccessToken());
        dto.setAccessKey(mqttToken.getAccessKey());
        return TeaMachineResult.success(dto);
    }
}
