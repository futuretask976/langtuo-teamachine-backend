package com.gx.sp3.demo.gtmf.manager;

import com.gx.sp3.demo.gtmf.metadata.ExtensionDefMetadata;
import com.gx.sp3.demo.gtmf.monitor.BizMonitorDataType;
import com.gx.sp3.demo.gtmf.monitor.CommonBizMonitor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author miya
 */
public class ExtensionTracerProxy implements MethodInterceptor {
    /**
     *
     */
    private static Map<Class<?>, Enhancer> proxyEnhancers = new ConcurrentHashMap();

    /**
     *
     */
    private Object traceOn;

    /**
     *
     */
    private Class<?> extensionDefClass;

    /**
     *
     */
    private String bizCode;

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        String methodName = "";
        long startTime = System.currentTimeMillis();
        try {
            List<String> requestStrList = new ArrayList<>();
            if (null != objects) {
                Arrays.stream(objects).forEach(object -> {
                            if ((null == object)) {
                                requestStrList.add("null");
                            } else {
                                if ((object instanceof BizMonitorDataType)) {
                                    requestStrList.add(((BizMonitorDataType) object).toMonitorString());
                                } else {
                                    requestStrList.add(object.toString());
                                }
                            }
                        }
                );
            } else {
                requestStrList.add("null");
            }
            methodName = traceOn.getClass().getSimpleName().split("\\$")[0] + "-" +
                    extensionDefClass.getSimpleName() + "." + method.getName();
            // CommonBizMonitor.enter(methodName, StringUtils.join(requestStrList, ","));
            result = method.invoke(traceOn, objects);
            if (null == result) {
                CommonBizMonitor.exit(methodName, "null", System.currentTimeMillis() - startTime);
            } else if (result instanceof BizMonitorDataType) {
                CommonBizMonitor.exit(methodName, ((BizMonitorDataType) result).toMonitorString(),
                        System.currentTimeMillis() - startTime);
            } else {
                CommonBizMonitor.exit(methodName, result.toString(),
                        System.currentTimeMillis() - startTime);
            }
        } catch (Throwable e) {
            CommonBizMonitor.exception(methodName, e, System.currentTimeMillis() - startTime);
            //throw e;
        }
        return result;
    }

    /**
     * @param traceOn
     * @param bizCode
     * @param extensionDefClass
     */
    public ExtensionTracerProxy(Object traceOn, String bizCode, Class<?> extensionDefClass) {
        this.traceOn = traceOn;
        this.extensionDefClass = extensionDefClass;
        this.bizCode = bizCode;
    }

    /**
     * @param impl
     * @param bizCode
     * @param extensionDefMetadata
     * @return
     */
    public static Object create(Object impl, String bizCode, ExtensionDefMetadata extensionDefMetadata) {
        //如果以后大量动态生成类，这里可能会有问题，导致metaspace满
        return proxyEnhancers.compute(impl.getClass(), (k, exists) -> {
            if (exists != null) {
                return exists;
            } else {
                Enhancer enhancer = new Enhancer();
                // 如果是接口直接实现接口，然后做代理
                if (extensionDefMetadata.getExtensionClass().isInterface()) {
                    enhancer.setInterfaces(new Class[]{extensionDefMetadata.getExtensionClass()});
                } else {
                    // 如果扩展点定义是一个类
                    enhancer.setSuperclass(extensionDefMetadata.getExtensionClass());
                }
                enhancer.setCallback(new ExtensionTracerProxy(impl, bizCode, extensionDefMetadata.getExtensionClass()));
                return enhancer;
            }
        }).create();
    }
}
