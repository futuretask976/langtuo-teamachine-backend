package com.langtuo.teamachine.web.controller;

import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.biz.service.util.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/mqtt")
@Slf4j
public class MQTTController {
    @Autowired
    private MessageSource messageSource;

    /**
     * url: http://localhost:8080/teamachinebackend/mqtt/test
     * @return
     */
    @GetMapping(value = "/test")
    public TeaMachineResult<Void> test(Model model) {
        log.info("/mqtt/test entering: " + (model == null ? null : model.toString()));
        try {
            // mqttService.sendMsgByTopic("tenant_001", "testMq4Iot", "here is testMq4Iot test: " + System.currentTimeMillis());
            // mqttService.sendMsgByP2P("tenant_001", MQTTConfig.CLIENT_ID, "here is p2p test: " + System.currentTimeMillis());
        } catch (Exception e) {
            log.error("test error: " + e.getMessage(), e);
        }
        log.info("/mqtt/test exiting");
        return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.TEST_ERR_ONLY_TEST, messageSource));
    }
}
