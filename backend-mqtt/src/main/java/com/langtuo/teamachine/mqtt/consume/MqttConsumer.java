package com.langtuo.teamachine.mqtt.consume;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.mqtt.concurrent.ExeService4Consume;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.consume.worker.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqttConsumer {
    public void consume(String topic, String payload) {
        if (StringUtils.isBlank(topic) || StringUtils.isBlank(payload)) {
            log.error("receive msg error, topic=" + topic + ", payload=" + payload);
            return;
        }
        JSONObject jsonPayload = JSONObject.parseObject(payload);
        log.info("received msg, topic=" + topic + ", payload=" + payload);

        String title = jsonPayload.getString(MqttConsts.RECEIVE_KEY_TITLE);
        if (MqttConsts.MSG_TITLE_PREPARE_ACCURACY.equals(title)) {
            ExeService4Consume.getExeService().submit(new AccuracyDispatchWorker(jsonPayload));
        } else if (MqttConsts.MSG_TITLE_PREPARE_MENU.equals(title)) {
            ExeService4Consume.getExeService().submit(new MenuDispatchWorker(jsonPayload));
        } else if (MqttConsts.MSG_TITLE_PREPARE_OPEN_RULE.equals(title)) {
            ExeService4Consume.getExeService().submit(new OpenRuleDispatchWorker(jsonPayload));
        } else if (MqttConsts.MSG_TITLE_PREPARE_CLEAN_RULE.equals(title)) {
            ExeService4Consume.getExeService().submit(new CleanRuleDispatchWorker(jsonPayload));
        } else if (MqttConsts.MSG_TITLE_PREPARE_WARNING_RULE.equals(title)) {
            ExeService4Consume.getExeService().submit(new WarningRuleDispatchWorker(jsonPayload));
        } else {
            log.info("match worker error, topic=" + topic);
        }
    }
}
