package com.langtuo.teamachine.mqtt;

import com.langtuo.teamachine.mqtt.concurrent.ExecutorService4Consume;
import com.langtuo.teamachine.mqtt.config.MQTTConfig;
import com.langtuo.teamachine.mqtt.concurrent.ExecutorService4Publish;
import com.langtuo.teamachine.mqtt.worker.ConsumeWorker;
import com.langtuo.teamachine.mqtt.wrapper.ConnectionOptionWrapper;
import lombok.extern.slf4j.Slf4j;
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
     *
     */
    private MqttClient mqttClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        initMqttClient();
        log.info("$$$$$ afterPropertiesSet exiting");
    }

    public void testSend() throws MqttException {
        for (int i = 0; i < 10; i++) {
            this.sendMsgByTopic("testMq4Iot", "guangxia send topic message");
            this.sendMsgByP2P(MQTTConfig.CLIENT_ID, "guangxia send p2p message");
        }
    }

    public void sendMsgByTopic(String topic, String payload) {
        ExecutorService4Publish.getExecutorService().submit(() -> {
            String sendTopic = MQTTConfig.PARENT_TOPIC_PREFIX + topic;
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(MQTTConfig.QOS_LEVEL);
            try {
                mqttClient.publish(sendTopic, message);
            } catch (MqttException e) {
                log.error("send msg by topic error: " + e.getMessage(), e);
            }
        });
    }

    public void sendMsgByP2P(String clientId, String payload) {
        ExecutorService4Publish.getExecutorService().submit(() -> {
            String p2pSendTopic = MQTTConfig.PARENT_P2P_TOPIC_PREFIX + clientId;
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(MQTTConfig.QOS_LEVEL);
            try {
                mqttClient.publish(p2pSendTopic, message);
            } catch (MqttException e) {
                log.error("send msg by p2p error: " + e.getMessage(), e);
            }
        });
    }

    private MqttClient initMqttClient() throws MqttException, NoSuchAlgorithmException, InvalidKeyException {
        if (mqttClient == null) {
            synchronized (MQTTService.class) {
                doInitMqttClient();
            }
        }
        return mqttClient;
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
                    final String topicFilter[] = {MQTTConfig.PARENT_TOPIC_PREFIX + "testMq4Iot"};
                    final int[] qos = {MQTTConfig.QOS_LEVEL};
                    mqttClient.subscribe(topicFilter, qos);
                    log.info("$$$$$ subscribe success");
                } catch (MqttException e) {
                    log.error("mqtt subscribe error: " + e.getMessage(), e);
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                log.error("mqtt connection lost: " + throwable.getMessage(), throwable);
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                ConsumeWorker worker = new ConsumeWorker(s, new String(mqttMessage.getPayload()));
                ExecutorService4Consume.getExecutorService().submit(worker);
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
}
