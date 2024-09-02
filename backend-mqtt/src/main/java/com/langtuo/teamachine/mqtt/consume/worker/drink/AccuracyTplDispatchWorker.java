package com.langtuo.teamachine.mqtt.consume.worker.drink;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.drink.AccuracyTplDTO;
import com.langtuo.teamachine.api.service.drink.AccuracyTplMgtService;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
import com.langtuo.teamachine.mqtt.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import static com.langtuo.teamachine.api.result.TeaMachineResult.getModel;

@Slf4j
public class AccuracyTplDispatchWorker implements Runnable {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 模板编码
     */
    private String templateCode;

    public AccuracyTplDispatchWorker(JSONObject jsonPayload) {
        this.tenantCode = jsonPayload.getString(MqttConsts.RECEIVE_KEY_TENANT_CODE);
        this.templateCode = jsonPayload.getString(MqttConsts.RECEIVE_KEY_TEMPLATE_CODE);
        if (StringUtils.isBlank(tenantCode)) {
            throw new IllegalArgumentException("tenantCode or menuCode is blank");
        }
    }

    @Override
    public void run() {
        JSONObject jsonDispatchCont = getDispatchCont();
        if (jsonDispatchCont == null) {
            log.info("dispatch content error, stop worker");
            return;
        }

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_DISPATCH_ACCURACY);
        jsonMsg.put(MqttConsts.SEND_KEY_ACCURACY_TPL, jsonDispatchCont);
        log.info("$$$$$ AccuracyDispatchWorker sendMsg: " + jsonMsg.toJSONString());

        MqttProducer mqttProducer = SpringUtils.getMqttProducer();
        mqttProducer.sendBroadcastMsgByTenant(tenantCode, jsonMsg.toJSONString());
    }

    private JSONObject getDispatchCont() {
        AccuracyTplMgtService accuracyTplMgtService = SpringUtils.getToppingAccuracyTplMgtService();
        AccuracyTplDTO dto = getModel(accuracyTplMgtService.getByCode(tenantCode, templateCode));
        if (dto == null) {
            log.info("open rule list is empty, stop worker");
            return null;
        }

        JSONObject jsonDispatchCont = (JSONObject) JSONObject.toJSON(dto);
        return jsonDispatchCont;
    }
}
