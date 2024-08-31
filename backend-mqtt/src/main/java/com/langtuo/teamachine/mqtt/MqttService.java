package com.langtuo.teamachine.mqtt;

import com.langtuo.teamachine.api.service.user.TenantMgtService;
import com.langtuo.teamachine.mqtt.config.MqttConfig;
import com.langtuo.teamachine.mqtt.concurrent.ExeService4Publish;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.util.MqttUtils;
import com.langtuo.teamachine.mqtt.consume.MqttConsumer;
import com.langtuo.teamachine.mqtt.wrapper.ConnectionOptionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public class MqttService implements InitializingBean {
    @Resource
    private MqttConsumer mqttConsumer;

    /**
     * MQTT客户端
     */
    private MqttClient mqttClient;

    @Override
    public void afterPropertiesSet() throws MqttException, NoSuchAlgorithmException, InvalidKeyException {
        if (mqttClient == null) {
            synchronized (MqttService.class) {
                if (mqttClient == null) {
                    doInitMqttClient();
                }
            }
        }
    }

    public void sendConsoleMsg(String payload) {
        ExeService4Publish.getExecutorService().submit(() -> {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(MqttConfig.QOS_LEVEL);
            try {
                mqttClient.publish(MqttUtils.getConsoleTopic(), message);
            } catch (MqttException e) {
                log.error("send msg by topic error: " + e.getMessage(), e);
            }
        });
    }

    public void sendBroadcastMsgByTenant(String tenantCode, String payload) {
        ExeService4Publish.getExecutorService().submit(() -> {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(MqttConfig.QOS_LEVEL);
            try {
                mqttClient.publish(MqttUtils.getBroadcastTopicByTenant(tenantCode), message);
            } catch (MqttException e) {
                log.error("send msg by p2p error: " + e.getMessage(), e);
            }
        });
    }

    public void sendP2PMsgByTenant(String tenantCode, String machineCode, String payload) {
        ExeService4Publish.getExecutorService().submit(() -> {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(MqttConfig.QOS_LEVEL);
            try {
                mqttClient.publish(MqttUtils.getP2PTopicByTenant(tenantCode, machineCode), message);
            } catch (MqttException e) {
                log.error("send msg by p2p error: " + e.getMessage(), e);
            }
        });
    }

    @PreDestroy
    public void onDestroy() {
        try {
            log.error("$$$$$ mqtt onDestroy entering");
            mqttClient.disconnect();
        } catch (MqttException e) {
            log.error("close mqtt client error: " + e.getMessage(), e);
        }
    }

    private void doInitMqttClient() throws MqttException, NoSuchAlgorithmException, InvalidKeyException {
        MemoryPersistence memoryPersistence = new MemoryPersistence();
        mqttClient = new MqttClient("tcp://" + MqttConfig.ENDPOINT + ":1883",
                MqttConfig.CLIENT_ID, memoryPersistence);
        // 客户端设置好发送超时时间，防止无限阻塞
        mqttClient.setTimeToWait(MqttConfig.TIME_TO_WAIT);

        // 设置订阅
        mqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                // 客户端连接成功后就需要尽快订阅需要的 topic
                try {
                    String[] topicFilters = getTopicFilters();
                    for (String topicFilter : topicFilters) {
                        log.info("$$$$$ topicFilter = " + topicFilter);
                    }
                    int[] qos = getQos();
                    mqttClient.subscribe(topicFilters, qos);
                } catch (MqttException e) {
                    log.error("mqtt subscribe error: " + e.getMessage(), e);
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                log.error("mqtt connection lost: " + throwable.getMessage(), throwable);
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) {
                mqttConsumer.consume(topic, new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                log.info("mqtt send success: " + iMqttDeliveryToken.getTopics()[0]);
            }
        });
        ConnectionOptionWrapper connectionOptionWrapper = new ConnectionOptionWrapper(
                MqttConfig.INSTANCE_ID, MqttConfig.ACCESS_KEY, MqttConfig.ACCESS_KEY_SECRET, MqttConfig.CLIENT_ID);
        mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());
    }

    private String[] getTopicFilters() {
        return new String[]{
                MqttConsts.CONSOLE_PARENT_TOPIC + MqttConsts.TOPIC_SEPERATOR + "console",
                MqttConsts.CONSOLE_PARENT_TOPIC + MqttConsts.TOPIC_SEPERATOR + "recall"
        };
    }

    private int[] getQos() {
        return new int[]{
                MqttConfig.QOS_LEVEL,
                MqttConfig.QOS_LEVEL
        };
    }
}
