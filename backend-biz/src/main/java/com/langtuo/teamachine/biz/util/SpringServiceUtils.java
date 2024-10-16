package com.langtuo.teamachine.biz.util;

import cn.hutool.extra.spring.SpringUtil;
import com.langtuo.teamachine.api.service.device.AndroidAppMgtService;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import com.langtuo.teamachine.api.service.device.ModelMgtService;
import com.langtuo.teamachine.api.service.drink.AccuracyTplMgtService;
import com.langtuo.teamachine.api.service.drink.TeaMgtService;
import com.langtuo.teamachine.api.service.menu.MenuMgtService;
import com.langtuo.teamachine.api.service.menu.SeriesMgtService;
import com.langtuo.teamachine.api.service.rule.CleanRuleMgtService;
import com.langtuo.teamachine.api.service.rule.DrainRuleMgtService;
import com.langtuo.teamachine.api.service.rule.WarningRuleMgtService;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
import org.springframework.context.ApplicationContext;

public class SpringServiceUtils {
    public static MqttProducer getMqttProducer() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MqttProducer service = appContext.getBean(MqttProducer.class);
        return service;
    }
}
