package com.langtuo.teamachine.biz.aync.worker.rule;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.device.MachineDTO;
import com.langtuo.teamachine.api.model.rule.WarningRuleDTO;
import com.langtuo.teamachine.api.model.rule.WarningRuleDispatchDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import com.langtuo.teamachine.api.service.rule.WarningRuleMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.biz.util.SpringUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.TeaMachineResult.getListModel;
import static com.langtuo.teamachine.api.result.TeaMachineResult.getModel;

@Slf4j
public class WarningRuleDispatchWorker implements Runnable {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 预警规则编码
     */
    private String warningRuleCode;

    public WarningRuleDispatchWorker(JSONObject jsonPayload) {
        this.tenantCode = jsonPayload.getString(CommonConsts.JSON_KEY_TENANT_CODE);
        this.warningRuleCode = jsonPayload.getString(CommonConsts.JSON_KEY_WARNING_RULE_CODE);
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(warningRuleCode)) {
            throw new IllegalArgumentException("tenantCode or warningRuleCode is blank");
        }
    }

    @Override
    public void run() {
        JSONObject jsonDispatchCont = getDispatchCont();

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_DISPATCH_WARNING_RULE);
        jsonMsg.put(CommonConsts.JSON_KEY_WARNING_RULE, jsonDispatchCont);
        log.info("$$$$$ WarningRuleDispatchWorker sendMsg: " + jsonMsg.toJSONString());

        // 准备发送
        List<String> machineCodeList = getMachineCodeList();
        if (CollectionUtils.isEmpty(machineCodeList)) {
            log.info("machine code list is empty, stop worker");
        }

        MqttProducer mqttProducer = SpringUtils.getMqttProducer();
        machineCodeList.stream().forEach(machineCode -> {
            mqttProducer.sendP2PMsgByTenant(tenantCode, machineCode, jsonMsg.toJSONString());
        });
    }

    private WarningRuleMgtService getWarningRuleMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        WarningRuleMgtService warningRuleMgtService = appContext.getBean(WarningRuleMgtService.class);
        return warningRuleMgtService;
    }

    private ShopMgtService getShopMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ShopMgtService shopMgtService = appContext.getBean(ShopMgtService.class);
        return shopMgtService;
    }

    private MachineMgtService getMachineMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MachineMgtService machineMgtService = appContext.getBean(MachineMgtService.class);
        return machineMgtService;
    }

    private JSONObject getDispatchCont() {
        WarningRuleMgtService warningRuleMgtService = getWarningRuleMgtService();
        WarningRuleDTO warningRuleDTO = getModel(warningRuleMgtService.getByCode(tenantCode, warningRuleCode));
        if (warningRuleDTO == null) {
            log.info("warning rule error, stop worker");
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSON.toJSON(warningRuleDTO);
        return jsonObject;
    }

    private List<String> getMachineCodeList() {
        WarningRuleMgtService warningRuleMgtService = getWarningRuleMgtService();
        WarningRuleDispatchDTO drainRuleDispatchDTO = getModel(warningRuleMgtService.getDispatchByWarningRuleCode(tenantCode, warningRuleCode));
        if (drainRuleDispatchDTO == null) {
            log.info("warning rule dispatch is null");
            return null;
        }

        ShopMgtService shopMgtService = getShopMgtService();
        List<String> shopCodeList = drainRuleDispatchDTO.getShopGroupCodeList().stream()
                .map(shopGroupCode -> {
                    List<ShopDTO> shopList = getListModel(shopMgtService.listByShopGroupCode(
                            tenantCode, shopGroupCode));
                    if (shopList == null) {
                        return null;
                    }

                    return shopList.stream()
                            .map(shop -> shop.getShopCode())
                            .collect(Collectors.toList());
                })
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(shopCodeList)) {
            log.info("shop code list is empty");
            return null;
        }

        MachineMgtService machineMgtService = getMachineMgtService();
        List<String> machineCodeList = shopCodeList.stream()
                .map(shopCode -> {
                    List<MachineDTO> machineList = getListModel(machineMgtService.listByShopCode(
                            tenantCode, shopCode));
                    if (machineList == null) {
                        return null;
                    }

                    return machineList.stream()
                            .map(shop -> shop.getMachineCode())
                            .collect(Collectors.toList());
                })
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return machineCodeList;
    }
}
