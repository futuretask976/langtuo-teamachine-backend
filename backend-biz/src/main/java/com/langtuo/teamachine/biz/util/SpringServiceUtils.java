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
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.api.service.user.*;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
import org.springframework.context.ApplicationContext;

public class SpringServiceUtils {
    public static MqttProducer getMqttProducer() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MqttProducer mqttProducer = appContext.getBean(MqttProducer.class);
        return mqttProducer;
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

    public static RoleMgtService getRoleMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        RoleMgtService roleMgtService = appContext.getBean(RoleMgtService.class);
        return roleMgtService;
    }

    public static OrgMgtService getOrgMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        OrgMgtService orgMgtService = appContext.getBean(OrgMgtService.class);
        return orgMgtService;
    }

    public static PermitActMgtService getPermitActMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        PermitActMgtService permitActMgtService = appContext.getBean(PermitActMgtService.class);
        return permitActMgtService;
    }

    public static AdminMgtService getAdminMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        AdminMgtService adminMgtService = appContext.getBean(AdminMgtService.class);
        return adminMgtService;
    }

    public static ModelMgtService getModelMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ModelMgtService modelMgtService = appContext.getBean(ModelMgtService.class);
        return modelMgtService;
    }

    public static AndroidAppMgtService getAndroidAppMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        AndroidAppMgtService androidAppMgtService = appContext.getBean(AndroidAppMgtService.class);
        return androidAppMgtService;
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

    public static InvalidActRecordMgtService getInvalidActRecordMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        InvalidActRecordMgtService invalidActRecordMgtService = appContext.getBean(InvalidActRecordMgtService.class);
        return invalidActRecordMgtService;
    }

    public static SupplyActRecordMgtService getSupplyActRecordMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        SupplyActRecordMgtService supplyActRecordMgtService = appContext.getBean(SupplyActRecordMgtService.class);
        return supplyActRecordMgtService;
    }

    public static CleanActRecordMgtService getCleanActRecordMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        CleanActRecordMgtService cleanActRecordMgtService = appContext.getBean(CleanActRecordMgtService.class);
        return cleanActRecordMgtService;
    }

    public static DrainActRecordMgtService getDrainActRecordMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        DrainActRecordMgtService drainActRecordMgtService = appContext.getBean(DrainActRecordMgtService.class);
        return drainActRecordMgtService;
    }

    public static OrderActRecordMgtService getOrderActRecordMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        OrderActRecordMgtService orderActRecordMgtService = appContext.getBean(OrderActRecordMgtService.class);
        return orderActRecordMgtService;
    }
}
