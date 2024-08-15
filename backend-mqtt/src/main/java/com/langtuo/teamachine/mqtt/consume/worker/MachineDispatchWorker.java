package com.langtuo.teamachine.mqtt.consume.worker;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.device.MachineDTO;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import com.langtuo.teamachine.mqtt.MqttService;
import com.langtuo.teamachine.mqtt.config.MqttConfig;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import static com.langtuo.teamachine.api.result.LangTuoResult.getModel;

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
        this.tenantCode = jsonPayload.getString(MqttConsts.RECEIVE_KEY_TENANT_CODE);
        this.machineCode = jsonPayload.getString(MqttConsts.RECEIVE_KEY_MACHINE_CODE);
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(machineCode)) {
            throw new IllegalArgumentException("tenantCode or machineCode is blank");
        }
    }

    @Override
    public void run() {
        JSONObject jsonObject = getDispatchCont();
        if (jsonObject == null) {
            log.info("dispatch content error, stop worker");
            return;
        }

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(MqttConsts.SEND_KEY_TITLE, MqttConsts.SEND_TITLE_DISPATCH_MACHINE);
        jsonMsg.put(MqttConsts.SEND_KEY_MACHINE, jsonObject);

        MqttService mqttService = getMQTTService();
        mqttService.sendDispatchMsgByTenant(tenantCode, jsonMsg.toJSONString());
    }

    private MqttService getMQTTService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MqttService mqttService = appContext.getBean(MqttService.class);
        return mqttService;
    }

    private MachineMgtService getMachineMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MachineMgtService machineMgtService = appContext.getBean(MachineMgtService.class);
        return machineMgtService;
    }

    private JSONObject getDispatchCont() {
        MachineMgtService machineMgtService = getMachineMgtService();
        MachineDTO dto = getModel(machineMgtService.get(tenantCode, machineCode));
        if (dto == null) {
            log.info("machine is null, stop worker");
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSON.toJSON(dto);
        return jsonObject;
    }
}
