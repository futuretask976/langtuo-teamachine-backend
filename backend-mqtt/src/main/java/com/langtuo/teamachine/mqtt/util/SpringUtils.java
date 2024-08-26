package com.langtuo.teamachine.mqtt.util;

import cn.hutool.extra.spring.SpringUtil;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import com.langtuo.teamachine.api.service.device.ModelMgtService;
import com.langtuo.teamachine.api.service.drink.AccuracyTplMgtService;
import com.langtuo.teamachine.api.service.drink.TeaMgtService;
import com.langtuo.teamachine.api.service.menu.MenuMgtService;
import com.langtuo.teamachine.api.service.menu.SeriesMgtService;
import com.langtuo.teamachine.api.service.rule.CleanRuleMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.api.service.user.TenantMgtService;
import com.langtuo.teamachine.mqtt.MqttService;
import org.springframework.context.ApplicationContext;

public class SpringUtils {
    public static MqttService getMQTTService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MqttService mqttService = appContext.getBean(MqttService.class);
        return mqttService;
    }

    public static MachineMgtService getMachineMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MachineMgtService machineMgtService = appContext.getBean(MachineMgtService.class);
        return machineMgtService;
    }

    public static TenantMgtService getTenantMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        TenantMgtService tenantMgtService = appContext.getBean(TenantMgtService.class);
        return tenantMgtService;
    }

    public static ModelMgtService getModelMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ModelMgtService modelMgtService = appContext.getBean(ModelMgtService.class);
        return modelMgtService;
    }

    public static AccuracyTplMgtService getToppingAccuracyTplMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        AccuracyTplMgtService accuracyTplMgtService = appContext.getBean(
                AccuracyTplMgtService.class);
        return accuracyTplMgtService;
    }

    public static MenuMgtService getMenuMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MenuMgtService menuMgtService = appContext.getBean(MenuMgtService.class);
        return menuMgtService;
    }

    public static ShopMgtService getShopMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ShopMgtService shopMgtService = appContext.getBean(ShopMgtService.class);
        return shopMgtService;
    }

    public static ShopMgtService getShopGrouMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ShopMgtService shopMgtService = appContext.getBean(ShopMgtService.class);
        return shopMgtService;
    }

    public static SeriesMgtService getSeriesMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        SeriesMgtService seriesMgtService = appContext.getBean(SeriesMgtService.class);
        return seriesMgtService;
    }

    public static TeaMgtService getTeaMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        TeaMgtService teaMgtService = appContext.getBean(TeaMgtService.class);
        return teaMgtService;
    }

    public static CleanRuleMgtService getCleanRuleMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        CleanRuleMgtService cleanRuleMgtService = appContext.getBean(CleanRuleMgtService.class);
        return cleanRuleMgtService;
    }
}
