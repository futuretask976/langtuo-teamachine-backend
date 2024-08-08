package com.langtuo.teamachine.mqtt.worker.dispatch;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.device.ModelDTO;
import com.langtuo.teamachine.api.service.device.ModelMgtService;
import com.langtuo.teamachine.mqtt.MQTTService;
import com.langtuo.teamachine.mqtt.config.MQTTConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.langtuo.teamachine.api.result.LangTuoResult.getListModel;

@Slf4j
public class ModelDispatchWorker implements Runnable {
    /**
     * 发送的消息中的key关键字
     */
    private static final String SEND_KEY_TOPIC = "topic";
    private static final String SEND_KEY_MODEL = "modelList";


    /**
     * 租户编码
     */
    private String tenantCode;

    public ModelDispatchWorker(String payload) {
    }

    @Override
    public void run() {
        JSONArray jsonArray = getDispatchCont();
        if (jsonArray == null) {
            log.info("dispatch content error, stop worker");
            return;
        }

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(SEND_KEY_TOPIC, MQTTConfig.TOPIC_DISPATCH_MODEL);
        jsonMsg.put(SEND_KEY_MODEL, jsonArray);
        MQTTService mqttService = getMQTTService();
        mqttService.sendMsgByTopic(MQTTConfig.TOPIC_DISPATCH_MODEL, jsonArray.toJSONString());
    }

    private MQTTService getMQTTService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MQTTService mqttService = appContext.getBean(MQTTService.class);
        return mqttService;
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
