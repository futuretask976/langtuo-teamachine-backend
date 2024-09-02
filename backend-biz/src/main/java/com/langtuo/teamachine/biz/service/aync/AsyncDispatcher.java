package com.langtuo.teamachine.biz.service.aync;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.langtuo.teamachine.biz.service.aync.threadpool.AsyncExeService;
import com.langtuo.teamachine.biz.service.aync.worker.device.MachineDispatchWorker;
import com.langtuo.teamachine.biz.service.aync.worker.device.ModelDispatchWorker;
import com.langtuo.teamachine.biz.service.aync.worker.drink.AccuracyTplDispatchWorker;
import com.langtuo.teamachine.biz.service.aync.worker.menu.MenuDispatch4InitWorker;
import com.langtuo.teamachine.biz.service.aync.worker.menu.MenuDispatchWorker;
import com.langtuo.teamachine.biz.service.aync.worker.rule.CleanRuleDispatchWorker;
import com.langtuo.teamachine.biz.service.aync.worker.rule.DrainRuleDispatchWorker;
import com.langtuo.teamachine.biz.service.aync.worker.rule.WarningRuleDispatchWorker;
import com.langtuo.teamachine.biz.service.aync.worker.user.TenantPostWorker;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.mqtt.consume.MqttConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;
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

        String bizCode = jsonPayload.getString(BizConsts.RECEIVE_KEY_BIZ_CODE);
        Function<JSONObject, Runnable> function = workerMap.get(bizCode);
        if (function == null) {
            log.info("AsyncDispatcher|dispatch|noMatch|" + bizCode);
        }
        AsyncExeService.getExecutorService().submit(function.apply(jsonPayload));
    }

    private void initWorkerMap() {
        workerMap = Maps.newHashMap();
        // device 相关
        workerMap.put(BizConsts.BIZ_CODE_PREPARE_MODEL, jsonPayload -> new ModelDispatchWorker(jsonPayload));
        workerMap.put(BizConsts.BIZ_CODE_PREPARE_MACHINE, jsonPayload -> new MachineDispatchWorker(jsonPayload));
        // drink 相关
        workerMap.put(BizConsts.BIZ_CODE_PREPARE_ACCURACY_TPL, jsonPayload -> new AccuracyTplDispatchWorker(jsonPayload));
        // menu 相关
        workerMap.put(BizConsts.BIZ_CODE_PREPARE_MENU, jsonPayload -> new MenuDispatchWorker(jsonPayload));
        workerMap.put(BizConsts.BIZ_CODE_PREPARE_MENU_INIT_LIST, jsonPayload -> new MenuDispatch4InitWorker(jsonPayload));
        // rule 相关
        workerMap.put(BizConsts.BIZ_CODE_PREPARE_DRAIN_RULE, jsonPayload -> new DrainRuleDispatchWorker(jsonPayload));
        workerMap.put(BizConsts.BIZ_CODE_PREPARE_CLEAN_RULE, jsonPayload -> new CleanRuleDispatchWorker(jsonPayload));
        workerMap.put(BizConsts.BIZ_CODE_PREPARE_WARNING_RULE, jsonPayload -> new WarningRuleDispatchWorker(jsonPayload));
        // user 相关
        workerMap.put(BizConsts.BIZ_CODE_PREPARE_TENANT, jsonPayload -> new TenantPostWorker(jsonPayload));
    }
}
