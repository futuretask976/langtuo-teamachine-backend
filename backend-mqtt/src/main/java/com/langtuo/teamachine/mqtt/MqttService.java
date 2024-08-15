package com.langtuo.teamachine.mqtt;

import com.langtuo.teamachine.mqtt.config.MqttConfig;
import com.langtuo.teamachine.mqtt.concurrent.ExeService4Publish;
import com.langtuo.teamachine.mqtt.util.MqttUtils;
import com.langtuo.teamachine.mqtt.consume.MqttMsgConsumer;
import com.langtuo.teamachine.mqtt.wrapper.ConnectionOptionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public class MqttService implements InitializingBean {
    @Resource
    private MqttMsgConsumer mqttMsgConsumer;

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

    public void sendConsoleMsgByTopic(String topic, String payload) {
        ExeService4Publish.getExecutorService().submit(() -> {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(MqttConfig.QOS_LEVEL);
            try {
                mqttClient.publish(MqttUtils.getConsoleTopic(topic), message);
            } catch (MqttException e) {
                log.error("send msg by topic error: " + e.getMessage(), e);
            }
        });
    }

    public void sendMachineMsg(String tenantCode, String topic, String payload) {
        ExeService4Publish.getExecutorService().submit(() -> {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(MqttConfig.QOS_LEVEL);
            try {
                mqttClient.publish(MqttUtils.getMachineTopic(tenantCode, topic), message);
            } catch (MqttException e) {
                log.error("send msg by p2p error: " + e.getMessage(), e);
            }
        });
    }

    public void sendMachineMsgByP2P(String tenantCode, String machineCode, String payload) {
        ExeService4Publish.getExecutorService().submit(() -> {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(MqttConfig.QOS_LEVEL);
            try {
                mqttClient.publish(MqttUtils.getMachineP2PTopic(tenantCode, machineCode), message);
            } catch (MqttException e) {
                log.error("send msg by p2p error: " + e.getMessage(), e);
            }
        });
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
                log.info("$$$$$ mqtt connnect success: " + serverURI);
                // 客户端连接成功后就需要尽快订阅需要的 topic
                try {
                    mqttClient.subscribe(MqttConfig.TOPIC_FILTERS, MqttConfig.QOS);
                } catch (MqttException e) {
                    log.error("mqtt subscribe error: " + e.getMessage(), e);
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                // log.error("mqtt connection lost: " + throwable.getMessage(), throwable);
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) {
                mqttMsgConsumer.consume(topic, new String(mqttMessage.getPayload()));
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
}
