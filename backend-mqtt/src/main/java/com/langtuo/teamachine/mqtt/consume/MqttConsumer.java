package com.langtuo.teamachine.mqtt.consume;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.mqtt.concurrent.ExeService4Consume;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.consume.worker.device.MachineDispatchWorker;
import com.langtuo.teamachine.mqtt.consume.worker.device.ModelDispatchWorker;
import com.langtuo.teamachine.mqtt.consume.worker.drink.AccuracyTplDispatchWorker;
import com.langtuo.teamachine.mqtt.consume.worker.menu.MenuDispatchWorker;
import com.langtuo.teamachine.mqtt.consume.worker.rule.CleanRuleDispatchWorker;
import com.langtuo.teamachine.mqtt.consume.worker.rule.DrainRuleDispatchWorker;
import com.langtuo.teamachine.mqtt.consume.worker.rule.WarningRuleDispatchWorker;
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

        String bizCode = jsonPayload.getString(MqttConsts.RECEIVE_KEY_BIZ_CODE);
        if (MqttConsts.BIZ_CODE_PREPARE_MODEL.equals(bizCode)) {
            ExeService4Consume.getExeService().submit(new ModelDispatchWorker(jsonPayload));
        } else if (MqttConsts.BIZ_CODE_PREPARE_MACHINE.equals(bizCode)) {
            ExeService4Consume.getExeService().submit(new MachineDispatchWorker(jsonPayload));
        } else if (MqttConsts.BIZ_CODE_PREPARE_ACCURACY_TPL.equals(bizCode)) {
            ExeService4Consume.getExeService().submit(new AccuracyTplDispatchWorker(jsonPayload));
        } else if (MqttConsts.BIZ_CODE_PREPARE_MENU.equals(bizCode)) {
            ExeService4Consume.getExeService().submit(new MenuDispatchWorker(jsonPayload));
        } else if (MqttConsts.BIZ_CODE_PREPARE_DRAIN_RULE.equals(bizCode)) {
            ExeService4Consume.getExeService().submit(new DrainRuleDispatchWorker(jsonPayload));
        } else if (MqttConsts.BIZ_CODE_PREPARE_CLEAN_RULE.equals(bizCode)) {
            ExeService4Consume.getExeService().submit(new CleanRuleDispatchWorker(jsonPayload));
        } else if (MqttConsts.BIZ_CODE_PREPARE_WARNING_RULE.equals(bizCode)) {
            ExeService4Consume.getExeService().submit(new WarningRuleDispatchWorker(jsonPayload));
        } else {
            log.info("match worker error, topic=" + topic);
        }
    }
}
