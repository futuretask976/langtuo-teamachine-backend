package com.langtuo.teamachine.mqtt;

import com.langtuo.teamachine.mqtt.concurrent.ExeService4Consume;
import com.langtuo.teamachine.mqtt.config.MQTTConfig;
import com.langtuo.teamachine.mqtt.concurrent.ExeService4Publish;
import com.langtuo.teamachine.mqtt.worker.PrepareMenuDispatchWorker;
import com.langtuo.teamachine.mqtt.wrapper.ConnectionOptionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public class MQTTService implements InitializingBean {
    /**
     * MQTT客户端
     */
    private MqttClient mqttClient;

    @Override
    public void afterPropertiesSet() throws MqttException, NoSuchAlgorithmException, InvalidKeyException {
        if (mqttClient == null) {
            synchronized (MQTTService.class) {
                if (mqttClient == null) {
                    doInitMqttClient();
                }
            }
        }
    }

    public void sendMsgByTopic(String topic, String payload) {
        ExeService4Publish.getExecutorService().submit(() -> {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(MQTTConfig.QOS_LEVEL);
            try {
                mqttClient.publish(topic, message);
            } catch (MqttException e) {
                log.error("send msg by topic error: " + e.getMessage(), e);
            }
        });
    }

    public void sendMsgByP2P(String clientId, String payload) {
        ExeService4Publish.getExecutorService().submit(() -> {
            String p2pTopic = MQTTConfig.PARENT_P2P_TOPIC + MQTTConfig.TOPIC_SEPERATOR + clientId;
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(MQTTConfig.QOS_LEVEL);
            try {
                mqttClient.publish(p2pTopic, message);
            } catch (MqttException e) {
                log.error("send msg by p2p error: " + e.getMessage(), e);
            }
        });
    }

    private void doInitMqttClient() throws MqttException, NoSuchAlgorithmException, InvalidKeyException {
        MemoryPersistence memoryPersistence = new MemoryPersistence();
        mqttClient = new MqttClient("tcp://" + MQTTConfig.ENDPOINT + ":1883",
                MQTTConfig.CLIENT_ID, memoryPersistence);
        // 客户端设置好发送超时时间，防止无限阻塞
        mqttClient.setTimeToWait(MQTTConfig.TIME_TO_WAIT);

        // 设置订阅
        mqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                // 客户端连接成功后就需要尽快订阅需要的 topic
                try {
                    mqttClient.subscribe(MQTTConfig.TOPIC_FILTERS, new int[]{MQTTConfig.QOS_LEVEL, MQTTConfig.QOS_LEVEL});
                } catch (MqttException e) {
                    log.error("mqtt subscribe error: " + e.getMessage(), e);
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                log.error("mqtt connection lost: " + throwable.getMessage(), throwable);
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                consume(topic, new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                log.info("mqtt send success: " + iMqttDeliveryToken.getTopics()[0]);
            }
        });
        ConnectionOptionWrapper connectionOptionWrapper = new ConnectionOptionWrapper(
                MQTTConfig.INSTANCE_ID, MQTTConfig.ACCESS_KEY, MQTTConfig.ACCESS_KEY_SECRET, MQTTConfig.CLIENT_ID);
        mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());
    }

    public void consume(String topic, String payload) {
        if (StringUtils.isBlank(topic) || StringUtils.isBlank(payload)) {
            log.info("receive msg error, topic=" + topic + ", payload=" + payload);
            return;
        }

        if (MQTTConfig.TOPIC_PREPARE_DISPATCH_MENU.equals(topic)) {
            ExeService4Consume.getExeService().submit(new PrepareMenuDispatchWorker(payload));
        } else {
            log.info("match worker error, topic=" + topic);
        }
    }
}
