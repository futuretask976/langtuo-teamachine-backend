package com.langtuo.teamachine.mqtt.produce.callback;

import com.alibaba.mqtt.server.callback.SendCallback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttSendCallback implements SendCallback {
    @Override
    public void onSuccess(String msgId) {
        log.info("MqttSendCallback|sendMsg|onSuccess|" + msgId);
    }

    @Override
    public void onFail() {
        log.error("MqttSendCallback|sendMsg|onFail|");
    }
}
