package com.langtuo.teamachine.biz.aync.worker.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.device.MachineDTO;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import com.langtuo.teamachine.biz.util.SpringUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import static com.langtuo.teamachine.api.result.TeaMachineResult.getModel;

@Slf4j
public class MachineDispatchWorker implements Runnable {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 机器编码
     */
    private String machineCode;

    public MachineDispatchWorker(JSONObject jsonPayload) {
        this.tenantCode = jsonPayload.getString(CommonConsts.JSON_KEY_TENANT_CODE);
        this.machineCode = jsonPayload.getString(CommonConsts.JSON_KEY_MACHINE_CODE);
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(machineCode)) {
            throw new IllegalArgumentException("tenantCode or machineCode is blank");
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
        jsonMsg.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_DISPATCH_MACHINE);
        jsonMsg.put(CommonConsts.JSON_KEY_MACHINE, jsonDispatchCont);

        MqttProducer mqttProducer = SpringUtils.getMqttProducer();
        mqttProducer.sendP2PMsgByTenant(tenantCode, machineCode, jsonMsg.toJSONString());
    }

    private JSONObject getDispatchCont() {
        MachineMgtService machineMgtService = SpringUtils.getMachineMgtService();
        MachineDTO dto = getModel(machineMgtService.getByCode(tenantCode, machineCode));
        if (dto == null) {
            log.info("machine is null, stop worker");
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSON.toJSON(dto);
        return jsonObject;
    }
}
