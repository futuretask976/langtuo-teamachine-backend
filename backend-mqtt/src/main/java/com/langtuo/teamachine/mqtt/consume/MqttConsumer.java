package com.langtuo.teamachine.mqtt.consume;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.mqtt.server.ServerConsumer;
import com.alibaba.mqtt.server.callback.MessageListener;
import com.alibaba.mqtt.server.callback.StatusListener;
import com.alibaba.mqtt.server.config.ChannelConfig;
import com.alibaba.mqtt.server.config.ConsumerConfig;
import com.alibaba.mqtt.server.model.MessageProperties;
import com.alibaba.mqtt.server.model.StatusNotice;
import com.google.common.collect.Maps;
import com.langtuo.teamachine.internal.constant.AliyunConsts;
import com.langtuo.teamachine.mqtt.threadpool.ConsumeExeService;
import com.langtuo.teamachine.mqtt.consume.worker.record.*;
import com.langtuo.teamachine.mqtt.util.MqttUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

/**
 * @author Jiaqing
 */
@Component
@Slf4j
public class MqttConsumer implements InitializingBean {
    /**
     * MQTT 消费者实例
     */
    private ServerConsumer serverConsumer;

    /**
     * 存放需要异步执行的 woker
     */
    private Map<String, Function<JSONObject, Runnable>> workerMap;

    @Override
    public void afterPropertiesSet() throws IOException, TimeoutException {
        if (serverConsumer == null) {
            synchronized (MqttConsumer.class) {
                if (serverConsumer == null) {
                    initServerConsumer();
                }
            }
        }

        if (workerMap == null) {
            synchronized (MqttConsumer.class) {
                if (workerMap == null) {
                    initWorkerMap();
                }
            }
        }
    }

    @PreDestroy
    public void onDestroy() {
        System.out.println("|mqttConsumer|onDestroy|entering|");
        if (serverConsumer == null) {
            return;
        }

        // 先关闭 MQTT 消息消费
        try {
            serverConsumer.stop();
            serverConsumer = null;
            Thread.sleep(1000 * 5);
        } catch (Exception e) {
            System.out.println("|mqttConsumer|stopServerConsumer|fatal|" + e.getMessage() + "|");
            e.printStackTrace();
        }
        System.out.println("|mqttConsumer|onDestroy|exiting|");
    }

    public void initServerConsumer() throws IOException, TimeoutException {
        ChannelConfig channelConfig = MqttUtils.getChannelConfig();
        serverConsumer = new ServerConsumer(channelConfig, new ConsumerConfig());
        serverConsumer.start();
        serverConsumer.subscribeTopic(AliyunConsts.MQTT_CONSOLE_PARENT_TOPIC, new MessageListener() {
            @Override
            public void process(String msgId, MessageProperties messageProperties, byte[] payload) {
                String strPayload = new String(payload);
                dispatch(strPayload);
            }
        });
        serverConsumer.subscribeStatus(AliyunConsts.MQTT_GROUP_ID, new StatusListener() {
            @Override
            public void process(StatusNotice statusNotice) {
                notice(JSONObject.toJSONString(statusNotice));
            }
        });
    }

    private void initWorkerMap() {
        workerMap = Maps.newHashMap();
        // record 相关
        workerMap.put(AliyunConsts.MQTT_BIZ_CODE_INVALID_ACT_RECORD, jsonPayload -> new InvalidActRecordWorker(jsonPayload));
        workerMap.put(AliyunConsts.MQTT_BIZ_CODE_SUPPLY_ACT_RECORD, jsonPayload -> new SupplyActRecordWorker(jsonPayload));
        workerMap.put(AliyunConsts.MQTT_BIZ_CODE_DRAIN_ACT_RECORD, jsonPayload -> new DrainActRecordWorker(jsonPayload));
        workerMap.put(AliyunConsts.MQTT_BIZ_CODE_CLEAN_ACT_RECORD, jsonPayload -> new CleanActRecordWorker(jsonPayload));
        workerMap.put(AliyunConsts.MQTT_BIZ_CODE_ORDER_ACT_RECORD, jsonPayload -> new OrderActRecordWorker(jsonPayload));
    }

    public void notice(String payload) {
        if (StringUtils.isBlank(payload)) {
            return;
        }
        log.info("|mqttConsumer|notice|entering|" + payload + "|");
    }

    public void dispatch(String payload) {
        if (StringUtils.isBlank(payload)) {
            log.error("|mqttConsumer|dispatch|payloadEmpty|" + payload + "|");
            return;
        }
        log.info("|mqttConsumer|dispatch|entering|" + payload + "|");

        try {
            JSONObject jsonPayload = JSONObject.parseObject(payload);
            String bizCode = jsonPayload.getString(AliyunConsts.MQTT_RECEIVE_KEY_BIZ_CODE);
            if (StringUtils.isBlank(bizCode)) {
                log.error("|mqttConsumer|dispatch|bizCodeEmpty|" + bizCode + "|");
                return;
            }
            Function<JSONObject, Runnable> function = workerMap.get(bizCode);
            if (function == null) {
                log.error("|mqttConsumer|dispatch|noMatch|" + bizCode + "|");
                return;
            }
            ConsumeExeService.getExeService().submit(function.apply(jsonPayload));
        } catch (Exception e) {
            log.error("|mqttConsumer|dispatch|fatal|" + e.getMessage() + "|" + payload + "|");
        }
    }
}
