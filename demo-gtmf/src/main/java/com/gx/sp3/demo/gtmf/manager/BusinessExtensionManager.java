package com.gx.sp3.demo.gtmf.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gx.sp3.demo.gtmf.annotation.*;
import com.gx.sp3.demo.gtmf.config.diamond.Diamond;
import com.gx.sp3.demo.gtmf.config.diamond.ManagerListenerAdapter;
import com.gx.sp3.demo.gtmf.config.switcher.FilterSwitch;
import com.gx.sp3.demo.gtmf.config.switcher.SwitchManager;
import com.gx.sp3.demo.gtmf.extension.*;
import com.gx.sp3.demo.gtmf.gray.BusinessGrayStrategy;
import com.gx.sp3.demo.gtmf.metadata.BusinessDefMetadata;
import com.gx.sp3.demo.gtmf.metadata.BusinessMetadata;
import com.gx.sp3.demo.gtmf.metadata.ExtensionDefMetadata;
import com.gx.sp3.demo.gtmf.metadata.ExtensionMetadata;
import com.gx.sp3.demo.gtmf.monitor.CommonBizMonitor;
import com.gx.sp3.demo.gtmf.scan.ScanClassUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 入口类
 *
 * @author miya
 */
@Component
@Slf4j
@Getter
public class BusinessExtensionManager implements ExtensionExecutable, ApplicationListener<ContextRefreshedEvent> {
    /**
     *
     */
    private volatile Map<String/*clientInfo*/, Map<Class<?>/*ExtensionClass*/, List<ExtensionMetadata>>> extensionFastIndex;

    /**
     *
     */
    private volatile Map<String/*bizCode*/, BusinessMetadata> businessTemplateMap = new HashMap<>();

    /**
     *
     */
    private ApplicationContext applicationContext;

    /**
     *
     */
    private volatile boolean ready;

    /**
     *
     */
    private volatile String lastConfig;

    /**
     *
     */
    @Getter
    private static List<ExtensionDefMetadata> systemDefinedExtensionList = new ArrayList<>();

    /**
     *
     */
    @Getter
    private static Map<String/*extCode*/, ExtensionDefMetadata> systemDefinedExtensionCodeMap = new HashMap<>();

    /**
     *
     */
    @Getter
    private static Map<Class<?>, ExtensionDefMetadata> systemDefinedExtensionClassMap = new HashMap<>();

    /**
     *
     */
    @Getter
    private static List<BusinessDefMetadata> javaBusinessDefMetadataList = new ArrayList<>();

    /**
     *
     */
    @Getter
    private static Map<String/*bizCode*/, BusinessDefMetadata> javaBusinessDefMetadataMap = new HashMap<>();

    static {
        System.out.println("BusinessExtensionManager的static区块开始被执行");
        ScanClassUtils.scan("com.gx", (resource, metadataReader, metadataReaderFactory) -> {
            AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
            Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();

            // System.out.println("BusinessExtensionManager的static区块：annotationTypes -> " + annotationTypes);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            loadBusinessDef(annotationMetadata, annotationTypes, classLoader);
            loadExtensionDef(annotationMetadata, annotationTypes, classLoader);
            loadSwitchInfoDef(annotationMetadata, annotationTypes, classLoader);
        });

        list2Map(javaBusinessDefMetadataList, javaBusinessDefMetadataMap, BusinessDefMetadata::getBizCode, Function.identity(), (metadata, exists) ->
                new RuntimeException("业务" + metadata.getBizCode() + " 重复定义" + metadata + " 与 " + exists));
        list2Map(systemDefinedExtensionList, systemDefinedExtensionCodeMap, ExtensionDefMetadata::getExtensionCode, Function.identity(), (metadata, exists) ->
                new RuntimeException("扩展点" + metadata.getExtensionCode() + " 重复定义,  " + metadata + " 与 " + exists));
        list2Map(systemDefinedExtensionList, systemDefinedExtensionClassMap, ExtensionDefMetadata::getExtensionClass, Function.identity(), (metadata, exists) ->
                new RuntimeException("扩展点" + metadata.getExtensionCode() + " 接口定义重复 " + metadata + " 与 " + exists));

        // 当前忽略该方法
        // registerSwitch();
    }

    /**
     * 当前忽略该方法
     */
//    private static void registerSwitch() {
//        GraySwitchClassGenerator graySwitchClassGenerator = new GraySwitchClassGenerator();
//        for (BusinessDefMetadata businessDefMetadata : javaBusinessDefMetadataList) {
//            Class<?> switchDef = graySwitchClassGenerator.generateV2(businessDefMetadata);
//            //先初始化，然后在注册
//            GrayConfigSource grayConfigSource  = new GrayConfigSource(switchDef, true);
//            SwitchManager.init(switchDef);
//            businessDefMetadata.setBusinessGrayStrategy(new DefaultGrayStrategy(grayConfigSource));
//        }
//    }

