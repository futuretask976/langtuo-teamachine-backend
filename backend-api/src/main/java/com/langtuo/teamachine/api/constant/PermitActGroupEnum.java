package com.langtuo.teamachine.api.constant;

import lombok.Data;

public enum PermitActGroupEnum {
    USER_SET("user_set", "用户"),
    SHOP_SET("shop_set", "店铺"),
    DEVICE_SET("device_set", "设备"),
    DRINK_SET("drink_set", "饮品生产"),
    MENU_SET("menu_set", "菜单"),
    RULE_SET("rule_set", "食安规则"),
    REPORT_SET("report_set", "日常报表"),
    ;

    /**
     *
     */
    private String permitActGroupCode;

    /**
     *
     */
    private String permitActGroupName;

    public String getPermitActGroupCode() {
        return permitActGroupCode;
    }

    public String getPermitActGroupName() {
        return permitActGroupName;
    }

    PermitActGroupEnum(String permitActGroupCode, String permitActGroupName) {
        this.permitActGroupCode = permitActGroupCode;
        this.permitActGroupName = permitActGroupName;
    }

    public static PermitActGroupEnum valueOfByCode(String inputCode) {
        for (PermitActGroupEnum permitActGroupEnum : PermitActGroupEnum.values()) {
            if (inputCode.equals(permitActGroupEnum.getPermitActGroupCode())) {
                return permitActGroupEnum;
            }
        }
        return null;
    }

    public static PermitActGroupEnum valueOfByName(String inputName) {
        for (PermitActGroupEnum permitActGroupEnum : PermitActGroupEnum.values()) {
            if (inputName.equals(permitActGroupEnum.getPermitActGroupCode())) {
                return permitActGroupEnum;
            }
        }
        return null;
    }
}
