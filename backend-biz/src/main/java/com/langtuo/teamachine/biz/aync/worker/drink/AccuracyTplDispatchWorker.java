package com.langtuo.teamachine.biz.aync.worker.drink;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.drink.AccuracyTplDTO;
import com.langtuo.teamachine.api.service.drink.AccuracyTplMgtService;
import com.langtuo.teamachine.biz.util.SpringServiceUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
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
        this.tenantCode = jsonPayload.getString(CommonConsts.JSON_KEY_TENANT_CODE);
        this.templateCode = jsonPayload.getString(CommonConsts.JSON_KEY_TEMPLATE_CODE);
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
        jsonMsg.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_DISPATCH_ACCURACY);
        jsonMsg.put(CommonConsts.JSON_KEY_ACCURACY_TPL, jsonDispatchCont);
        log.info("$$$$$ AccuracyDispatchWorker sendMsg: " + jsonMsg.toJSONString());

        MqttProducer mqttProducer = SpringServiceUtils.getMqttProducer();
        mqttProducer.sendBroadcastMsgByTenant(tenantCode, jsonMsg.toJSONString());
    }

    private JSONObject getDispatchCont() {
        AccuracyTplMgtService accuracyTplMgtService = SpringServiceUtils.getToppingAccuracyTplMgtService();
        AccuracyTplDTO dto = getModel(accuracyTplMgtService.getByCode(tenantCode, templateCode));
        if (dto == null) {
            log.info("open rule list is empty, stop worker");
            return null;
        }

        JSONObject jsonDispatchCont = (JSONObject) JSONObject.toJSON(dto);
        return jsonDispatchCont;
    }
}