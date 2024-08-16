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

    public void sendConsoleMsg4Menu(String tenantCode, String menuCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(menuCode)) {
            return;
        }

        JSONObject payloadJSON = new JSONObject();
        payloadJSON.put(MqttConsts.SEND_KEY_TITLE, MqttConsts.MSG_TITLE_PREPARE_MENU);
        payloadJSON.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        payloadJSON.put(MqttConsts.SEND_KEY_MENU_CODE, menuCode);
        mqttService.sendConsoleMsg(payloadJSON.toJSONString());
    }

    public void sendConsoleMsg4OpenRule(String tenantCode, String openRuleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(openRuleCode)) {
            return;
        }

        JSONObject payloadJSON = new JSONObject();
        payloadJSON.put(MqttConsts.SEND_KEY_TITLE, MqttConsts.MSG_TITLE_PREPARE_OPEN_RULE);
        payloadJSON.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        payloadJSON.put(MqttConsts.SEND_KEY_OPEN_RULE_CODE, openRuleCode);
        mqttService.sendConsoleMsg(payloadJSON.toJSONString());
    }

    public void sendConsoleMsg4CleanRule(String tenantCode, String cleanRuleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(cleanRuleCode)) {
            return;
        }

        JSONObject payloadJSON = new JSONObject();
        payloadJSON.put(MqttConsts.SEND_KEY_TITLE, MqttConsts.MSG_TITLE_PREPARE_CLEAN_RULE);
        payloadJSON.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        payloadJSON.put(MqttConsts.SEND_KEY_CLEAN_RULE_CODE, cleanRuleCode);
        mqttService.sendConsoleMsg(payloadJSON.toJSONString());
    }

    public void sendConsoleMsg4WarningRule(String tenantCode, String warningRuleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(warningRuleCode)) {
            return;
        }

        JSONObject payloadJSON = new JSONObject();
        payloadJSON.put(MqttConsts.SEND_KEY_TITLE, MqttConsts.MSG_TITLE_PREPARE_WARNING_RULE);
        payloadJSON.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        payloadJSON.put(MqttConsts.SEND_KEY_WARNING_RULE_CODE, warningRuleCode);
        mqttService.sendConsoleMsg(payloadJSON.toJSONString());
    }
}
