package com.langtuo.teamachine.mqtt.constant;

public class MqttConsts {
    /**
     * 收到的消息中的key关键字
     */
    public static final String RECEIVE_KEY_BIZ_CODE = "bizCode";
    public static final String RECEIVE_KEY_TENANT_CODE = "tenantCode";
    public static final String RECEIVE_KEY_MODEL_CODE = "modelCode";
    public static final String RECEIVE_KEY_MACHINE_CODE = "machineCode";
    public static final String RECEIVE_KEY_TEMPLATE_CODE = "templateCode";
    public static final String RECEIVE_KEY_MENU_CODE = "menuCode";
    public static final String RECEIVE_KEY_CLEAN_RULE_CODE = "cleanRuleCode";
    public static final String RECEIVE_KEY_OPEN_RULE_CODE = "openRuleCode";
    public static final String RECEIVE_KEY_WARNING_RULE_CODE = "warningRuleCode";


    /**
     * 发送的消息中的key关键字
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
    public static final String SEND_KEY_MENU_CODE = "menuCode";
    public static final String SEND_KEY_OPEN_RULE_CODE = "openRuleCode";
    public static final String SEND_KEY_OPEN_RULE = "openRule";
    public static final String SEND_KEY_CLEAN_RULE_CODE = "cleanRuleCode";
    public static final String SEND_KEY_CLEAN_RULE = "cleanRule";
    public static final String SEND_KEY_WARNING_RULE_CODE = "warningRuleCode";
    public static final String SEND_KEY_WARNING_RULE = "warningRule";

    /**
     * console用的消息title
     */
    public static final String BIZ_CODE_PREPARE_MODEL = "prepare_model";
    public static final String BIZ_CODE_PREPARE_MACHINE = "prepare_machine";
    public static final String BIZ_CODE_PREPARE_ACCURACY_TPL = "prepare_accuracy";
    public static final String BIZ_CODE_PREPARE_MENU = "prepare_menu";
    public static final String BIZ_CODE_PREPARE_OPEN_RULE = "prepare_open_rule";
    public static final String BIZ_CODE_PREPARE_CLEAN_RULE = "prepare_clean_rule";
    public static final String BIZ_CODE_PREPARE_WARNING_RULE = "prepare_warning_rule";

    /**
     * dispatch用的消息title
     */
    public static final String BIZ_CODE_DISPATCH_ACCURACY = "dispatch_accuracy";
    public static final String BIZ_CODE_DISPATCH_CLEAN_RULE = "dispatch_clean_rule";
    public static final String BIZ_CODE_DISPATCH_CLOSE_RULE = "dispatch_close_rule";
    public static final String BIZ_CODE_DISPATCH_MACHINE = "dispatch_machine";
    public static final String BIZ_CODE_DISPATCH_MENU = "dispatch_menu";
    public static final String BIZ_CODE_DISPATCH_MODEL = "dispatch_model";
    public static final String BIZ_CODE_DISPATCH_OPEN_RULE = "dispatch_open_rule";
    public static final String BIZ_CODE_DISPATCH_WARNING_RULE = "dispatch_warning_rule";

    /**
     * topic相关常量
     */
    public static final String CONSOLE_PARENT_TOPIC = "teamachine";
    public static final String TENANT_PARENT_TOPIC_POSTFIX = "_teamachine";
    public static final String TENANT_PARENT_P2P_TOPIC_POSTFIX = "_teamachine/p2p";

    /**
     * topic分隔符
     */
    public static final String TOPIC_SEPERATOR = "/";
}