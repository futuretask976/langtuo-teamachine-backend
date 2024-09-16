package com.langtuo.teamachine.mqtt.util;

import com.alibaba.mqtt.server.config.ChannelConfig;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.*;

/**
 * @author miya
 */
@Slf4j
public class MqttUtils {
    /**
     * @param text 要签名的文本
     * @param secretKey 阿里云MQ secretKey
     * @return 加密后的字符串
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public static String macSignature(String text,
            String secretKey) throws InvalidKeyException, NoSuchAlgorithmException {
        Charset charset = Charset.forName("UTF-8");
        String algorithm = "HmacSHA1";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(secretKey.getBytes(charset), algorithm));
        byte[] bytes = mac.doFinal(text.getBytes(charset));
        return new String(Base64.encodeBase64(bytes), charset);
    }

    public static ChannelConfig getChannelConfig() {
        // 此处参数内容为示意。接入点地址，购买实例，且配置完成后即可获取，接入点地址必须填写分配的域名，
        // 不得使用 IP 地址直接连接，否则可能会导致客户端异常
        String domain = MqttConsts.ENDPOINT;

        // 使用的协议和端口必须匹配，为5672
        int port = 5672;

        // 此处参数内容为示意。MQTT 实例 ID，购买后控制台获取
        String instanceId = MqttConsts.INSTANCE_ID;

        // 此处参数内容为示意。账号 accesskey，从账号系统控制台获取
        String accessKey = MqttConsts.ACCESS_KEY; // "accessKey";

        // 此处参数内容为示意。账号 secretKey，从账号系统控制台获取，仅在Signature鉴权模式下需要设置
        String secretKey = MqttConsts.ACCESS_KEY_SECRET; // "secretKey";

        ChannelConfig channelConfig = new ChannelConfig();
        channelConfig.setDomain(domain);
        channelConfig.setPort(port);
        channelConfig.setInstanceId(instanceId);
        channelConfig.setAccessKey(accessKey);
        channelConfig.setSecretKey(secretKey);
        return channelConfig;
    }
}