    private static void loadExtensionDef(AnnotationMetadata annotationMetadata, Set<String> annotationTypes,
            ClassLoader classLoader) throws ClassNotFoundException {
        // 扩展点加载
        if (annotationTypes.contains(ExtensionFacade.class.getName())) {
            Class<?> extFacadeClass = classLoader.loadClass(annotationMetadata.getClassName());
            Method[] methods = extFacadeClass.getMethods();
            for (Method method : methods) {
                ExtensionDef extDef = method.getAnnotation(ExtensionDef.class);
                if (extDef != null && validateMethod(method)) {
                    Class<?> returnType = method.getReturnType();
                    if (returnType.toGenericString().contains("<")) {
                        throw new RuntimeException("不支持泛型扩展类型");
                    }

                    if (returnType.getName().startsWith("java.")) {
                        throw new RuntimeException("不支持的扩展类型" + returnType.getName());
                    }

                    ExtensionDefMetadata extensionDefMetadata = ExtensionDefMetadata.of(extFacadeClass, returnType,
                            method, extDef.code(), extDef.desc());
                    systemDefinedExtensionList.add(extensionDefMetadata);
                }
            }
        }
    }

    private static void loadBusinessDef(AnnotationMetadata annotationMetadata, Set<String> annotationTypes,
            ClassLoader classLoader) throws ClassNotFoundException {
        // 加载business定义
        if (annotationTypes.contains(BusinessDef.class.getName())) {
            Class<?> bizDefClass = classLoader.loadClass(annotationMetadata.getClassName());
            BusinessDef businessDef = bizDefClass.getAnnotation(BusinessDef.class);
            if (!ClassUtils.isAssignable(bizDefClass, BusinessTemplate.class)) {
                throw new RuntimeException(businessDef.code() + " 的定义类" + bizDefClass.getName() + "没有实现" + BusinessRecognize.class.getName());
            }

            BusinessDefMetadata businessDefMetadata = BusinessDefMetadata.of((Class<? extends BusinessTemplate>) bizDefClass, businessDef.code(), businessDef.desc());
            javaBusinessDefMetadataList.add(businessDefMetadata);
        }
    }

    private static void loadSwitchInfoDef(AnnotationMetadata annotationMetadata, Set<String> annotationTypes,
            ClassLoader classLoader) throws ClassNotFoundException {
        if (annotationTypes.contains(ProductSwitchInfo.class.getName())) {
            //加载各产品的开关定义
            Class<?> bizDefClass = classLoader.loadClass(annotationMetadata.getClassName());
            SwitchManager.init(bizDefClass);
        } else if (annotationTypes.contains(PlatformSwitchInfo.class.getName())) {
            //加载各平台的开关定义
            Class<?> bizDefClass = classLoader.loadClass(annotationMetadata.getClassName());
            SwitchManager.init(bizDefClass);
        }
    }

    private static <LT, MK, MV> void list2Map(List<LT> list, Map<MK, MV> map, Function<LT, MK> keyMapper,
            Function<LT, MV> valueMapper, BiFunction<LT, MV, RuntimeException> exceptionOnExist) {
        for (LT lt : list) {
            MK key = keyMapper.apply(lt);
            MV exits = map.get(key);
            if (exits != null) {
                throw exceptionOnExist.apply(lt, exits);
            }

            map.put(key, valueMapper.apply(lt));
        }
    }

    private static boolean validateMethod(Method method) {
        return !Modifier.isFinal(method.getModifiers()) && !Modifier.isStatic(method.getModifiers())
                && Modifier.isPublic(method.getModifiers());
    }

    public Set<String> getMatchBizCodes(BusinessRecognizeRequest req) {
        Stream<BusinessMetadata> stream = businessTemplateMap.values().stream();
        if (FilterSwitch.isOpen) {
            stream = stream.filter(d -> simpleGrayMatch(d, req));
        }
        return stream
                .filter(d -> d.getBusinessTemplate().match(req))
                .map(BusinessMetadata::getBizCode)
                .collect(Collectors.toSet());
    }

