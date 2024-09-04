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
    /**
     * MQTT 发送者实例
     */
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
                log.error("mqttProducer|stopServerProducer|fatal|" + e.getMessage(), e);
            }
        }
    }

    public void init() throws IOException, TimeoutException {
        ChannelConfig channelConfig = MqttUtils.getChannelConfig();
        serverProducer = new ServerProducer(channelConfig, new ProducerConfig());
        serverProducer.start();
    }

    public void sendP2PMsgByTenant(String tenantCode, String machineCode, String payload) {
        machineCode = "machine_333";
        String topic = tenantCode + MqttConsts.TENANT_PARENT_P2P_TOPIC_POSTFIX + machineCode;
        try {
            log.info("$$$$$ MqttProducer#sendP2PMsgByTenant topic=" + topic + ", payload=" + payload);
            serverProducer.sendMessage(topic, payload.getBytes(StandardCharsets.UTF_8), new MqttSendCallback());
        } catch (IOException e) {
            log.error("mqttProducer|sendP2PMsgByTenant|fatal|" + e.getMessage(), e);
        }
    }

    public void sendBroadcastMsgByTenant(String tenantCode, String payload) {
        String topic = tenantCode + MqttConsts.TENANT_PARENT_TOPIC_POSTFIX + MqttConsts.TOPIC_SEPERATOR + "broadcast";
        try {
            log.info("$$$$$ MqttProducer#sendBroadcastMsgByTenant topic=" + topic + ", payload=" + payload);
            serverProducer.sendMessage(topic, payload.getBytes(StandardCharsets.UTF_8), new MqttSendCallback());
        } catch (IOException e) {
            log.error("mqttProducer|sendBroadcastMsgByTenant|fatal|" + e.getMessage(), e);
        }
    }
}
