package com.gx.sp3.demo.gtmf.consts;

import lombok.Getter;

/**
 * @author miya
 */
public enum OperatingSystemEnum {
    UNKNOWN("unknown", "未知来源"),
    IOS("IOS", "IOS"),
    ANDROID("ANDROID", "ANDROID"),
    ;

    @Getter
    private String code;

    @Getter
    private String desc;

    OperatingSystemEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OperatingSystemEnum fromCode(String code) {
        if (null == code) {
            return UNKNOWN;
        }
        for (OperatingSystemEnum typeEnum : OperatingSystemEnum.values()) {
            if (typeEnum.code.equals(code)) {
                return typeEnum;
            }
        }
        return UNKNOWN;
    }
}