    @Override
    public <T, R> List<ExecuteExtResult<R>> executeAllGetNonNull(ExecuteExtRequest req, Class<T> extension,
            Function<T, R> func) {
        String client = req.getRequestSource().getClientTypeEnum().getCode();
        Set<String> bizCodes = req.getBizCodes();
        Map<Class<?>, List<ExtensionMetadata>> currentClientExtensions = extensionFastIndex.get(client);
        if (currentClientExtensions == null) {
            log.warn(String.format("%s 不是合法端|%s|%s", client, CommonBizMonitor.getKeyTraceId(), CommonBizMonitor.getKeyIdentityId()));
            return Collections.emptyList();
        }

        List<ExecuteExtResult<R>> results = new ArrayList<>();
        List<ExtensionMetadata> extensionMetadataList = currentClientExtensions.get(extension);
        if (extensionMetadataList == null) {
            log.warn(String.format("%s 不是合法扩展点|%s|%s", extension, CommonBizMonitor.getKeyTraceId(), CommonBizMonitor.getKeyIdentityId()));
            return Collections.emptyList();
        }
        for (ExtensionMetadata extensionMetadata : extensionMetadataList) {
            if (bizCodes.contains(extensionMetadata.getBizCode())) {
                if (!matchGrayStrategy(extensionMetadata, req)) {
                    continue;
                }

                R ret = func.apply((T) extensionMetadata.getImpl());
                if (ret != null) {
                    fillInBizCode(ret, extensionMetadata);
                    results.add(new ExecuteExtResult<>(extensionMetadata, ret));
                }
            }
        }
        return results;
    }

    @Override
    public <T, R> Optional<ExecuteExtResult<R>> executeUntilNonNull(ExecuteExtRequest req, Class<T> extension,
            Function<T, R> func) {
        String client = req.getRequestSource().getClientTypeEnum().getCode();
        Set<String> bizCodes = req.getBizCodes();
        Map<Class<?>, List<ExtensionMetadata>> currentChannelExtensions = extensionFastIndex.get(client);
        if (currentChannelExtensions == null) {
            log.warn(String.format("%s 不是合法端|%s|%s", client, CommonBizMonitor.getKeyTraceId(),
                    CommonBizMonitor.getKeyIdentityId()));
            return Optional.empty();
        }

        List<ExtensionMetadata> extensionMetadataList = currentChannelExtensions.get(extension);
        if (extensionMetadataList == null) {
            log.warn(String.format("%s 不是合法扩展点|%s|%s", extension, CommonBizMonitor.getKeyTraceId(),
                    CommonBizMonitor.getKeyIdentityId()));
            return Optional.empty();
        }

        for (ExtensionMetadata extensionMetadata : extensionMetadataList) {
            if (bizCodes.contains(extensionMetadata.getBizCode())) {
                if (!matchGrayStrategy(extensionMetadata, req)) {
                    //TODO
                    continue;
                }
                R ret = func.apply((T) extensionMetadata.getImpl());
                if (ret != null) {
                    fillInBizCode(ret, extensionMetadata);
                    ExecuteExtResult<R> testExecuteResult = new ExecuteExtResult(extensionMetadata, (R) ret);
                    return Optional.of(testExecuteResult);
                }
            }
        }
        return Optional.empty();
    }

    private boolean matchGrayStrategy(ExtensionMetadata extensionMetadata, ExecuteExtRequest req) {
        BusinessGrayStrategy businessGrayStrategy = extensionMetadata.getBusinessMetadata().getBusinessGrayStrategy();
        if (businessGrayStrategy == null) {
            return true;
        }

        return businessGrayStrategy.match(req);

    }

    private boolean simpleGrayMatch(BusinessMetadata businessMetadata, BusinessRecognizeRequest req) {
        BusinessGrayStrategy businessGrayStrategy = businessMetadata.getBusinessGrayStrategy();
        if (businessGrayStrategy == null) {
            return true;
        }
        return businessGrayStrategy.simpleMatch(req);
    }

