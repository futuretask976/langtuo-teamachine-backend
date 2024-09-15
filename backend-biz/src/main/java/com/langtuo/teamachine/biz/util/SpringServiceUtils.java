package com.langtuo.teamachine.biz.util;

import cn.hutool.extra.spring.SpringUtil;
import com.langtuo.teamachine.api.service.device.AndroidAppMgtService;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import com.langtuo.teamachine.api.service.device.ModelMgtService;
import com.langtuo.teamachine.api.service.drink.AccuracyTplMgtService;
import com.langtuo.teamachine.api.service.drink.TeaMgtService;
import com.langtuo.teamachine.api.service.menu.MenuMgtService;
import com.langtuo.teamachine.api.service.menu.SeriesMgtService;
import com.langtuo.teamachine.api.service.record.*;
import com.langtuo.teamachine.api.service.rule.CleanRuleMgtService;
import com.langtuo.teamachine.api.service.rule.DrainRuleMgtService;
import com.langtuo.teamachine.api.service.rule.WarningRuleMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.api.service.user.*;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
import org.springframework.context.ApplicationContext;

public class SpringServiceUtils {
    public static MqttProducer getMqttProducer() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MqttProducer service = appContext.getBean(MqttProducer.class);
        return service;
    }

    public static MachineMgtService getMachineMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MachineMgtService service = appContext.getBean(MachineMgtService.class);
        return service;
    }

    public static ModelMgtService getModelMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ModelMgtService service = appContext.getBean(ModelMgtService.class);
        return service;
    }

    public static AndroidAppMgtService getAndroidAppMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        AndroidAppMgtService service = appContext.getBean(AndroidAppMgtService.class);
        return service;
    }

    public static AccuracyTplMgtService getToppingAccuracyTplMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        AccuracyTplMgtService service = appContext.getBean(AccuracyTplMgtService.class);
        return service;
    }

    public static MenuMgtService getMenuMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MenuMgtService service = appContext.getBean(MenuMgtService.class);
        return service;
    }

    public static SeriesMgtService getSeriesMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        SeriesMgtService service = appContext.getBean(SeriesMgtService.class);
        return service;
    }

    public static TeaMgtService getTeaMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        TeaMgtService service = appContext.getBean(TeaMgtService.class);
        return service;
    }

    public static DrainRuleMgtService getDrainRuleMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        DrainRuleMgtService service = appContext.getBean(DrainRuleMgtService.class);
        return service;
    }

    public static CleanRuleMgtService getCleanRuleMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        CleanRuleMgtService service = appContext.getBean(CleanRuleMgtService.class);
        return service;
    }

    public static WarningRuleMgtService getWarningRuleMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        WarningRuleMgtService service = appContext.getBean(WarningRuleMgtService.class);
        return service;
    }
}
