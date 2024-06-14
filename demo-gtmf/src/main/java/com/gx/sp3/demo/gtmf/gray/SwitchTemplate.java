package com.gx.sp3.demo.gtmf.gray;

import java.util.HashSet;
import java.util.Set;

// generate template
// 这里内容不能轻易变动！！！
// @NameSpace(nameSpace = "Product.${bizCode}", desc = "Product.${bizCode} 的灰度开关}")
public class SwitchTemplate {

    static {
        System.out.println("init！！！！！！！！！！");
    }

    // @AppSwitch(des = "${bizCode} 的用户灰度开关", level = Switch.Level.p1)
    public static volatile long userIdGrayRate = 0;

    // @AppSwitch(des = "${bizCode} 的灰度白名单", level = Switch.Level.p1)
    public static Set<Long> userIdWhiteList = new HashSet<>();

    // @AppSwitch(des = "${bizCode} 手淘Android灰度控制", level = Switch.Level.p1)
    public static ClientVersionConfig taobaoClientAndroidConfig = new ClientVersionConfig();

    // @AppSwitch(des = "${bizCode} 手淘IOS", level = Switch.Level.p1)
    public static ClientVersionConfig taobaoClientTaoIOSConfig = new ClientVersionConfig();

    // @AppSwitch(des = "${bizCode} 猫客Android灰度控制", level = Switch.Level.p1)
    public static ClientVersionConfig tmallClientAndroidConfig = new ClientVersionConfig();

    // @AppSwitch(des = "${bizCode} 猫客IOS灰度控制", level = Switch.Level.p1)
    public static ClientVersionConfig tmallClientIOSConfig = new ClientVersionConfig();

    // @AppSwitch(des = "${bizCode} 其他端生效名单,__all__表示全部生效", level = Switch.Level.p1)
    public static Set<String> enableOtherClients = new HashSet<>();
}
