package com.langtuo.teamachine.mqtt.consume.worker.device;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.device.ModelDTO;
import com.langtuo.teamachine.api.model.user.TenantDTO;
import com.langtuo.teamachine.api.service.device.ModelMgtService;
import com.langtuo.teamachine.api.service.user.TenantMgtService;
import com.langtuo.teamachine.mqtt.MqttService;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

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
        this.modelCode = jsonPayload.getString(MqttConsts.RECEIVE_KEY_MODEL_CODE);
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
        jsonMsg.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_DISPATCH_MODEL);
        jsonMsg.put(MqttConsts.SEND_KEY_MODEL, jsonDispatchCont);
        log.info("$$$$$ ModelDispatchWorker jsonMsg: " + jsonMsg);

        TenantMgtService tenantMgtService = getTenantMgtService();
        List<String> tenantCodeList = getListModel(tenantMgtService.list()).stream()
                .map(TenantDTO::getTenantCode)
                .collect(Collectors.toList());

        MqttService mqttService = getMQTTService();
        tenantCodeList.forEach(tenantCode -> {
            mqttService.sendBroadcastMsgByTenant(tenantCode, jsonMsg.toJSONString());
        });
    }

    private MqttService getMQTTService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MqttService mqttService = appContext.getBean(MqttService.class);
        return mqttService;
    }

    private TenantMgtService getTenantMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        TenantMgtService tenantMgtService = appContext.getBean(TenantMgtService.class);
        return tenantMgtService;
    }

    private ModelMgtService getModelMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ModelMgtService modelMgtService = appContext.getBean(ModelMgtService.class);
        return modelMgtService;
    }

    private JSONObject getDispatchCont() {
        ModelMgtService modelMgtService = getModelMgtService();
        ModelDTO dto = getModel(modelMgtService.get(modelCode));
        if (dto == null) {
            log.info("model is empty, stop worker");
            return null;
        }

        JSONObject jsonDispatchCont = (JSONObject) JSONObject.toJSON(dto);
        return jsonDispatchCont;
    }
}
