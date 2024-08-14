package com.langtuo.teamachine.web.controller;

import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.mqtt.MQTTService;
import com.langtuo.teamachine.mqtt.config.MQTTConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/mqtt")
@Slf4j
public class MQTTController {
    @Resource
    private MQTTService mqttService;

    /**
     * url: http://localhost:8080/teamachine/mqtt/test
     * @return
     */
    @GetMapping(value = "/test")
    public LangTuoResult<Void> index(Model model) {
        try {
            // mqttService.sendMsgByTopic("tenant_001", "testMq4Iot", "here is testMq4Iot test: " + System.currentTimeMillis());
            // mqttService.sendMsgByP2P("tenant_001", MQTTConfig.CLIENT_ID, "here is p2p test: " + System.currentTimeMillis());
        } catch (Exception e) {
            log.error("test error: " + e.getMessage(), e);
        }
        return LangTuoResult.error(ErrorEnum.TEST_ERR_ONLY_TEST);
    }
}