    private <R> void fillInBizCode(R ret, ExtensionMetadata extensionMetadata) {
        if (null == ret) {
            return;
        }
        if (ret instanceof BizCodeLike) {
            ((BizCodeLike) ret).setBizCode(extensionMetadata.getBizCode());
        } else if (ret instanceof BizCodeSetLike) {
            Set<String> bizCodeSet = new HashSet<>();
            bizCodeSet.add(extensionMetadata.getBizCode());
            ((BizCodeSetLike) ret).setBizCodeSet(bizCodeSet);
        } else if (ret instanceof List && !((List) ret).isEmpty()) {
            ((List) ret).stream().filter(Objects::nonNull).forEach(
                    o -> {
                        if (o instanceof BizCodeLike) {
                            ((BizCodeLike) o).setBizCode(extensionMetadata.getBizCode());
                        } else if (o instanceof BizCodeSetLike) {
                            Set<String> bizCodeSet = new HashSet<>();
                            bizCodeSet.add(extensionMetadata.getBizCode());
                            ((BizCodeSetLike) o).setBizCodeSet(bizCodeSet);
                        }
                    }
            );
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        if (applicationContext.getParent() != null) {
            return;
        }

        try {
            String config = Diamond.getConfig("biz_priority_config_data", "DEFAULT_GROUP", 1000);
            this.applicationContext = applicationContext;
            reload(config);
            ready = true;
            Diamond.addListener("biz_priority_config_data", "DEFAULT_GROUP", new ManagerListenerAdapter() {
                @Override
                public void receiveConfigInfo(String s) {
                    reload(s);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("启动获取优扩展点优先级配置失败");
        }
    }

    public void reload(String config) {
        String lastConfig = this.lastConfig;
        if (lastConfig != null && lastConfig.equals(config)) {
            return;
        }

        Map<String, BusinessMetadata> businessMetadataMap = loadJavaBusinessTemplate(applicationContext.getBeansWithAnnotation(BusinessDef.class));
        this.lastConfig = config;

        JSONObject jsonObject = JSONObject.parseObject(config);

        Map<String/*clientInfo*/, Map<Class<?>, List<ExtensionMetadata>>> extensionFastIndex = new HashMap<>();
        Set<String> clientInfoSet = jsonObject.keySet();
        for (String clientInfo : clientInfoSet) {
            Map<Class<?>, List<ExtensionMetadata>> extConfig = parse(jsonObject.getJSONObject(clientInfo), businessMetadataMap);
            extensionFastIndex.put(clientInfo, extConfig);
        }

        this.businessTemplateMap = businessMetadataMap;
        this.extensionFastIndex = extensionFastIndex;
    }


    private Map<Class<?>, List<ExtensionMetadata>> parse(JSONObject extPriorityConfig, Map<String, BusinessMetadata> businessMetadataMap) {

        Map<Class<?>, List<ExtensionMetadata>> extensionClassListMap = new HashMap<>();
        for (String extCode : extPriorityConfig.keySet()) {
            ExtensionDefMetadata extensionDefMetadata = systemDefinedExtensionCodeMap.get(extCode);
            if (extensionDefMetadata == null) {
                //log.error("{} 未定义的扩展点", extCode);
                continue;
            }

            Class<?> extensionClass = extensionDefMetadata.getExtensionClass();
            List<ExtensionMetadata> implList = new ArrayList<>();

            JSONArray bizImplPriorityArray = extPriorityConfig.getJSONArray(extCode);
            if (bizImplPriorityArray != null) {
                for (Object o : bizImplPriorityArray) {
                    String bizCode = o.toString();
                    BusinessMetadata businessMetadata = businessMetadataMap.get(bizCode);
                    if (businessMetadata == null) {
                        //log.error("{} 不是合法的业务身份", bizCode);
                        continue;
                    }

                    Object extensionImpl = null;
                    try {
                        extensionImpl = extensionDefMetadata.getExtensionGetterInvoker().invoke(businessMetadata.getBusinessTemplate());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.warn("获取扩展点实现异常", e);
                    }
                    if (extensionImpl != null) {
                        Object traceableExtensionImpl = ExtensionTracerProxy.create(extensionImpl, bizCode, extensionDefMetadata);
                        ExtensionMetadata extensionMetadata = extensionDefMetadata.toRuntimeExtensionMetadata(businessMetadata, traceableExtensionImpl, extensionImpl);
                        businessMetadata.getExtensionMetaData().add(extensionMetadata);
                        implList.add(extensionMetadata);
                    }
                }
            }

            extensionClassListMap.put(extensionClass, implList);
        }

        return extensionClassListMap;
    }

    private Map<String/*bizCode*/, BusinessMetadata> loadJavaBusinessTemplate(Map<String, Object> beansWithAnnotation) {
        Map<String/*bizCode*/, BusinessMetadata> businessMetadataMap = new HashMap<>();
        beansWithAnnotation.forEach((beanName, bean) -> {
            Class<?> clazz = bean.getClass();
            if (!(bean instanceof BusinessTemplate)) {
                throw new RuntimeException("business def must implement BusinessTemplate");
            }
            BusinessDef bizDef = clazz.getAnnotation(BusinessDef.class);
            BusinessDefMetadata businessDefMetadata = javaBusinessDefMetadataMap.get(bizDef.code());
            businessMetadataMap.put(bizDef.code(), businessDefMetadata.toRuntimeBusinessMetadata((BusinessTemplate) bean));
        });

        return businessMetadataMap;
    }

    public static void trigger4Test() {
        System.out.println("BusinessExtensionManager@trigger4Test被执行");
    }
}