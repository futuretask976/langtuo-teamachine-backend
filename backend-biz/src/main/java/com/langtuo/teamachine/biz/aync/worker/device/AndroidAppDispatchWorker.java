package com.langtuo.teamachine.biz.aync.worker.device;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.device.AndroidAppDTO;
import com.langtuo.teamachine.api.service.device.AndroidAppMgtService;
import com.langtuo.teamachine.biz.util.SpringServiceUtils;
import com.langtuo.teamachine.dao.accessor.device.AndroidAppDispatchAccessor;
import com.langtuo.teamachine.dao.po.device.AndroidAppDispatchPO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.dao.util.SpringAccessorUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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
        this.tenantCode = jsonPayload.getString(CommonConsts.JSON_KEY_TENANT_CODE);
        this.version = jsonPayload.getString(CommonConsts.JSON_KEY_VERSION);
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(version)) {
            log.error("androidAppDispatchWorker|init|illegalArgument|" + tenantCode + "|" + version);
            throw new IllegalArgumentException("tenantCode or version is blank");
        }
    }

    @Override
    public void run() {
        JSONObject jsonDispatchCont = getDispatchCont();
        if (jsonDispatchCont == null) {
            log.error("androidAppDispatchWorker|getDispatchCont|error|stopWorker|" + jsonDispatchCont);
            return;
        }

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_DISPATCH_ANDROID_APP);
        jsonMsg.put(CommonConsts.JSON_KEY_MODEL, jsonDispatchCont);

        // 准备发送
        List<String> machineCodeList = getMachineCodeList();
        if (CollectionUtils.isEmpty(machineCodeList)) {
            log.error("androidAppDispatchWorker|getMachineCodeList|empty|stopWorker|" + machineCodeList);
        }

        MqttProducer mqttProducer = SpringServiceUtils.getMqttProducer();
        machineCodeList.stream().forEach(machineCode -> {
            mqttProducer.sendP2PMsgByTenant(tenantCode, machineCode, jsonMsg.toJSONString());
        });
    }

    private List<String> getMachineCodeList() {
        AndroidAppDispatchAccessor androidAppDispatchAccessor = SpringAccessorUtils.getAndroidAppDispatchAccessor();
        List<AndroidAppDispatchPO> androidAppDispatchPOList = androidAppDispatchAccessor.listByVersion(
                tenantCode, version);
        if (CollectionUtils.isEmpty(androidAppDispatchPOList)) {
            log.error("androidAppDispatchWorker|getDispatch|error|stopWorker|" + androidAppDispatchPOList);
            return null;
        }

        List<String> shopCodeList = DaoUtils.getShopCodeListByShopGroupCodeList(tenantCode,
                androidAppDispatchPOList.stream()
                        .map(AndroidAppDispatchPO::getShopGroupCode)
                        .collect(Collectors.toList()));
        List<String> machineCodeList = DaoUtils.getMachineCodeListByShopCodeList(tenantCode, shopCodeList);
        return machineCodeList;
    }

    private JSONObject getDispatchCont() {
        AndroidAppMgtService androidAppMgtService = SpringServiceUtils.getAndroidAppMgtService();
        AndroidAppDTO dto = getModel(androidAppMgtService.get(version));
        if (dto == null) {
            log.error("androidAppDispatchWorker|getAndroidApp|error|stopWorker|" + dto);
            return null;
        }

        JSONObject jsonDispatchCont = (JSONObject) JSONObject.toJSON(dto);
        return jsonDispatchCont;
    }
}
