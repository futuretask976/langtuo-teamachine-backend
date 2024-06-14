package com.gx.sp3.demo.gtmf.monitor;

import com.gx.sp3.demo.gtmf.config.switcher.CommonBizMonitorSwitch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author miya
 */
@Slf4j
public class CommonBizMonitor {
    /**
     *
     */
    private static final Logger bizMonitorLogger = LoggerFactory.getLogger("biz-monitor-logger");

    /**
     *
     */
    private static final Logger actionMonitorLogger = LoggerFactory.getLogger("action-monitor-logger");

    /**
     *
     */
    private static final Logger bizExceptionLogger = LoggerFactory.getLogger("biz-exception-logger");

    /**
     *
     */
    private static final Logger illegalAccessLoggger = LoggerFactory.getLogger("illegal-access-logger");

    /**
     *
     */
    private static final Logger apiLogger = LoggerFactory.getLogger("api-logger");

    /**
     *
     */
    private static final Logger metaqConsumeLogger = LoggerFactory.getLogger("metaq-consume-logger");

    /**
     *
     */
    private static ThreadLocal<List<InvokeMetaData>> invokeCache = ThreadLocal.withInitial(ArrayList::new);

    /**
     *
     */
    private static ThreadLocal<Map<String, Object>> monitorCache = ThreadLocal.withInitial(HashMap::new);

    /**
     *
     */
    private final static String KEY_TRACE_ID = "traceId";

    /**
     *
     */
    private final static String KEY_APP_NAME = "appName";

    /**
     *
     */
    private final static String KEY_SERVER_IP = "serverIp";

    /**
     *
     */
    private final static String KEY_SERVICE_NAME = "serviceName";

    /**
     *
     */
    private final static String KEY_SERVICE_REQUEST = "serviceRequest";

    /**
     *
     */
    private final static String KEY_SERVICE_RESPONSE = "serviceResponse";

    /**
     *
     */
    private final static String KEY_SERVICE_EXCEPTION = "serviceException";

    /**
     * 业务识别的主键
     */
    private final static String KEY_IDENTITY_ID = "identityId";

    /**
     * 流量来源
     */
    private final static String KEY_REQUEST_SOURCE = "reqSource";

    private final static String LOG_ITEM_SEP = "|";

    /**
     * @param traceId
     * @param appName
     * @param serverIp
     * @param serviceName
     * @param requestString
     * @param identityId
     * @param reqSource
     */
    public static void enterServiceImpl(String traceId, String appName, String serverIp, String serviceName,
                                        String requestString, String identityId, String reqSource) {
        monitorCache.get().put(KEY_TRACE_ID, traceId);
        monitorCache.get().put(KEY_APP_NAME, appName);
        monitorCache.get().put(KEY_SERVER_IP, serverIp);
        monitorCache.get().put(KEY_SERVICE_NAME, serviceName);
        monitorCache.get().put(KEY_SERVICE_REQUEST, requestString);
        monitorCache.get().put(KEY_IDENTITY_ID, identityId);
        monitorCache.get().put(KEY_REQUEST_SOURCE, reqSource);
    }

    /**
     * @param responseString
     */
    public static void exitServiceImpl(String responseString) {
        monitorCache.get().put(KEY_SERVICE_RESPONSE, responseString);
        logMonitorCache();
        monitorCache.get().clear();
        invokeCache.get().clear();
    }

    /**
     * @param throwable
     */
    public static void exitServiceImplWithException(Throwable throwable) {
        if (CommonBizMonitorSwitch.finalOpen) {
            monitorCache.get().put(KEY_SERVICE_EXCEPTION, throwable);
            bizExceptionLogger.error(getKeyTraceId()
                            + "|" + getKeyIdentityId()
                            + "|" + getKeyServiceName()
                            + "|" + getKeyRequestSource()
                    , throwable);
            logMonitorCache();
            monitorCache.get().clear();
            invokeCache.get().clear();
        }
    }

    /**
     * @param throwable
     * @param errorCode
     * @param errorParam
     */
    public static void exitServiceImplWithException(Throwable throwable, String errorCode, String errorParam) {
        if (CommonBizMonitorSwitch.finalOpen) {
            monitorCache.get().put(KEY_SERVICE_EXCEPTION, throwable);
            bizExceptionLogger.error(getKeyTraceId()
                            + "|" + getKeyIdentityId()
                            + "|" + getKeyServiceName()
                            + "|" + getKeyRequestSource()
                            + "|" + errorCode
                            + "|" + errorParam,
                    throwable);
            logMonitorCache();
        }
        monitorCache.get().clear();
        invokeCache.get().clear();
    }

    public static void remove() {
        try {
            monitorCache.remove();
            invokeCache.remove();
        } catch (Throwable t) {
            log.error("remove|ThreadLocal|error", t);
        }
    }

    public static String getKeyTraceId() {
        if (null == monitorCache.get().get(KEY_TRACE_ID)) {
            monitorCache.get().put(KEY_TRACE_ID, EagleEye.getTraceId());
        }
        return (String) monitorCache.get().get(KEY_TRACE_ID);
    }

    public static String getKeyIdentityId() {
        return (String) monitorCache.get().getOrDefault(KEY_IDENTITY_ID, "null");
    }

    public static String getKeyServiceName() {
        return (String) monitorCache.get().getOrDefault(KEY_SERVICE_NAME, "null");
    }

    public static String getKeyRequestSource() {
        return (String) monitorCache.get().getOrDefault(KEY_REQUEST_SOURCE, "null");
    }

    public static void enter(String methodName, String requestString) {
        invokeCache.get().add(InvokeMetaData.enter(methodName, requestString));
    }

