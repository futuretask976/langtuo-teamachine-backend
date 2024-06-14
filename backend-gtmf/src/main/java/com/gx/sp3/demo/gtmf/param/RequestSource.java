package com.gx.sp3.demo.gtmf.param;

import com.gx.sp3.demo.gtmf.consts.ClientTypeEnum;
import lombok.Data;

/**
 * @author miya
 */
@Data
public class RequestSource {
    /**
     *
     */
    private UserAgent userAgent;

    /**
     *
     */
    private String ttid;

    /**
     *
     */
    private String appName;

    /**
     *
     */
    private String appVersion;

    /**
     *
     */
    private ClientTypeEnum clientTypeEnum;

    /**
     *
     */
    private String referer;
}
