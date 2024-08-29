package com.langtuo.teamachine.mqtt.constant;

public class MqttConsts {
    /**
     * 收到的消息中的 key 关键字
     */
    public static final String RECEIVE_KEY_BIZ_CODE = "bizCode";
    public static final String RECEIVE_KEY_TENANT_CODE = "tenantCode";
    public static final String RECEIVE_KEY_MODEL_CODE = "modelCode";
    public static final String RECEIVE_KEY_MACHINE_CODE = "machineCode";
    public static final String RECEIVE_KEY_SHOP_CODE = "shopCode";
    public static final String RECEIVE_KEY_TEMPLATE_CODE = "templateCode";
    public static final String RECEIVE_KEY_MENU_CODE = "menuCode";
    public static final String RECEIVE_KEY_CLEAN_RULE_CODE = "cleanRuleCode";
    public static final String RECEIVE_KEY_DRAIN_RULE_CODE = "drainRuleCode";
    public static final String RECEIVE_KEY_WARNING_RULE_CODE = "warningRuleCode";
    public static final String RECEIVE_KEY_LIST = "list";


    /**
     * 发送的消息中的 key 关键字
     */
    public static final String SEND_KEY_BIZ_CODE = "bizCode";
    public static final String SEND_KEY_TENANT_CODE = "tenantCode";
    public static final String SEND_KEY_MODEL_CODE = "modelCode";
    public static final String SEND_KEY_MODEL = "model";
    public static final String SEND_KEY_MACHINE_CODE = "machineCode";
    public static final String SEND_KEY_MACHINE = "machine";
    public static final String SEND_KEY_MD5_AS_HEX = "md5AsHex";
    public static final String SEND_KEY_OSS_PATH = "ossPath";
    public static final String SEND_KEY_TEMPLATE_CODE = "templateCode";
    public static final String SEND_KEY_ACCURACY_TPL = "accuracyTpl";
    public static final String SEND_KEY_SHOP_CODE = "shopCode";
    public static final String SEND_KEY_MENU_CODE = "menuCode";
    public static final String SEND_KEY_DRAIN_RULE_CODE = "openRuleCode";
    public static final String SEND_KEY_OPEN_RULE = "openRule";
    public static final String SEND_KEY_CLEAN_RULE_CODE = "cleanRuleCode";
    public static final String SEND_KEY_CLEAN_RULE = "cleanRule";
    public static final String SEND_KEY_WARNING_RULE_CODE = "warningRuleCode";
    public static final String SEND_KEY_WARNING_RULE = "warningRule";

    /**
     * console 用的消息 bizCode
     */
    public static final String BIZ_CODE_PREPARE_MODEL = "prepare_model";
    public static final String BIZ_CODE_PREPARE_MACHINE = "prepare_machine";
    public static final String BIZ_CODE_PREPARE_ACCURACY_TPL = "prepare_accuracy";
    public static final String BIZ_CODE_PREPARE_MENU = "prepare_menu";
    public static final String BIZ_CODE_PREPARE_MENU_INIT_LIST = "prepare_menu_init_list";
    public static final String BIZ_CODE_PREPARE_DRAIN_RULE = "prepare_open_rule";
    public static final String BIZ_CODE_PREPARE_CLEAN_RULE = "prepare_clean_rule";
    public static final String BIZ_CODE_PREPARE_WARNING_RULE = "prepare_warning_rule";

    /**
     * dispatch 用的消息 bizCode
     */
    public static final String BIZ_CODE_DISPATCH_ACCURACY = "dispatch_accuracy";
    public static final String BIZ_CODE_DISPATCH_CLEAN_RULE = "dispatch_clean_rule";
    public static final String BIZ_CODE_DISPATCH_CLOSE_RULE = "dispatch_close_rule";
    public static final String BIZ_CODE_DISPATCH_MACHINE = "dispatch_machine";
    public static final String BIZ_CODE_DISPATCH_MENU = "dispatch_menu";
    public static final String BIZ_CODE_DISPATCH_MENU_INIT_LIST = "dispatch_menu_init_list";
    public static final String BIZ_CODE_DISPATCH_MODEL = "dispatch_model";
    public static final String BIZ_CODE_DISPATCH_OPEN_RULE = "dispatch_open_rule";
    public static final String BIZ_CODE_DISPATCH_WARNING_RULE = "dispatch_warning_rule";

    /**
     * 接受设备端消息用的 bizCode
     */
    public static final String BIZ_CODE_INVALID_ACT_RECORD = "invalidActRecord";
    public static final String BIZ_CODE_SUPPLY_ACT_RECORD = "supplyActRecord";
    public static final String BIZ_CODE_DRAIN_ACT_RECORD = "drainActRecord";
    public static final String BIZ_CODE_CLEAN_ACT_RECORD = "cleanActRecord";
    public static final String BIZ_CODE_ORDER_ACT_RECORD = "orderActRecord";

    /**
     * topic 相关常量
     */
    public static final String CONSOLE_PARENT_TOPIC = "teamachine";
    public static final String TENANT_PARENT_TOPIC_POSTFIX = "_teamachine";
    public static final String TENANT_PARENT_P2P_TOPIC_POSTFIX = "_teamachine/p2p";

    /**
     * topic 分隔符
     */
    public static final String TOPIC_SEPERATOR = "/";
}
