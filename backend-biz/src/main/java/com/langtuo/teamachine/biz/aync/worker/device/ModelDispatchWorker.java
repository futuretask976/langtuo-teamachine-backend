package com.langtuo.teamachine.biz.aync.worker.device;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.device.ModelDTO;
import com.langtuo.teamachine.api.model.user.TenantDTO;
import com.langtuo.teamachine.api.service.device.ModelMgtService;
import com.langtuo.teamachine.api.service.user.TenantMgtService;
import com.langtuo.teamachine.biz.util.SpringServiceUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
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
        this.modelCode = jsonPayload.getString(CommonConsts.JSON_KEY_MODEL_CODE);
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
        jsonMsg.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_DISPATCH_MODEL);
        jsonMsg.put(CommonConsts.JSON_KEY_MODEL, jsonDispatchCont);

        TenantMgtService tenantMgtService = SpringServiceUtils.getTenantMgtService();
        List<String> tenantCodeList = getListModel(tenantMgtService.list()).stream()
                .map(TenantDTO::getTenantCode)
                .collect(Collectors.toList());

        MqttProducer mqttProducer = SpringServiceUtils.getMqttProducer();
        tenantCodeList.forEach(tenantCode -> {
            mqttProducer.sendBroadcastMsgByTenant(tenantCode, jsonMsg.toJSONString());
        });
    }

    private JSONObject getDispatchCont() {
        ModelMgtService modelMgtService = SpringServiceUtils.getModelMgtService();
        ModelDTO dto = getModel(modelMgtService.get(modelCode));
        if (dto == null) {
            log.info("model is empty, stop worker");
            return null;
        }

        JSONObject jsonDispatchCont = (JSONObject) JSONObject.toJSON(dto);
        return jsonDispatchCont;
    }
}
