package com.langtuo.teamachine.biz.aync;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.langtuo.teamachine.biz.aync.threadpool.AsyncExeService;
import com.langtuo.teamachine.biz.aync.worker.device.AndroidAppDispatchWorker;
import com.langtuo.teamachine.biz.aync.worker.device.MachineDispatchWorker;
import com.langtuo.teamachine.biz.aync.worker.device.ModelDispatchWorker;
import com.langtuo.teamachine.biz.aync.worker.drink.AccuracyTplDispatchWorker;
import com.langtuo.teamachine.biz.aync.worker.menu.MenuDispatch4InitWorker;
import com.langtuo.teamachine.biz.aync.worker.menu.MenuDispatchWorker;
import com.langtuo.teamachine.biz.aync.worker.rule.CleanRuleDispatchWorker;
import com.langtuo.teamachine.biz.aync.worker.rule.DrainRuleDispatchWorker;
import com.langtuo.teamachine.biz.aync.worker.rule.WarningRuleDispatchWorker;
import com.langtuo.teamachine.biz.aync.worker.user.TenantPostWorker;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.mqtt.consume.MqttConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class AsyncDispatcher implements InitializingBean {
    /**
     * 存放需要异步执行的 woker
     */
    private Map<String, Function<JSONObject, Runnable>> workerMap;

    @Override
    public void afterPropertiesSet() {
        if (workerMap == null) {
            synchronized (MqttConsumer.class) {
                if (workerMap == null) {
                    initWorkerMap();
                }
            }
        }
    }

    public void dispatch(JSONObject jsonPayload) {
        if (jsonPayload == null) {
            return;
        }
        if (workerMap == null) {
            initWorkerMap();
        }

        String bizCode = jsonPayload.getString(CommonConsts.JSON_KEY_BIZ_CODE);
        Function<JSONObject, Runnable> function = workerMap.get(bizCode);
        if (function == null) {
            log.info("AsyncDispatcher|dispatch|noMatch|" + bizCode);
        }
        AsyncExeService.getExecutorService().submit(function.apply(jsonPayload));
    }

    private void initWorkerMap() {
        workerMap = Maps.newHashMap();
        // device 相关
        workerMap.put(CommonConsts.BIZ_CODE_MODEL_UPDATED, jsonPayload -> new ModelDispatchWorker(jsonPayload));
        workerMap.put(CommonConsts.BIZ_CODE_MACHINE_UPDATED, jsonPayload -> new MachineDispatchWorker(jsonPayload));
        workerMap.put(CommonConsts.BIZ_CODE_ANDROID_APP_DISPATCHED, jsonPayload -> new AndroidAppDispatchWorker(jsonPayload));
        // drink 相关
        workerMap.put(CommonConsts.BIZ_CODE_ACCURACY_TPL_UPDATED, jsonPayload -> new AccuracyTplDispatchWorker(jsonPayload));
        // menu 相关
        workerMap.put(CommonConsts.BIZ_CODE_MENU_DISPATCH_REQUESTED, jsonPayload -> new MenuDispatchWorker(jsonPayload));
        workerMap.put(CommonConsts.BIZ_CODE_MENU_LIST_REQUESTED, jsonPayload -> new MenuDispatch4InitWorker(jsonPayload));
        // rule 相关
        workerMap.put(CommonConsts.BIZ_CODE_DRAIN_RULE_DISPATCH_REQUESTED, jsonPayload -> new DrainRuleDispatchWorker(jsonPayload));
        workerMap.put(CommonConsts.BIZ_CODE_CLEAN_RULE_DISPATCH_REQUESTED, jsonPayload -> new CleanRuleDispatchWorker(jsonPayload));
        workerMap.put(CommonConsts.BIZ_CODE_WARNING_RULE_DISPATCH_REQUESTED, jsonPayload -> new WarningRuleDispatchWorker(jsonPayload));
        // user 相关
        workerMap.put(CommonConsts.BIZ_CODE_TENANT_UPDATED, jsonPayload -> new TenantPostWorker(jsonPayload));
    }
}
