package com.langtuo.teamachine.biz.service.util;

import cn.hutool.extra.spring.SpringUtil;
import com.langtuo.teamachine.dao.accessor.device.DeployAccessor;
import com.langtuo.teamachine.dao.accessor.drink.*;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.accessor.user.AdminAccessor;
import com.langtuo.teamachine.dao.accessor.user.OrgAccessor;
import com.langtuo.teamachine.dao.accessor.user.TenantAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

public class SpringUtils {
    public static AdminAccessor getAdminAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        AdminAccessor adminAccessor = appContext.getBean(AdminAccessor.class);
        return adminAccessor;
    }

    public static OrgAccessor getOrgAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        OrgAccessor orgAccessor = appContext.getBean(OrgAccessor.class);
        return orgAccessor;
    }

    public static ShopGroupAccessor getShopGroupAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ShopGroupAccessor shopGroupAccessor = appContext.getBean(ShopGroupAccessor.class);
        return shopGroupAccessor;
    }

    public static ShopAccessor getShopAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ShopAccessor shopAccessor = appContext.getBean(ShopAccessor.class);
        return shopAccessor;
    }

    public static TenantAccessor getTenantAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        TenantAccessor tenantAccessor = appContext.getBean(TenantAccessor.class);
        return tenantAccessor;
    }

    public static DeployAccessor getDeployAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        DeployAccessor deployAccessor = appContext.getBean(DeployAccessor.class);
        return deployAccessor;
    }

    public static TeaAccessor getTeaAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        TeaAccessor teaAccessor = appContext.getBean(TeaAccessor.class);
        return teaAccessor;
    }

    public static ToppingBaseRuleAccessor getToppingBaseRuleAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ToppingBaseRuleAccessor toppingBaseRuleAccessor = appContext.getBean(ToppingBaseRuleAccessor.class);
        return toppingBaseRuleAccessor;
    }

    public static ToppingAdjustRuleAccessor getToppingAdjustRuleAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ToppingAdjustRuleAccessor toppingAdjustRuleAccessor = appContext.getBean(ToppingAdjustRuleAccessor.class);
        return toppingAdjustRuleAccessor;
    }

    public static TeaUnitAccessor getTeaUnitAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        TeaUnitAccessor teaUnitAccessor = appContext.getBean(TeaUnitAccessor.class);
        return teaUnitAccessor;
    }

    public static ToppingAccessor getToppingAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ToppingAccessor toppingAccessor = appContext.getBean(ToppingAccessor.class);
        return toppingAccessor;
    }

    public static SpecItemAccessor getSpecItemAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        SpecItemAccessor specItemAccessor = appContext.getBean(SpecItemAccessor.class);
        return specItemAccessor;
    }

    public static MessageSource getMessageSource() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MessageSource messageSource = appContext.getBean(MessageSource.class);
        return messageSource;
    }
}
