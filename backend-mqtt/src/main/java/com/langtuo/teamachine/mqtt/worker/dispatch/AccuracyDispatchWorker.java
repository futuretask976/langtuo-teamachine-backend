package com.langtuo.teamachine.mqtt.worker.dispatch;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.drink.AccuracyTplDTO;
import com.langtuo.teamachine.api.service.drink.AccuracyTplMgtService;
import com.langtuo.teamachine.mqtt.MQTTService;
import com.langtuo.teamachine.mqtt.config.MQTTConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.langtuo.teamachine.api.result.LangTuoResult.getListModel;

@Slf4j
public class AccuracyDispatchWorker implements Runnable {
    /**
     * 收到的消息中的key关键字
     */
    private static final String RECEIVE_KEY_TENANT_CODE = "tenantCode";

    /**
     * 发送的消息中的key关键字
     */
    private static final String SEND_KEY_TOPIC = "topic";
    private static final String SEND_KEY_ACCURACY_TPL_LIST = "accuracyTplList";


    /**
     * 租户编码
     */
    private String tenantCode;

    public AccuracyDispatchWorker(String payload) {
        JSONObject jsonPayload = JSONObject.parseObject(payload);
        this.tenantCode = jsonPayload.getString(RECEIVE_KEY_TENANT_CODE);
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
        jsonMsg.put(SEND_KEY_TOPIC, MQTTConfig.TOPIC_DISPATCH_ACCURACY);
        jsonMsg.put(SEND_KEY_ACCURACY_TPL_LIST, jsonArray);
        MQTTService mqttService = getMQTTService();
        mqttService.sendMsgByTopic(MQTTConfig.TOPIC_DISPATCH_ACCURACY, jsonArray.toJSONString());
    }

    private MQTTService getMQTTService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MQTTService mqttService = appContext.getBean(MQTTService.class);
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
