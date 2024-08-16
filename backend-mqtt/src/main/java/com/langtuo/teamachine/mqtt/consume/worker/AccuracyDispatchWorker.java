package com.langtuo.teamachine.mqtt.consume.worker;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.drink.AccuracyTplDTO;
import com.langtuo.teamachine.api.service.drink.AccuracyTplMgtService;
import com.langtuo.teamachine.mqtt.MqttService;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.langtuo.teamachine.api.result.LangTuoResult.getListModel;

@Slf4j
public class AccuracyDispatchWorker implements Runnable {
    /**
     * 租户编码
     */
    private String tenantCode;

    public AccuracyDispatchWorker(JSONObject jsonPayload) {
        this.tenantCode = jsonPayload.getString(MqttConsts.RECEIVE_KEY_TENANT_CODE);
        if (StringUtils.isBlank(tenantCode)) {
            throw new IllegalArgumentException("tenantCode or menuCode is blank");
        }
    }

    @Override
    public void run() {
        JSONArray jsonArray = getDispatchCont();
        if (jsonArray == null) {
            log.info("dispatch content error, stop worker");
            return;
        }

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(MqttConsts.SEND_KEY_TITLE, MqttConsts.MSG_TITLE_DISPATCH_ACCURACY);
        jsonMsg.put(MqttConsts.SEND_KEY_ACCURACY_TPL_LIST, jsonArray);
        log.info("$$$$$ AccuracyDispatchWorker sendMsg: " + jsonMsg.toJSONString());

        MqttService mqttService = getMQTTService();
        mqttService.sendDispatchMsgByTenant(tenantCode, jsonMsg.toJSONString());
    }

    private MqttService getMQTTService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MqttService mqttService = appContext.getBean(MqttService.class);
        return mqttService;
    }

    private AccuracyTplMgtService getToppingAccuracyTplMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        AccuracyTplMgtService accuracyTplMgtService = appContext.getBean(
                AccuracyTplMgtService.class);
        return accuracyTplMgtService;
    }

    private JSONArray getDispatchCont() {
        AccuracyTplMgtService accuracyTplMgtService = getToppingAccuracyTplMgtService();
        List<AccuracyTplDTO> list = getListModel(accuracyTplMgtService.list(tenantCode));
        if (CollectionUtils.isEmpty(list)) {
            log.info("open rule list is empty, stop worker");
            return null;
        }

        JSONArray jsonArray = (JSONArray) JSON.toJSON(list);
        return jsonArray;
    }
}
