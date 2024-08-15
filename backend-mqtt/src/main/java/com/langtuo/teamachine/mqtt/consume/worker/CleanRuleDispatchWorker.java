package com.langtuo.teamachine.mqtt.consume.worker;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.device.MachineDTO;
import com.langtuo.teamachine.api.model.rule.CleanRuleDTO;
import com.langtuo.teamachine.api.model.rule.CleanRuleDispatchDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import com.langtuo.teamachine.api.service.rule.CleanRuleMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.mqtt.MqttService;
import com.langtuo.teamachine.mqtt.config.MqttConfig;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.LangTuoResult.getListModel;
import static com.langtuo.teamachine.api.result.LangTuoResult.getModel;

@Slf4j
public class CleanRuleDispatchWorker implements Runnable {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 清洁规则编码
     */
    private String cleanRuleCode;

    public CleanRuleDispatchWorker(JSONObject jsonPayload) {
        this.tenantCode = jsonPayload.getString(MqttConsts.RECEIVE_KEY_TENANT_CODE);
        this.cleanRuleCode = jsonPayload.getString(MqttConsts.RECEIVE_KEY_CLEAN_RULE_CODE);
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(cleanRuleCode)) {
            throw new IllegalArgumentException("tenantCode or cleanRuleCode is blank");
        }
    }

    @Override
    public void run() {
        JSONObject dispatchCont = getDispatchCont();

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(MqttConsts.SEND_KEY_TITLE, MqttConsts.SEND_TITLE_DISPATCH_OPEN_RULE);
        jsonMsg.put(MqttConsts.SEND_KEY_CLEAN_RULE, dispatchCont);
        log.info("$$$$$ CleanRuleDispatchWorker sendMsg: " + jsonMsg.toJSONString());

        // 准备发送
        List<String> machineCodeList = getMachineCodeList();
        if (CollectionUtils.isEmpty(machineCodeList)) {
            log.info("machine code list is empty, stop worker");
        }

        MqttService mqttService = getMQTTService();
        machineCodeList.stream().forEach(machineCode -> {
            mqttService.sendP2PMsgByTenant(tenantCode, machineCode, jsonMsg.toJSONString());
        });
    }

    private CleanRuleMgtService getCleanRuleMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        CleanRuleMgtService cleanRuleMgtService = appContext.getBean(CleanRuleMgtService.class);
        return cleanRuleMgtService;
    }

    private MqttService getMQTTService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MqttService mqttService = appContext.getBean(MqttService.class);
        return mqttService;
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
        CleanRuleMgtService cleanRuleMgtService = getCleanRuleMgtService();
        CleanRuleDTO cleanRuleDTO = getModel(cleanRuleMgtService.getByCode(tenantCode, cleanRuleCode));
        if (cleanRuleDTO == null) {
            log.info("clean rule error, stop worker");
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSON.toJSON(cleanRuleDTO);
        return jsonObject;
    }

    private List<String> getMachineCodeList() {
        CleanRuleMgtService cleanRuleMgtService = getCleanRuleMgtService();
        CleanRuleDispatchDTO cleanRuleDispatchDTO = getModel(cleanRuleMgtService.getDispatchByCode(tenantCode, cleanRuleCode));
        if (cleanRuleDispatchDTO == null) {
            log.info("clean rule dispatch is null");
            return null;
        }

        ShopMgtService shopMgtService = getShopMgtService();
        List<String> shopCodeList = cleanRuleDispatchDTO.getShopGroupCodeList().stream()
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
