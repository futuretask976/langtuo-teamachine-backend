package com.langtuo.teamachine.dao.constant;

public enum PermitActEnum {
    // 设备
    MODEL_MGT("model_mgt", "型号管理", "device_set"),
    DEPLOY_MGT("deploy_mgt", "预部署管理", "device_set"),
    MACHINE_MGT("machine_mgt", "设备管理", "device_set"),

    // 用户
    // TENANT_MGT("tenant_mgt", "商户管理", "user_set"),
    ORG_MGT("org_mgt", "组织管理", "user_set"),
    ROLE_MGT("role_mgt", "角色管理", "user_set"),
    PERMIT_ACT_MGT("permit_act_mgt", "权限管理", "user_set"),
    ADMIN_MGT("admin_mgt", "管理员管理", "user_set"),

    // 店铺
    SHOP_GROUP_MGT("shop_group_mgt", "店铺组管理", "shop_set"),
    SHOP_MGT("shop_mgt", "店铺管理", "shop_set"),

    // 饮品生产
    TOPPING_MGT("topping_mgt", "物料管理", "drink_set"),
    SPEC_MGT("spec_mgt", "规格管理", "drink_set"),
    TEA_MGT("tea_mgt", "茶品管理", "drink_set"),
    ACCURACY_MGT("accuracy_mgt", "物料精度模板管理", "drink_set"),

    // 菜单
    SERIES_MGT("series_mgt", "系列管理", "menu_set"),
    MENU_MGT("menu_mgt", "菜单管理", "menu_set"),

    // 食安规则
    OPEN_RULE_RULE_MGT("open_rule_mgt", "营业准备管理", "rule_set"),
    CLOSE_RULE_MGT("close_rule_mgt", "打烊准备管理", "rule_set"),
    CLEAN_RULE_MGT("clean_rule_mgt", "清洗规则管理", "rule_set"),
    WARNING_RULE_MGT("warning_rule_mgt", "预警规则管理", "rule_set"),

    // 报表
    INVALID_REC_MGT("invalid_rec_mgt", "废料记录管理", "report_set"),
    SUPPLY_REC_MGT("supply_rec_mgt", "补料记录管理", "report_set"),
    CLEAN_REC_MGT("clean_rec_mgt", "清洗记录管理", "report_set"),
    ORDER_REC_MGT("order_rec_mgt", "订单记录管理", "report_set"),
    ;

    /**
     *
     */
    private String permitActCode;

    /**
     *
     */
    private String permitActName;

    /**
     *
     */
    private String permitActGroupCode;

    public String getPermitActCode() {
        return permitActCode;
    }

    public String getPermitActName() {
        return permitActName;
    }

    public String getPermitActGroupCode() {
        return permitActGroupCode;
    }

    PermitActEnum(String permitActCode, String permitActName, String permitActGroupCode) {
        this.permitActCode = permitActCode;
        this.permitActName = permitActName;
        this.permitActGroupCode = permitActGroupCode;
    }

    public PermitActEnum valueOfByCode(String inputCode) {
        for (PermitActEnum permitActEnum : PermitActEnum.values()) {
            if (inputCode.equals(permitActEnum.getPermitActGroupCode())) {
                return permitActEnum;
            }
        }
        return null;
    }

    public PermitActEnum valueOfByName(String inputName) {
        for (PermitActEnum permitActEnum : PermitActEnum.values()) {
            if (inputName.equals(permitActEnum.getPermitActCode())) {
                return permitActEnum;
            }
        }
        return null;
    }

    public PermitActEnum valueOfByGroupName(String inputGroupCode) {
        for (PermitActEnum permitActEnum : PermitActEnum.values()) {
            if (inputGroupCode.equals(permitActEnum.getPermitActGroupCode())) {
                return permitActEnum;
            }
        }
        return null;
    }
}
