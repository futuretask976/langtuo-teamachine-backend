package com.gx.sp3.demo.gtmf.config.switcher;

import com.gx.sp3.demo.gtmf.annotation.ProductSwitchInfo;

/**
 * @author miya 
 */
// @NameSpace(nameSpace = "filter.switch")
@ProductSwitchInfo
public class FilterSwitch {
    // @AppSwitch(des = "前置灰度过滤开关", level = Switch.Level.p4)
    public static volatile Boolean isOpen = false;

    // @AppSwitch(des = "是否计算kdDiscount中的blackGoldCard", level = Switch.Level.p4)
    public static volatile boolean enableBlackGoldCardCalc = false;

    // @AppSwitch(des = "是否开启计算coupon288vipcard", level = Switch.Level.p4)
    public static volatile boolean enableCoupon288vipcard = false;
}
