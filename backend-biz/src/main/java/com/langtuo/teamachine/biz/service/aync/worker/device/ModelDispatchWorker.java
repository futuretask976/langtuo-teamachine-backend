package com.langtuo.teamachine.biz.service.aync.worker.device;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.device.ModelDTO;
import com.langtuo.teamachine.api.model.user.TenantDTO;
import com.langtuo.teamachine.api.service.device.ModelMgtService;
import com.langtuo.teamachine.api.service.user.TenantMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
import com.langtuo.teamachine.mqtt.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.TeaMachineResult.getListModel;
import static com.langtuo.teamachine.api.result.TeaMachineResult.getModel;

@Slf4j
public class ModelDispatchWorker implements Runnable {
    /**
     * 型号编码
     */
    private String modelCode;

    public ModelDispatchWorker(JSONObject jsonPayload) {
        this.modelCode = jsonPayload.getString(BizConsts.JSON_KEY_MODEL_CODE);
        if (StringUtils.isBlank(modelCode)) {
            throw new IllegalArgumentException("modelCode is blank");
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
        jsonMsg.put(BizConsts.JSON_KEY_BIZ_CODE, BizConsts.BIZ_CODE_DISPATCH_MODEL);
        jsonMsg.put(BizConsts.JSON_KEY_MODEL, jsonDispatchCont);
        log.info("$$$$$ ModelDispatchWorker jsonMsg: " + jsonMsg);

        TenantMgtService tenantMgtService = SpringUtils.getTenantMgtService();
        List<String> tenantCodeList = getListModel(tenantMgtService.list()).stream()
                .map(TenantDTO::getTenantCode)
                .collect(Collectors.toList());

        MqttProducer mqttProducer = SpringUtils.getMqttProducer();
        tenantCodeList.forEach(tenantCode -> {
            mqttProducer.sendBroadcastMsgByTenant(tenantCode, jsonMsg.toJSONString());
        });
    }

    private JSONObject getDispatchCont() {
        ModelMgtService modelMgtService = SpringUtils.getModelMgtService();
        ModelDTO dto = getModel(modelMgtService.get(modelCode));
        if (dto == null) {
            log.info("model is empty, stop worker");
            return null;
        }

        JSONObject jsonDispatchCont = (JSONObject) JSONObject.toJSON(dto);
        return jsonDispatchCont;
    }
}
