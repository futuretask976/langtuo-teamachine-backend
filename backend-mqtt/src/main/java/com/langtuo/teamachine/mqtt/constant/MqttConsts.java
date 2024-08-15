package com.langtuo.teamachine.mqtt.constant;

public class MqttConsts {
    /**
     * 收到的消息中的key关键字
     */
    public static final String RECEIVE_KEY_TITLE = "title";
    public static final String RECEIVE_KEY_TENANT_CODE = "tenantCode";
    public static final String RECEIVE_KEY_CLEAN_RULE_CODE = "cleanRuleCode";
    public static final String RECEIVE_KEY_MACHINE_CODE = "machineCode";
    public static final String RECEIVE_KEY_MENU_CODE = "menuCode";
    public static final String RECEIVE_KEY_OPEN_RULE_CODE = "openRuleCode";
    public static final String RECEIVE_KEY_WARNING_RULE_CODE = "warningRuleCode";


    /**
     * 发送的消息中的key关键字
     */
    public static final String SEND_KEY_TITLE = "title";
    public static final String SEND_KEY_ACCURACY_TPL_LIST = "accuracyTplList";
    public static final String SEND_KEY_MODEL_LIST = "modelList";
    public static final String SEND_KEY_CLEAN_RULE = "cleanRule";
    public static final String SEND_KEY_OPEN_RULE = "openRule";
    public static final String SEND_KEY_WARNING_RULE = "warningRule";
    public static final String SEND_KEY_MACHINE = "machine";
    public static final String SEND_KEY_MD5_AS_HEX = "md5AsHex";
    public static final String SEND_KEY_OSS_PATH = "ossPath";

    /**
     * console用的消息title
     */
    public static final String CONSOLE_MSG_TITLE_PREPARE_ACCURACY = "prepare_accuracy";
    public static final String CONSOLE_MSG_TITLE_PREPARE_CLEAN_RULE = "prepare_clean_rule";
    public static final String CONSOLE_MSG_TITLE_PREPARE_CLOSE_RULE = "prepare_close_rule";
    public static final String CONSOLE_MSG_TITLE_PREPARE_MACHINE = "prepare_machine";
    public static final String CONSOLE_MSG_TITLE_PREPARE_MENU = "prepare_menu";
    public static final String CONSOLE_MSG_TITLE_PREPARE_MODEL = "prepare_model";
    public static final String CONSOLE_MSG_TITLE_PREPARE_OPEN_RULE = "prepare_open_rule";
    public static final String CONSOLE_MSG_TITLE_PREPARE_WARNING_RULE = "prepare_warning_rule";

    /**
     * dispatch用的消息title
     */
    public static final String SEND_MSG_TITLE_DISPATCH_ACCURACY = "dispatch_accuracy";
    public static final String SEND_MSG_TITLE_DISPATCH_CLEAN_RULE = "dispatch_clean_rule";
    public static final String SEND_MSG_TITLE_DISPATCH_CLOSE_RULE = "dispatch_close_rule";
    public static final String SEND_MSG_TITLE_DISPATCH_MACHINE = "dispatch_machine";
    public static final String SEND_MSG_TITLE_DISPATCH_MENU = "dispatch_menu";
    public static final String SEND_MSG_TITLE_DISPATCH_MODEL = "dispatch_model";
    public static final String SEND_MSG_TITLE_DISPATCH_OPEN_RULE = "dispatch_open_rule";
    public static final String SEND_MSG_TITLE_DISPATCH_WARNING_RULE = "dispatch_warning_rule";
}
