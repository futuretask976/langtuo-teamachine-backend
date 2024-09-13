package com.langtuo.teamachine.biz.service.aync.worker.device;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.device.AndroidAppDTO;
import com.langtuo.teamachine.api.model.device.AndroidAppDispatchDTO;
import com.langtuo.teamachine.api.model.device.MachineDTO;
import com.langtuo.teamachine.api.model.device.ModelDTO;
import com.langtuo.teamachine.api.model.menu.MenuDispatchDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.model.user.TenantDTO;
import com.langtuo.teamachine.api.service.device.AndroidAppMgtService;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import com.langtuo.teamachine.api.service.device.ModelMgtService;
import com.langtuo.teamachine.api.service.menu.MenuMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.api.service.user.TenantMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
import com.langtuo.teamachine.mqtt.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.TeaMachineResult.getListModel;
import static com.langtuo.teamachine.api.result.TeaMachineResult.getModel;

@Slf4j
public class AndroidAppDispatchWorker implements Runnable {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 型号编码
     */
    private String version;

    public AndroidAppDispatchWorker(JSONObject jsonPayload) {
        this.tenantCode = jsonPayload.getString(BizConsts.JSON_KEY_TENANT_CODE);
        this.version = jsonPayload.getString(BizConsts.JSON_KEY_VERSION);
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(version)) {
            throw new IllegalArgumentException("tenantCode or version is blank");
        }
    }

    @Override
    public void run() {
        JSONObject jsonDispatchCont = getDispatchCont();
        if (jsonDispatchCont == null) {
            log.info("dispatch content error, stop worker");
            return;
        }

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(BizConsts.JSON_KEY_BIZ_CODE, BizConsts.BIZ_CODE_DISPATCH_ANDROID_APP);
        jsonMsg.put(BizConsts.JSON_KEY_MODEL, jsonDispatchCont);

        // 准备发送
        List<String> machineCodeList = getMachineCodeList();
        if (CollectionUtils.isEmpty(machineCodeList)) {
            log.info("menuDispatchWorker|getMachineCodeList|empty|stopWorker");
        }

        MqttProducer mqttProducer = SpringUtils.getMqttProducer();
        machineCodeList.stream().forEach(machineCode -> {
            mqttProducer.sendP2PMsgByTenant(tenantCode, machineCode, jsonMsg.toJSONString());
        });
    }

    private List<String> getMachineCodeList() {
        AndroidAppMgtService androidAppMgtService = SpringUtils.getAndroidAppMgtService();
        AndroidAppDispatchDTO androidAppDispatchDTO = getModel(androidAppMgtService.getDispatchByVersion(
                tenantCode, version));
        if (androidAppDispatchDTO == null) {
            log.info("androidAppDispatchWorker|getDispatch|null|stopWorker");
            return null;
        }

        ShopMgtService shopMgtService = SpringUtils.getShopMgtService();
        List<String> shopCodeList = androidAppDispatchDTO.getShopGroupCodeList().stream()
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
            log.info("menuDispatchWorker|getShopList|empty|stopWorker");
            return null;
        }

        MachineMgtService machineMgtService = SpringUtils.getMachineMgtService();
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

    private JSONObject getDispatchCont() {
        AndroidAppMgtService androidAppMgtService = SpringUtils.getAndroidAppMgtService();
        AndroidAppDTO dto = getModel(androidAppMgtService.get(version));
        if (dto == null) {
            log.info("android app is empty, stop worker");
            return null;
        }

        JSONObject jsonDispatchCont = (JSONObject) JSONObject.toJSON(dto);
        return jsonDispatchCont;
    }
}
