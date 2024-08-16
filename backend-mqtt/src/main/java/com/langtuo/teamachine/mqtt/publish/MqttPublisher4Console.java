package com.langtuo.teamachine.mqtt.publish;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.mqtt.MqttService;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MqttPublisher4Console {
    @Resource
    private MqttService mqttService;

    public void send4Menu(String tenantCode, String menuCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(menuCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_TITLE, MqttConsts.MSG_TITLE_PREPARE_MENU);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_MENU_CODE, menuCode);
        mqttService.sendConsoleMsg(jsonPayload.toJSONString());
    }

    public void send4OpenRule(String tenantCode, String openRuleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(openRuleCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_TITLE, MqttConsts.MSG_TITLE_PREPARE_OPEN_RULE);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_OPEN_RULE_CODE, openRuleCode);
        mqttService.sendConsoleMsg(jsonPayload.toJSONString());
    }

    public void send4CleanRule(String tenantCode, String cleanRuleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(cleanRuleCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_TITLE, MqttConsts.MSG_TITLE_PREPARE_CLEAN_RULE);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_CLEAN_RULE_CODE, cleanRuleCode);
        mqttService.sendConsoleMsg(jsonPayload.toJSONString());
    }

    public void send4WarningRule(String tenantCode, String warningRuleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(warningRuleCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_TITLE, MqttConsts.MSG_TITLE_PREPARE_WARNING_RULE);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_WARNING_RULE_CODE, warningRuleCode);
        mqttService.sendConsoleMsg(jsonPayload.toJSONString());
    }
}
