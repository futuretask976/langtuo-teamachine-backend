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
    public static final String ACCESS_KEY = "LTAI5tNqF1bav1mtzdUSGwkQ"; // System.getenv("MQTT_AK_ENV");

    /**
     * 账号 secretKey，从账号系统控制台获取，仅在Signature鉴权模式下需要设置
     */
    public static final String ACCESS_KEY_SECRET = "njNRe0aL6MNrWXI4TUYYWRolx1uYOq"; // System.getenv("MQTT_SK_ENV");

    public static final String MANAGER_ENDPOINT = "onsmqtt.cn-beijing.aliyuncs.com";

    /**
     * 超时时长
     */
    public static final int TIME_TO_WAIT = 5000;

    /**
     * topic 相关常量
     */
    public static final String GROUP_ID = "GID_TEAMACHINE";
    public static final String CONSOLE_PARENT_TOPIC = "teamachine";
    public static final String TENANT_PARENT_TOPIC_POSTFIX = "-teamachine";
    public static final String TENANT_PARENT_P2P_TOPIC_POSTFIX = "-teamachine/p2p/" + GROUP_ID + "@@@";

    /**
     * topic 分隔符
     */
    public static final String TOPIC_SEPERATOR = "/";

    /**
     * 收到的消息中的 key 关键字
     */
    public static final String RECEIVE_KEY_BIZ_CODE = "bizCode";
    public static final String RECEIVE_KEY_LIST = "list";


    /**
     * 发送的消息中的 key 关键字
     */
    public static final String SEND_KEY_BIZ_CODE = "bizCode";

    /**
     * 接受设备端消息用的 bizCode
     */
    public static final String BIZ_CODE_INVALID_ACT_RECORD = "invalidActRecord";
    public static final String BIZ_CODE_SUPPLY_ACT_RECORD = "supplyActRecord";
    public static final String BIZ_CODE_DRAIN_ACT_RECORD = "drainActRecord";
    public static final String BIZ_CODE_CLEAN_ACT_RECORD = "cleanActRecord";
    public static final String BIZ_CODE_ORDER_ACT_RECORD = "orderActRecord";
}
