package com.langtuo.teamachine.mqtt.worker;

import com.langtuo.teamachine.mqtt.config.MQTTConfig;
import org.apache.commons.lang3.StringUtils;

public class ConsumeWorker implements Runnable {
    /**
     * 已不包含parentTopic
     */
    private String subTopic;

    /**
     * 消息体，JSON格式
     */
    private String payload;

    /**
     *
     */
    private boolean isP2P = false;

    public ConsumeWorker(String topic, String payload) {
        if (StringUtils.isBlank(topic) || StringUtils.isBlank(payload)) {
            throw new IllegalArgumentException("ConsumeWorker param is illegal");
        }

        if (topic.startsWith(MQTTConfig.PARENT_P2P_TOPIC_PREFIX)) {
            isP2P = true;
        }
        this.subTopic = isP2P ? topic.substring(MQTTConfig.PARENT_P2P_TOPIC_PREFIX.length())
                : topic.substring(MQTTConfig.PARENT_TOPIC_PREFIX.length());
        this.payload = payload;
    }

    @Override
    public void run() {
        System.out.println("$$$$$ subTopic=" + subTopic + ", payload=" + payload);
    }
}
