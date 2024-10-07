package com.langtuo.teamachine.mqtt.produce;

import com.alibaba.mqtt.server.ServerProducer;
import com.alibaba.mqtt.server.callback.SendCallback;
import com.alibaba.mqtt.server.config.ChannelConfig;
import com.alibaba.mqtt.server.config.ProducerConfig;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.util.MqttUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ScheduledThreadPoolExecutor;
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
        System.out.println("mqttProducer|onDestroy|entering");
        if (serverProducer == null) {
            return;
        }

        try {
            serverProducer.stop();

            Field schedulerField = ServerProducer.class.getDeclaredField("scheduler");
            schedulerField.setAccessible(true);
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) schedulerField.get(
                    serverProducer);
            scheduledThreadPoolExecutor.shutdownNow();

            serverProducer = null;
        } catch (Exception e) {
            System.out.println("mqttProducer|stopServerProducer|fatal|" + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("mqttProducer|onDestroy|exiting");
    }

    public void init() throws IOException, TimeoutException {
        ChannelConfig channelConfig = MqttUtils.getChannelConfig();
        serverProducer = new ServerProducer(channelConfig, new ProducerConfig());
        serverProducer.start();
    }

    public void sendP2PMsgByTenant(String tenantCode, String machineCode, String payload) {
        String topic = tenantCode + MqttConsts.TENANT_PARENT_P2P_TOPIC_POSTFIX + machineCode;
        try {
            serverProducer.sendMessage(topic, payload.getBytes(StandardCharsets.UTF_8), new SendCallback() {
                @Override
                public void onSuccess(String s) {
                    log.info("$$$$$ mqttProducer|sendP2PMsg|onSuccess|" + topic + "|" + payload);
                }

                @Override
                public void onFail() {
                    log.info("$$$$$ mqttProducer|sendP2PMsg|onFail|" + topic + "|" + payload);
                }
            });
        } catch (Throwable e) {
            log.error("mqttProducer|sendP2PMsgByTenant|fatal|" + e.getMessage(), e);
        }
    }

    public void sendBroadcastMsgByTenant(String tenantCode, String payload) {
        String topic = tenantCode + MqttConsts.TENANT_PARENT_TOPIC_POSTFIX + MqttConsts.TOPIC_SEPERATOR + "broadcast";
        try {
            serverProducer.sendMessage(topic, payload.getBytes(StandardCharsets.UTF_8), new SendCallback() {
                @Override
                public void onSuccess(String s) {
                    log.info("$$$$$ mqttProducer|sendBroadcastMsg|onSuccess|" + topic + "|" + payload);
                }

                @Override
                public void onFail() {
                    log.info("$$$$$ mqttProducer|sendBroadcastMsg|onFail|" + topic + "|" + payload);
                }
            });
        } catch (Throwable e) {
            log.error("mqttProducer|sendBroadcastMsgByTenant|fatal|" + e.getMessage(), e);
        }
    }
}
