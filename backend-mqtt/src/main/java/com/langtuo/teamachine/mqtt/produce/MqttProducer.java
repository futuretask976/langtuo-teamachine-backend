package com.langtuo.teamachine.mqtt.produce;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.mqtt.server.ServerProducer;
import com.alibaba.mqtt.server.config.ChannelConfig;
import com.alibaba.mqtt.server.config.ProducerConfig;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.produce.callback.MqttSendCallback;
import com.langtuo.teamachine.mqtt.util.MqttUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class MqttProducer implements InitializingBean {
    private ServerProducer serverProducer;

    @Override
    public void afterPropertiesSet() throws IOException, TimeoutException {
        if (serverProducer == null) {
            synchronized (MqttProducer.class) {
                if (serverProducer == null) {
                    init();
                }
            }
        }
    }

    @PreDestroy
    public void onDestroy() {
        if (serverProducer != null) {
            try {
                log.info("$$$$$ MqttProducer#onDestroy entering");
                serverProducer.stop();
            } catch (IOException e) {
                log.error("MqttProducer|stopServerProducer|fatal|" + e.getMessage(), e);
            }
        }
    }

    public void init() throws IOException, TimeoutException {
        ChannelConfig channelConfig = MqttUtils.getChannelConfig();
        serverProducer = new ServerProducer(channelConfig, new ProducerConfig());
        serverProducer.start();
    }

    public void sendToConsole(String topic, String payload) {
        try {
            serverProducer.sendMessage(topic, payload.getBytes(StandardCharsets.UTF_8), new MqttSendCallback());
        } catch (IOException e) {
            log.error("MqttProducer|sendToConsole|fatal|" + e.getMessage(), e);
        }
    }

    public void sendToConsole4NewTenant(String tenantCode) {
        if (StringUtils.isBlank(tenantCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_TENANT);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        sendToConsole(MqttConsts.CONSOLE_PARENT_TOPIC, jsonPayload.toJSONString());
    }

    public void sendToConsole4Model(String modelCode) {
        if (StringUtils.isBlank(modelCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_MODEL);
        jsonPayload.put(MqttConsts.SEND_KEY_MODEL_CODE, modelCode);
        sendToConsole(MqttConsts.CONSOLE_PARENT_TOPIC, jsonPayload.toJSONString());
    }

    public void sendToConsole4Machine(String tenantCode, String machineCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(machineCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_MACHINE);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_MACHINE_CODE, machineCode);
        sendToConsole(MqttConsts.CONSOLE_PARENT_TOPIC, jsonPayload.toJSONString());
    }

    public void sendToConsole4AccuracyTpl(String tenantCode, String templateCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(templateCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_ACCURACY_TPL);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_TEMPLATE_CODE, templateCode);
        sendToConsole(MqttConsts.CONSOLE_PARENT_TOPIC, jsonPayload.toJSONString());
    }

    public void sendToConsole4Menu(String tenantCode, String menuCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(menuCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_MENU);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_MENU_CODE, menuCode);
        sendToConsole(MqttConsts.CONSOLE_PARENT_TOPIC, jsonPayload.toJSONString());
    }

    public void sendToConsole4MenuInitList(String tenantCode, String shopCode, String machineCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(shopCode) || StringUtils.isBlank(machineCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_MENU_INIT_LIST);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_SHOP_CODE, shopCode);
        jsonPayload.put(MqttConsts.SEND_KEY_MACHINE_CODE, machineCode);
        sendToConsole(MqttConsts.CONSOLE_PARENT_TOPIC, jsonPayload.toJSONString());
    }

    public void sendToConsole4DrainRule(String tenantCode, String drainRuleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(drainRuleCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_DRAIN_RULE);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_DRAIN_RULE_CODE, drainRuleCode);
        sendToConsole(MqttConsts.CONSOLE_PARENT_TOPIC, jsonPayload.toJSONString());
    }

    public void sendToConsole4CleanRule(String tenantCode, String cleanRuleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(cleanRuleCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_CLEAN_RULE);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_CLEAN_RULE_CODE, cleanRuleCode);
        sendToConsole(MqttConsts.CONSOLE_PARENT_TOPIC, jsonPayload.toJSONString());
    }

    public void sendToConsole4WarningRule(String tenantCode, String warningRuleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(warningRuleCode)) {
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_PREPARE_WARNING_RULE);
        jsonPayload.put(MqttConsts.SEND_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(MqttConsts.SEND_KEY_WARNING_RULE_CODE, warningRuleCode);
        sendToConsole(MqttConsts.CONSOLE_PARENT_TOPIC, jsonPayload.toJSONString());
    }

    public void sendP2PMsgByTenant(String tenantCode, String machineCode, String payload) {
        String topic = tenantCode + MqttConsts.TENANT_PARENT_P2P_TOPIC_POSTFIX
                + MqttConsts.TOPIC_SEPERATOR + machineCode;
        try {
            serverProducer.sendMessage(topic, payload.getBytes(StandardCharsets.UTF_8), new MqttSendCallback());
        } catch (IOException e) {
            log.error("MqttProducer|sendP2PMsgByTenant|fatal|" + e.getMessage(), e);
        }
    }

    public void sendBroadcastMsgByTenant(String tenantCode, String payload) {
        String topic = tenantCode + MqttConsts.TENANT_PARENT_TOPIC_POSTFIX;
        try {
            serverProducer.sendMessage(topic, payload.getBytes(StandardCharsets.UTF_8), new MqttSendCallback());
        } catch (IOException e) {
            log.error("MqttProducer|sendBroadcastMsgByTenant|fatal|" + e.getMessage(), e);
        }
    }
}
