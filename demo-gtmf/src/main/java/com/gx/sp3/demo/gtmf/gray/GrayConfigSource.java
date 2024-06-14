package com.gx.sp3.demo.gtmf.gray;//package com.gx.gtmf.framework.gray;
//
//import com.gx.gtmf.framework.consts.ClientTypeEnum;
//import com.gx.gtmf.framework.consts.OperatingSystemEnum;
//import lombok.extern.slf4j.Slf4j;
//
//import javax.annotation.Nonnull;
//import java.lang.reflect.Field;
//import java.util.*;
//
///**
// * @author miya
// */
//@Slf4j
//public class GrayConfigSource {
//    /**
//     *
//     */
//    private Class<?> switchClass;
//
//    /**
//     *
//     */
//    private Field userIdGrayRate;
//
//    /**
//     *
//     */
//    private Field userIdWhiteList;
//
//    /**
//     *
//     */
//    private Map<OperatingSystemEnum, Map<ClientTypeEnum, Field>> platformClientVersionConfig = new HashMap() {{
//        put(OperatingSystemEnum.IOS, new HashMap<>());
//        put(OperatingSystemEnum.ANDROID, new HashMap<>());
//    }};
//
//    /**
//     *
//     */
//    private Field enableOtherClients;
//
//    /**
//     * 由于 <<clinit>> 中没有按预期生成static 字段的初始化字节码，所以这里先初始化一下，我知道这有点蛋疼，临时先解决下
//     * @param switchClass
//     * @param initField
//     */
//    public GrayConfigSource(Class<?> switchClass, boolean initField) {
//        try {
//            this.switchClass = switchClass;
//            userIdGrayRate = switchClass.getField(GraySwitchClassGenerator.grayFieldName);
//            userIdGrayRate.setAccessible(true);
//            if (initField) {
//                userIdGrayRate.set(null, 0);
//            }
//            userIdWhiteList = switchClass.getField("userIdWhiteList");
//            userIdWhiteList.setAccessible(true);
//            if (initField) {
//                userIdWhiteList.set(null, new HashSet<Long>());
//            }
//            registerClientConfig(switchClass, initField, "taobaoClientAndroidConfig", OperatingSystemEnum.ANDROID, ClientTypeEnum.TAOBAO_CLIENT);
//            registerClientConfig(switchClass, initField, "taobaoClientTaoIOSConfig", OperatingSystemEnum.IOS, ClientTypeEnum.TAOBAO_CLIENT);
//            registerClientConfig(switchClass, initField, "tmallClientAndroidConfig", OperatingSystemEnum.ANDROID, ClientTypeEnum.TMALL_CLIENT);
//            registerClientConfig(switchClass, initField, "tmallClientIOSConfig", OperatingSystemEnum.IOS, ClientTypeEnum.TMALL_CLIENT);
//            enableOtherClients = switchClass.getField("enableOtherClients");
//            enableOtherClients.setAccessible(true);
//            if (initField) {
//                enableOtherClients.set(null, new HashSet<String>());
//            }
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     *
//     * @param os
//     * @param client
//     * @return
//     */
//    public ClientVersionConfig getClientVersionConfig(OperatingSystemEnum os, ClientTypeEnum client) {
//        Field configField = Optional.ofNullable(platformClientVersionConfig.get(os))
//                .orElseGet(Collections::emptyMap)
//                .getOrDefault(client, null);
//        if (configField == null) {
//            return null;
//        }
//
//        try {
//            return (ClientVersionConfig) configField.get(null);
//        } catch (IllegalAccessException e) {
//            log.error(e.getMessage(), e);
//        }
//        return null;
//    }
//
//    /**
//     *
//     * @return
//     */
//    @Nonnull
//    public Set<String> getEnableOtherClients() {
//        try {
//            return (Set<String>) enableOtherClients.get(null);
//        } catch (IllegalAccessException e) {
//            log.error("{} enableOtherClients", switchClass, e);
//        }
//
//        return Collections.emptySet();
//    }
//
//    /**
//     *
//     * @return
//     */
//    public Set<Long> getUserIdWhiteList() {
//        try {
//            return (Set<Long>) userIdWhiteList.get(null);
//        } catch (IllegalAccessException e) {
//            log.error("{} userIdWhiteList", switchClass, e);
//        }
//
//        return Collections.emptySet();
//    }
//
//    /**
//     *
//     * @return
//     */
//    public long getUserIdGrayRate() {
//        try {
//            return userIdGrayRate.getLong(null);
//        } catch (IllegalAccessException e) {
//            log.error("{} userIdGrayRate", switchClass, e);
//            return 0;
//        }
//    }
//
//    private void registerClientConfig(Class<?> switchClass, boolean initField, String filedName, OperatingSystemEnum os, ClientTypeEnum clientType) throws NoSuchFieldException, IllegalAccessException {
//        Field field = switchClass.getField(filedName);
//        field.setAccessible(true);
//        if (initField) {
//            field.set(null, new ClientVersionConfig());
//        }
//        platformClientVersionConfig.get(os)
//                .put(clientType, field);
//    }
//}
