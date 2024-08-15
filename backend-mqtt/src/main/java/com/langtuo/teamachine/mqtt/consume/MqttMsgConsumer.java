package com.langtuo.teamachine.mqtt.consume;

import com.langtuo.teamachine.mqtt.concurrent.ExeService4Consume;
import com.langtuo.teamachine.mqtt.config.MqttConfig;
import com.langtuo.teamachine.mqtt.consume.worker.*;
import com.langtuo.teamachine.mqtt.util.MQTTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqttMsgConsumer {
    public void consume(String topic, String payload) {
        if (StringUtils.isBlank(topic) || StringUtils.isBlank(payload)) {
            log.info("receive msg error, topic=" + topic + ", payload=" + payload);
            return;
        }
        log.info("received msg, topic=" + topic + ", payload=" + payload);

        if (MQTTUtils.getConsoleTopic(MqttConfig.CONSOLE_TOPIC_PREPARE_DISPATCH_ACCURACY).equals(topic)) {
            ExeService4Consume.getExeService().submit(new AccuracyDispatchWorker(payload));
        } else if (MQTTUtils.getConsoleTopic(MqttConfig.CONSOLE_TOPIC_PREPARE_DISPATCH_MENU).equals(topic)) {
            ExeService4Consume.getExeService().submit(new MenuDispatchWorker(payload));
        } else if (MQTTUtils.getConsoleTopic(MqttConfig.CONSOLE_TOPIC_PREPARE_DISPATCH_OPEN_RULE).equals(topic)) {
            ExeService4Consume.getExeService().submit(new OpenRuleDispatchWorker(payload));
        } else if (MQTTUtils.getConsoleTopic(MqttConfig.CONSOLE_TOPIC_PREPARE_DISPATCH_CLEAN_RULE).equals(topic)) {
            ExeService4Consume.getExeService().submit(new CleanRuleDispatchWorker(payload));
        } else if (MQTTUtils.getConsoleTopic(MqttConfig.CONSOLE_TOPIC_PREPARE_DISPATCH_WARNING_RULE).equals(topic)) {
            ExeService4Consume.getExeService().submit(new WarningRuleDispatchWorker(payload));
        } else {
            log.info("match worker error, topic=" + topic);
        }
    }
}
