package com.langtuo.teamachine.mqtt.consume.worker;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.device.ModelDTO;
import com.langtuo.teamachine.api.model.user.TenantDTO;
import com.langtuo.teamachine.api.service.device.ModelMgtService;
import com.langtuo.teamachine.api.service.user.TenantMgtService;
import com.langtuo.teamachine.mqtt.MqttService;
import com.langtuo.teamachine.mqtt.config.MqttConfig;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.LangTuoResult.getListModel;

@Slf4j
public class ModelDispatchWorker implements Runnable {
    public ModelDispatchWorker(JSONObject jsonPayload) {
    }

    @Override
    public void run() {
        JSONArray jsonArray = getDispatchCont();
        if (jsonArray == null) {
            log.info("dispatch content error, stop worker");
            return;
        }

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(MqttConsts.SEND_KEY_TITLE, MqttConsts.SEND_TITLE_DISPATCH_MODEL);
        jsonMsg.put(MqttConsts.SEND_KEY_MODEL_LIST, jsonArray);

        TenantMgtService tenantMgtService = getTenantMgtService();
        List<String> tenantCodeList = getListModel(tenantMgtService.list()).stream()
                .map(TenantDTO::getTenantCode)
                .collect(Collectors.toList());

        MqttService mqttService = getMQTTService();
        tenantCodeList.forEach(tenantCode -> {
            mqttService.sendDispatchMsgByTenant(tenantCode, jsonArray.toJSONString());
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

    private ModelMgtService getCloseRuleMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ModelMgtService modelMgtService = appContext.getBean(ModelMgtService.class);
        return modelMgtService;
    }

    private JSONArray getDispatchCont() {
        ModelMgtService modelMgtService = getCloseRuleMgtService();
        List<ModelDTO> list = getListModel(modelMgtService.list());
        if (CollectionUtils.isEmpty(list)) {
            log.info("model list is empty, stop worker");
            return null;
        }

        JSONArray jsonArray = (JSONArray) JSON.toJSON(list);
        return jsonArray;
    }
}
