package com.gx.sp3.demo.gtmf.config.switcher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class CommonBizMonitorSwitch implements InitializingBean {
    public static volatile boolean isOpen = true;

    public static volatile String whitelist = ",99999999,88888888,";

    public static volatile boolean infoOpen = false;

    public static volatile boolean warnOpen = false;

    public static volatile boolean errorOpen = false;

    public static volatile boolean finalOpen = true;

    @Override
    public void afterPropertiesSet() throws Exception {
        SwitchManager.init(CommonBizMonitorSwitch.class);
    }
}