    public static void exit(String methodName, String responseString, long costTime) {
        invokeCache.get().add(InvokeMetaData.exit(methodName, responseString, costTime));
    }

    public static void exception(String methodName, Throwable throwable, long costTime) {
        if (!CommonBizMonitorSwitch.errorOpen) {
            return;
        }
        invokeCache.get().add(InvokeMetaData.exception(methodName, throwable, costTime));
        bizExceptionLogger.error(getKeyTraceId()
                        + "|" + getKeyIdentityId()
                        + "|" + methodName
                        + "|" + getKeyRequestSource(),
                throwable);
    }

    /**
     * 记录一次调用的上下文信息
     */
    static class InvokeMetaData {
        String methodName;
        String request;
        String response;
        Throwable throwable;
        Long costTime;
        DataType dataType;

        enum DataType {
            ENTER,
            RETURN,
            EXCEPTION
        }

        static InvokeMetaData enter(String methodName, String request) {
            InvokeMetaData invokeMetaData = new InvokeMetaData();
            invokeMetaData.methodName = methodName;
            invokeMetaData.request = request;
            invokeMetaData.dataType = DataType.ENTER;
            return invokeMetaData;
        }

        static InvokeMetaData exit(String methodName, String response, Long costTime) {
            InvokeMetaData invokeMetaData = new InvokeMetaData();
            invokeMetaData.methodName = methodName;
            invokeMetaData.response = response;
            invokeMetaData.costTime = costTime;
            invokeMetaData.dataType = DataType.RETURN;
            return invokeMetaData;
        }

        static InvokeMetaData exception(String methodName, Throwable throwable, Long costTime) {
            InvokeMetaData invokeMetaData = new InvokeMetaData();
            invokeMetaData.methodName = methodName;
            invokeMetaData.throwable = throwable;
            invokeMetaData.costTime = costTime;
            invokeMetaData.dataType = DataType.EXCEPTION;
            return invokeMetaData;
        }

        String toMonitorString() {
            if (DataType.ENTER == dataType) {
                return DataType.ENTER + LOG_ITEM_SEP + methodName + LOG_ITEM_SEP + request;
            } else if (DataType.RETURN == dataType) {
                return DataType.RETURN + LOG_ITEM_SEP + methodName + LOG_ITEM_SEP +
                        response + LOG_ITEM_SEP + costTime + "ms";
            } else if (DataType.EXCEPTION == dataType) {
                return DataType.EXCEPTION + LOG_ITEM_SEP + methodName + LOG_ITEM_SEP +
                        throwable.getMessage() + LOG_ITEM_SEP + costTime + "ms";
            }
            return "";
        }

    }

    private static void logMonitorCache() {
        // RPC出入日志 monitorCache
        if (!CommonBizMonitorSwitch.warnOpen) {
            return;
        }
        bizMonitorLogger.warn(getKeyTraceId() + "|" + getKeyIdentityId() + "|" + getKeyRequestSource(),
                monitorCache.get().get(KEY_SERVICE_RESPONSE),
                StringUtils.join(monitorCache.get().entrySet().stream()
                        .filter(entry -> !KEY_TRACE_ID.equals(entry.getKey()) && !KEY_IDENTITY_ID.equals(entry.getKey()))
                        .map(entry -> entry.getKey() + ":" + entry.getValue())
                        .collect(Collectors.toList()), LOG_ITEM_SEP));
        // 内部调用链路日志 invokeCache
        if (CommonBizMonitorSwitch.isOpen || CommonBizMonitorSwitch.whitelist.contains("," + getKeyIdentityId() + ",")) {
            invokeCache.get().forEach(
                    invokeMetaData -> bizMonitorLogger.warn(getKeyTraceId() + "|" + getKeyIdentityId() + "|" + getKeyRequestSource() + "|" + invokeMetaData.toMonitorString())
            );
        }
    }

    public static void payStateError(String s, Object... objects) {
        bizMonitorLogger.warn(s, objects);
    }

    public static void logIllegalAccess(Long legalUserId, Long accessOrderId, String role) {
        if (!CommonBizMonitorSwitch.warnOpen) {
            return;
        }
        illegalAccessLoggger.warn(getKeyTraceId()
                + "|" + getKeyIdentityId()
                + "|" + role
                + "|" + getKeyRequestSource()
                + "|" + legalUserId
                + "|" + accessOrderId);
    }

    public static void logCommonError(String methodName, Throwable throwable) {
        if (!CommonBizMonitorSwitch.errorOpen) {
            return;
        }
        bizExceptionLogger.error(getKeyTraceId()
                        + "|" + getKeyIdentityId()
                        + "|" + methodName
                        + "|" + getKeyRequestSource(),
                throwable);
    }

    public static void logCommonApi(String methodName, String request, String response) {
        if (!CommonBizMonitorSwitch.infoOpen) {
            return;
        }
        apiLogger.info(getKeyTraceId()
                + "|" + getKeyIdentityId()
                + "|" + methodName
                + "|" + getKeyRequestSource()
                + "|" + request
                + "|" + response);
    }

    public static void logCommonAction(String actionName, String actionResult, String... params) {
        if (!CommonBizMonitorSwitch.infoOpen) {
            return;
        }
        String output = getKeyTraceId() + "|" + getKeyIdentityId() + "|" + actionName + "|" + actionResult;
        for (String param : params) {
            output = output + "|" + param;
        }
        output = output + "|";
        actionMonitorLogger.info(output);
    }

    public static void logMetaqConsume(String topic, String msgId, String tags, String data) {
        if (!CommonBizMonitorSwitch.warnOpen) {
            return;
        }
        metaqConsumeLogger.warn(getKeyTraceId() + "|" + topic + "|" + msgId + "|" + tags + "|" + data);
    }
}
