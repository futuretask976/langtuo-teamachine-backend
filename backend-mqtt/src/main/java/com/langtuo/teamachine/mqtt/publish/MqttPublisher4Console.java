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

    public void send4NewTenant(String tenantCode) {
        if (StringUtils.isBlank(tenantCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_TENANT);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        mqttService.sendConsoleMsg(jsonPayload.toJSONString());
    }

    public void send4Model(String modelCode) {
        if (StringUtils.isBlank(modelCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_MODEL);
        jsonPayload.put(MqttConsts.SEND_KEY_MODEL_CODE, modelCode);
        mqttService.sendConsoleMsg(jsonPayload.toJSONString());
    }

    public void send4Machine(String tenantCode, String machineCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(machineCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_MACHINE);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_MACHINE_CODE, machineCode);
        mqttService.sendConsoleMsg(jsonPayload.toJSONString());
    }

    public void send4AccuracyTpl(String tenantCode, String templateCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(templateCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_ACCURACY_TPL);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_TEMPLATE_CODE, templateCode);
        mqttService.sendConsoleMsg(jsonPayload.toJSONString());
    }

    public void send4Menu(String tenantCode, String menuCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(menuCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_MENU);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_MENU_CODE, menuCode);
        mqttService.sendConsoleMsg(jsonPayload.toJSONString());
    }

    public void send4MenuInitList(String tenantCode, String shopCode, String machineCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(shopCode) || StringUtils.isBlank(machineCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_MENU_INIT_LIST);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_SHOP_CODE, shopCode);
        jsonPayload.put(MqttConsts.SEND_KEY_MACHINE_CODE, machineCode);
        mqttService.sendConsoleMsg(jsonPayload.toJSONString());
    }

    public void send4DrainRule(String tenantCode, String drainRuleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(drainRuleCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_DRAIN_RULE);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_DRAIN_RULE_CODE, drainRuleCode);
        mqttService.sendConsoleMsg(jsonPayload.toJSONString());
    }

    public void send4CleanRule(String tenantCode, String cleanRuleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(cleanRuleCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_CLEAN_RULE);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_CLEAN_RULE_CODE, cleanRuleCode);
        mqttService.sendConsoleMsg(jsonPayload.toJSONString());
    }

    public void send4WarningRule(String tenantCode, String warningRuleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(warningRuleCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_WARNING_RULE);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_WARNING_RULE_CODE, warningRuleCode);
        mqttService.sendConsoleMsg(jsonPayload.toJSONString());
    }
}
