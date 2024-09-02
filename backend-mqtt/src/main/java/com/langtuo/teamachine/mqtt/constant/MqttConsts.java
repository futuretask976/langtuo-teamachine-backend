package com.langtuo.teamachine.mqtt.constant;

public class MqttConsts {
    /**
     * MQ4IOT 实例 ID，购买后控制台获取
     */
    public static final String INSTANCE_ID = "post-cn-kvw3w72ds01";
    /**
     * 接入点地址，购买 MQ4IOT 实例，且配置完成后即可获取，接入点地址必须填写分配的域名，不得使用 IP 地址直接连接，否则可能会导致客户端异常。
     */
    public static final String ENDPOINT = "post-cn-kvw3w72ds01.mqtt.aliyuncs.com";

    /**
     * 账号 accesskey，从账号系统控制台获取
     * 阿里云账号AccessKey拥有所有API的访问权限，建议您使用RAM用户进行API访问或日常运维。
     * 强烈建议不要把AccessKey ID和AccessKey Secret保存到工程代码里，否则可能导致AccessKey泄露，威胁您账号下所有资源的安全。
     * 本示例以把AccessKey ID和AccessKey Secret保存在环境变量为例说明。运行本代码示例之前，请先配置环境变量MQTT_AK_ENV和MQTT_SK_ENV
     * 例如：export MQTT_AK_ENV=<access_key_id>
     *      export MQTT_SK_ENV=<access_key_secret>
     * 需要将<access_key_id>替换为已准备好的AccessKey ID，<access_key_secret>替换为AccessKey Secret。
     */
    public static final String ACCESS_KEY = "LTAI5t6hg6snjTBEddAP8tz8"; // System.getenv("MQTT_AK_ENV");

    /**
     * 账号 secretKey，从账号系统控制台获取，仅在Signature鉴权模式下需要设置
     */
    public static final String ACCESS_KEY_SECRET = "MammwnIOPrHe9AAO4CnaUJwmIG96Kc"; // System.getenv("MQTT_SK_ENV");

    /**
     * QoS参数代表传输质量，可选0，1，2，根据实际需求合理设置，具体参考 https://help.aliyun.com/document_detail/42420.html?spm=a2c4g.11186623.6.544.1ea529cfAO5zV3
     */
    public static final int QOS_LEVEL = 0;

    /**
     * MQ4IOT clientId，由业务系统分配，需要保证每个 tcp 连接都不一样，保证全局唯一，如果不同的客户端对象（tcp 连接）使用了相同的 clientId 会导致连接异常断开。
     * clientId 由两部分组成，格式为 GroupID@@@DeviceId，其中 groupId 在 MQ4IOT 控制台申请，DeviceId 由业务方自己设置，clientId 总长度不得超过64个字符。
     */
    public static final String CLIENT_ID = "GID_TEAMACHINE_CONSOLE@@@console4local";

    /**
     * 超时时长
     */
    public static final int TIME_TO_WAIT = 5000;

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
    public static final String BIZ_CODE_PREPARE_TENANT = "prepare_tenant";
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
    public static final String TENANT_PARENT_TOPIC_POSTFIX = "-teamachine";
    public static final String TENANT_PARENT_P2P_TOPIC_POSTFIX = "-teamachine/p2p";

    /**
     * topic 分隔符
     */
    public static final String TOPIC_SEPERATOR = "/";
}
