package com.langtuo.teamachine.mqtt.consume.worker.record;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.langtuo.teamachine.api.request.record.InvalidActRecordPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.InvalidActRecordMgtService;
import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Slf4j
public class InvalidActRecordWorker implements Runnable {
    /**
     *
     */
    private List<InvalidActRecordPutRequest> requestList = Lists.newArrayList();

    public InvalidActRecordWorker(JSONObject jsonPayload) {
        JSONArray jsonList = jsonPayload.getJSONArray(MqttConsts.RECEIVE_KEY_LIST);
        jsonList.forEach(jsonObject -> {
            InvalidActRecordPutRequest request = TypeUtils.castToJavaBean(jsonObject, InvalidActRecordPutRequest.class);
            if (request.isValid()) {
                requestList.add(request);
            } else {
                log.error("request is invalid: " + jsonObject == null ? null : JSON.toJSONString(jsonObject));
            }
        });
        if (CollectionUtils.isEmpty(requestList)) {
            throw new IllegalArgumentException("request list is empty");
        }
    }

    @Override
    public void run() {
        InvalidActRecordMgtService invalidActRecordMgtService = SpringUtils.getInvalidActRecordMgtService();
        for (InvalidActRecordPutRequest request : requestList) {
            TeaMachineResult<Void> result = invalidActRecordMgtService.put(request);
            if (result == null || !result.isSuccess()) {
                log.error("insert invalid act record error: " + result == null ? null : result.getErrorMsg());
            }
        }
    }
}
