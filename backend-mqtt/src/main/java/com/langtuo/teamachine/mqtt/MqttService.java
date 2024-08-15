package com.langtuo.teamachine.mqtt;

import com.langtuo.teamachine.api.model.user.TenantDTO;
import com.langtuo.teamachine.api.service.user.TenantMgtService;
import com.langtuo.teamachine.mqtt.config.MqttConfig;
import com.langtuo.teamachine.mqtt.concurrent.ExeService4Publish;
import com.langtuo.teamachine.mqtt.util.MqttUtils;
import com.langtuo.teamachine.mqtt.consume.MqttMsgConsumer;
import com.langtuo.teamachine.mqtt.wrapper.ConnectionOptionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.LangTuoResult.getListModel;

@Component
@Slf4j
public class MqttService implements InitializingBean {
    @Resource
    private MqttMsgConsumer mqttMsgConsumer;

    @Resource
    private TenantMgtService tenantMgtService;

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
                    String[] topicFilterArray = getTopicFilterArray();
                    int[] qosArray = getQosArray(topicFilterArray);
                    mqttClient.subscribe(topicFilterArray, qosArray);
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

    private String[] getTopicFilterArray() {
        List<String> topicList = Lists.newArrayList();
        topicList.add(MqttConfig.CONSOLE_PARENT_TOPIC + MqttConfig.TOPIC_SEPERATOR + "console");
        topicList.add(MqttConfig.CONSOLE_PARENT_TOPIC + MqttConfig.TOPIC_SEPERATOR + "recall");

        List<String> tenantCodeList = getListModel(tenantMgtService.list()).stream()
                .map(TenantDTO::getTenantCode)
                .collect(Collectors.toList());
        tenantCodeList.forEach(tenantCode -> {
            topicList.add(tenantCode + MqttConfig.TENANT_PARENT_TOPIC_POSTFIX
                    + MqttConfig.TOPIC_SEPERATOR + "dispatch");
        });
        return (String[]) topicList.toArray();
    }

    private int[] getQosArray(String[] topicArray) {
        int[] qosArr = new int[topicArray.length];
        for (int i = 0; i < qosArr.length; i++) {
            qosArr[i] = MqttConfig.QOS_LEVEL;
        }
        return qosArr;
    }
}
