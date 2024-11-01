package com.langtuo.teamachine.biz.aync.worker.device;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.device.ModelDTO;
import com.langtuo.teamachine.biz.convert.device.ModelMgtConvertor;
import com.langtuo.teamachine.biz.util.SpringServiceUtils;
import com.langtuo.teamachine.dao.accessor.device.ModelAccessor;
import com.langtuo.teamachine.dao.po.device.ModelPO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.dao.util.SpringAccessorUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Slf4j
public class ModelDispatchWorker implements Runnable {
    /**
     * 型号编码
     */
    private String modelCode;

    public ModelDispatchWorker(JSONObject jsonPayload) {
        this.modelCode = jsonPayload.getString(CommonConsts.JSON_KEY_MODEL_CODE);
        if (StringUtils.isBlank(modelCode)) {
            log.error("|modelDispatchWorker|init|illegalArgument|" + modelCode);
            throw new IllegalArgumentException("modelCode is blank");
        }
    }

    @Override
    public void run() {
        JSONObject jsonDispatchCont = getDispatchCont();
        if (jsonDispatchCont == null) {
            log.error("|modelDispatchWorker|getDispatchCont|error|stopWorker|" + jsonDispatchCont);
            return;
        }

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_DISPATCH_MODEL);
        jsonMsg.put(CommonConsts.JSON_KEY_MODEL, jsonDispatchCont);

        MqttProducer mqttProducer = SpringServiceUtils.getMqttProducer();
        List<String> tenantCodeList = DaoUtils.getTenantCodeList();
        for (String tenantCode : tenantCodeList) {
            log.info("|modelDispatchWorker|beforeCallSendBroadcast|" + tenantCode + "|" + jsonMsg.toJSONString() + "|");
            mqttProducer.sendBroadcastMsgByTenant(tenantCode, jsonMsg.toJSONString());
            log.info("|modelDispatchWorker|afterCallSendBroadcast|" + tenantCode + "|" + jsonMsg.toJSONString() + "|");
        }
    }

    private JSONObject getDispatchCont() {
        ModelAccessor modelAccessor = SpringAccessorUtils.getModelAccessor();
        ModelPO po = modelAccessor.getByModelCode(modelCode);
        ModelDTO dto = ModelMgtConvertor.convertToModelDTO(po);
        if (dto == null) {
            log.error("|machineDispatchWorker|getModel|error|stopWorker|" + dto + "|");
            return null;
        }

        JSONObject jsonDispatchCont = (JSONObject) JSONObject.toJSON(dto);
        return jsonDispatchCont;
    }
}
