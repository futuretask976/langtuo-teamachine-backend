package com.langtuo.teamachine.dao.util;

import cn.hutool.extra.spring.SpringUtil;
import com.langtuo.teamachine.dao.accessor.device.AndroidAppDispatchAccessor;
import com.langtuo.teamachine.dao.accessor.device.DeployAccessor;
import com.langtuo.teamachine.dao.accessor.device.MachineAccessor;
import com.langtuo.teamachine.dao.accessor.drink.*;
import com.langtuo.teamachine.dao.accessor.menu.MenuDispatchAccessor;
import com.langtuo.teamachine.dao.accessor.record.*;
import com.langtuo.teamachine.dao.accessor.rule.CleanRuleDispatchAccessor;
import com.langtuo.teamachine.dao.accessor.rule.DrainRuleDispatchAccessor;
import com.langtuo.teamachine.dao.accessor.rule.WarningRuleDispatchAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.accessor.user.*;
import org.springframework.context.ApplicationContext;

public class SpringAccessorUtils {
    public static OrderToppingActRecordAccessor getOrderToppingActRecordAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        OrderToppingActRecordAccessor accessor = appContext.getBean(OrderToppingActRecordAccessor.class);
        return accessor;
    }

    public static MenuDispatchAccessor getMenuDispatchAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MenuDispatchAccessor accessor = appContext.getBean(MenuDispatchAccessor.class);
        return accessor;
    }

    public static CleanRuleDispatchAccessor getCleanRuleDispatchAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        CleanRuleDispatchAccessor accessor = appContext.getBean(CleanRuleDispatchAccessor.class);
        return accessor;
    }

    public static DrainRuleDispatchAccessor getDrainRuleDispatchAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        DrainRuleDispatchAccessor accessor = appContext.getBean(DrainRuleDispatchAccessor.class);
        return accessor;
    }

    public static WarningRuleDispatchAccessor getWarningRuleDispatchAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        WarningRuleDispatchAccessor accessor = appContext.getBean(WarningRuleDispatchAccessor.class);
        return accessor;
    }

    public static OrderSpecItemActRecordAccessor getOrderSpecItemActRecordAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        OrderSpecItemActRecordAccessor accessor = appContext.getBean(OrderSpecItemActRecordAccessor.class);
        return accessor;
    }

    public static OrderActRecordAccessor getOrderActRecordAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        OrderActRecordAccessor accessor = appContext.getBean(OrderActRecordAccessor.class);
        return accessor;
    }

    public static SupplyActRecordAccessor getSupplyActRecordAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        SupplyActRecordAccessor accessor = appContext.getBean(SupplyActRecordAccessor.class);
        return accessor;
    }

    public static InvalidActRecordAccessor getInvalidActRecordAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        InvalidActRecordAccessor accessor = appContext.getBean(InvalidActRecordAccessor.class);
        return accessor;
    }

    public static DrainActRecordAccessor getDrainActRecordAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        DrainActRecordAccessor accessor = appContext.getBean(DrainActRecordAccessor.class);
        return accessor;
    }

    public static CleanActRecordAccessor getCleanActRecordAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        CleanActRecordAccessor accessor = appContext.getBean(CleanActRecordAccessor.class);
        return accessor;
    }

    public static AdminAccessor getAdminAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        AdminAccessor accessor = appContext.getBean(AdminAccessor.class);
        return accessor;
    }

    public static OrgAccessor getOrgAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        OrgAccessor accessor = appContext.getBean(OrgAccessor.class);
        return accessor;
    }

    public static ShopGroupAccessor getShopGroupAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ShopGroupAccessor accessor = appContext.getBean(ShopGroupAccessor.class);
        return accessor;
    }

    public static ShopAccessor getShopAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ShopAccessor accessor = appContext.getBean(ShopAccessor.class);
        return accessor;
    }

    public static TenantAccessor getTenantAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        TenantAccessor accessor = appContext.getBean(TenantAccessor.class);
        return accessor;
    }

    public static RoleAccessor getRoleAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        RoleAccessor accessor = appContext.getBean(RoleAccessor.class);
        return accessor;
    }

    public static RoleActRelAccessor getRoleActRelAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        RoleActRelAccessor accessor = appContext.getBean(RoleActRelAccessor.class);
        return accessor;
    }

    public static PermitActAccessor getPermitActAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        PermitActAccessor accessor = appContext.getBean(PermitActAccessor.class);
        return accessor;
    }

    public static DeployAccessor getDeployAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        DeployAccessor accessor = appContext.getBean(DeployAccessor.class);
        return accessor;
    }

    public static TeaAccessor getTeaAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        TeaAccessor accessor = appContext.getBean(TeaAccessor.class);
        return accessor;
    }

    public static ToppingBaseRuleAccessor getToppingBaseRuleAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ToppingBaseRuleAccessor accessor = appContext.getBean(ToppingBaseRuleAccessor.class);
        return accessor;
    }

    public static ToppingAdjustRuleAccessor getToppingAdjustRuleAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ToppingAdjustRuleAccessor accessor = appContext.getBean(ToppingAdjustRuleAccessor.class);
        return accessor;
    }

    public static TeaUnitAccessor getTeaUnitAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        TeaUnitAccessor accessor = appContext.getBean(TeaUnitAccessor.class);
        return accessor;
    }

    public static ToppingAccessor getToppingAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ToppingAccessor accessor = appContext.getBean(ToppingAccessor.class);
        return accessor;
    }

    public static SpecItemAccessor getSpecItemAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        SpecItemAccessor accessor = appContext.getBean(SpecItemAccessor.class);
        return accessor;
    }

    public static MachineAccessor getMachineItemAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MachineAccessor accessor = appContext.getBean(MachineAccessor.class);
        return accessor;
    }

    public static AndroidAppDispatchAccessor getAndroidAppDispatchAccessor() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        AndroidAppDispatchAccessor accessor = appContext.getBean(AndroidAppDispatchAccessor.class);
        return accessor;
    }
}