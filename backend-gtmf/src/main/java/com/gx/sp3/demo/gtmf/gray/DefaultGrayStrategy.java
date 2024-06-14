package com.gx.sp3.demo.gtmf.gray;//package com.gx.gtmf.framework.gray;
//
//import com.google.common.primitives.Ints;
//import com.gx.gtmf.framework.core.BusinessRecognizeRequest;
//import com.gx.gtmf.framework.param.SourceInfo;
//import com.gx.gtmf.framework.param.UserAgent;
//import com.gx.gtmf.framework.core.ExecuteRequest;
//import org.apache.commons.lang3.StringUtils;
//
///**
// * @author miya
// */
//public class DefaultGrayStrategy implements BusinessGrayStrategy {
//    private GrayConfigSource config;
//
//    public DefaultGrayStrategy(GrayConfigSource config) {
//        this.config = config;
//    }
//
//    boolean matchUser(long userId) {
//
//        if (config.getUserIdWhiteList().contains(userId)) {
//            return true;
//        }
//
//        long last4 = userId % 10000;
//        long middle2 = last4 / 100;
//        return middle2 < config.getUserIdGrayRate();
//    }
//
//    @Override
//    public boolean match(ExecuteRequest req) {
//        if (!matchUser(req.getUserId())) {
//            return false;
//        }
//
//        SourceInfo sourceInfo = req.getSourceInfo();
//        if (sourceInfo == null) {
//            return false;
//        }
//
//        UserAgent userAgent = sourceInfo.getUserAgent();
//
//        ClientVersionConfig clientVersionConfig = config.getClientVersionConfig(userAgent.getOperatingSystem(),
//                sourceInfo.getClientTypeEnum());
//
//
//        // 获取client配置
//        if (clientVersionConfig != null) {
//            // 如果当前client不生效false
//            if (!clientVersionConfig.isEnable()) {
//                return false;
//            }
//
//            String appVersion = sourceInfo.getAppVersion();
//            // appVersion不空，并且不在黑名单，并且大于版本号return true
//            if (appVersion != null && !clientVersionConfig.getVersionNotSupport().contains(sourceInfo.getAppVersion())) {
//                if (versionGreaterEqualThan(sourceInfo.getAppVersion(), clientVersionConfig.getVersionGreaterEqualThan())) {
//                    return true;
//                }
//            }
//
//            // 啥？有端信息，没有version，或者在黑名单或者小于最低版本号,返回false
//            return false;
//        } else {
//            //取不到端配置,非手淘，猫客
//            if (config.getEnableOtherClients().contains("__all__")) {
//                return true;
//            }
//
//            return config.getEnableOtherClients().contains(sourceInfo.getClientTypeEnum().getCode());
//        }
//    }
//
//    @Override
//    public boolean simpleMatch(BusinessRecognizeRequest req) {
//        return matchUser(req.getUserId());
//    }
//
//    private boolean versionGreaterEqualThan(String appVersion, String minVersion) {
//        if (StringUtils.isEmpty(minVersion)) {
//            return false;
//        }
//
//        int[] version = versions(appVersion);
//        if (version == null) {
//            return false;
//        }
//
//        int[] min = versions(minVersion);
//
//
//        return Ints.lexicographicalComparator()
//                .compare(version, min) >= 0;
//    }
//
//    private int[] versions(String version) {
//        String[] vs = version.split("\\.");
//        int[] v = new int[vs.length];
//        for (int i = 0; i < v.length; i++) {
//            try {
//                v[i] = Integer.parseInt(vs[i]);
//            } catch (Exception e) {
//                return null;
//            }
//        }
//
//        return v;
//    }
//}
