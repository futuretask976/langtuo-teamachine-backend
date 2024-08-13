package com.langtuo.teamachine.mqtt.worker.dispatch;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.device.MachineDTO;
import com.langtuo.teamachine.api.model.menu.MenuDispatchDTO;
import com.langtuo.teamachine.api.model.rule.CleanRuleDTO;
import com.langtuo.teamachine.api.model.rule.CleanRuleDispatchDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import com.langtuo.teamachine.api.service.drink.TeaMgtService;
import com.langtuo.teamachine.api.service.menu.SeriesMgtService;
import com.langtuo.teamachine.api.service.rule.CleanRuleMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.dao.oss.OSSUtils;
import com.langtuo.teamachine.mqtt.MQTTService;
import com.langtuo.teamachine.mqtt.config.MQTTConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.LangTuoResult.getListModel;
import static com.langtuo.teamachine.api.result.LangTuoResult.getModel;

@Slf4j
public class CleanRuleDispatchWorker implements Runnable {
    /**
     * 收到的消息中的key关键字
     */
    private static final String RECEIVE_KEY_TENANT_CODE = "tenantCode";
    private static final String RECEIVE_KEY_CLEAN_RULE_CODE = "cleanRuleCode";

    /**
     * 发送的消息中的key关键字
     */
    private static final String SEND_KEY_TOPIC = "topic";
    private static final String SEND_KEY_CLEAN_RULE = "cleanRule";


    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 清洁规则编码
     */
    private String cleanRuleCode;

    public CleanRuleDispatchWorker(String payload) {
        JSONObject jsonPayload = JSONObject.parseObject(payload);
        this.tenantCode = jsonPayload.getString(RECEIVE_KEY_TENANT_CODE);
        this.cleanRuleCode = jsonPayload.getString(RECEIVE_KEY_CLEAN_RULE_CODE);
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(cleanRuleCode)) {
            throw new IllegalArgumentException("tenantCode or cleanRuleCode is blank");
        }
    }

    @Override
    public void run() {
        JSONObject dispatchCont = getDispatchCont();

        // 准备发送
        List<String> machineCodeList = getMachineCodeList();
        if (CollectionUtils.isEmpty(machineCodeList)) {
            log.info("machine code list is empty, stop worker");
        }

        MQTTService mqttService = getMQTTService();
        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(SEND_KEY_TOPIC, MQTTConfig.TOPIC_DISPATCH_CLEAN_RULE);
        jsonMsg.put(SEND_KEY_CLEAN_RULE, dispatchCont);
        machineCodeList.stream().forEach(machineCode -> {
            mqttService.sendMsgByTopic(tenantCode, MQTTConfig.TOPIC_DISPATCH_CLEAN_RULE, jsonMsg.toJSONString());
        });
    }

    private CleanRuleMgtService getCleanRuleMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        CleanRuleMgtService cleanRuleMgtService = appContext.getBean(CleanRuleMgtService.class);
        return cleanRuleMgtService;
    }

    private MQTTService getMQTTService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MQTTService mqttService = appContext.getBean(MQTTService.class);
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

    private SeriesMgtService getSeriesMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        SeriesMgtService seriesMgtService = appContext.getBean(SeriesMgtService.class);
        return seriesMgtService;
    }

    private TeaMgtService getTeaMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        TeaMgtService teaMgtService = appContext.getBean(TeaMgtService.class);
        return teaMgtService;
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

    private String uploadOSS(File file) {
        String ossPath = null;
        try {
            ossPath = OSSUtils.uploadFile(file);
        } catch (FileNotFoundException e) {
            log.info("upload oss error, stop worker");
        }
        return ossPath;
    }

    private String calcMD5Hex(File file) {
        String md5AsHex = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            md5AsHex = DigestUtils.md5DigestAsHex(fileInputStream);
        } catch (IOException e) {
            log.info("calc md5 error, stop worker");
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.info("close file input stream error, stop worker");
                }
            }
        }
        return md5AsHex;
    }
}
