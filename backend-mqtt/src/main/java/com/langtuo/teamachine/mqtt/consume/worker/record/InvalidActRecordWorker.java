package com.langtuo.teamachine.mqtt.consume.worker.record;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.dao.accessor.record.InvalidActRecordAccessor;
import com.langtuo.teamachine.dao.po.record.InvalidActRecordPO;
import com.langtuo.teamachine.dao.util.SpringAccessorUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.request.record.InvalidActRecordPutRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Slf4j
public class InvalidActRecordWorker implements Runnable {
    /**
     *
     */
    private List<InvalidActRecordPutRequest> requestList = Lists.newArrayList();

    public InvalidActRecordWorker(JSONObject jsonPayload) {
        JSONArray jsonList = jsonPayload.getJSONArray(MqttConsts.RECEIVE_KEY_LIST);
        jsonList.forEach(jsonObject -> {
            InvalidActRecordPutRequest request = TypeUtils.castToJavaBean(jsonObject, InvalidActRecordPutRequest.class);
            requestList.add(request);
        });
    }

    @Override
    public void run() {
        if (CollectionUtils.isEmpty(requestList)) {
            log.error("invalidActRecordWorker|run|illegalArgument|requestListEmpty");
        }

        for (InvalidActRecordPutRequest request : requestList) {
            put(request);
        }
    }

    public void put(InvalidActRecordPutRequest request) {
        if (request == null || !request.isValid()) {
            log.error("invalidActRecordWorker|put|illegalArgument|"
                    + request == null ? null : JSONObject.toJSONString(request));
            return;
        }

        InvalidActRecordPO po = convert(request);
        try {
            InvalidActRecordAccessor invalidActRecordAccessor = SpringAccessorUtils.getInvalidActRecordAccessor();
            InvalidActRecordPO exist = invalidActRecordAccessor.getByIdempotentMark(po.getTenantCode(), po.getIdempotentMark());
            if (exist == null) {
                int inserted = invalidActRecordAccessor.insert(po);
                if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                    log.error("invalidActRecordWorker|put|error|" + inserted + "|" + JSONObject.toJSONString(po));
                }
            }
        } catch (Exception e) {
            log.error("invalidActRecordWorker|put|fatal|" + e.getMessage(), e);
        }
    }

    private InvalidActRecordPO convert(InvalidActRecordPutRequest request) {
        if (request == null) {
            return null;
        }

        InvalidActRecordPO po = new InvalidActRecordPO();
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setIdempotentMark(request.getIdempotentMark());
        po.setMachineCode(request.getMachineCode());
        po.setShopCode(request.getShopCode());
        po.setShopGroupCode(request.getShopGroupCode());
        po.setInvalidTime(request.getInvalidTime());
        po.setToppingCode(request.getToppingCode());
        po.setPipelineNum(request.getPipelineNum());
        po.setInvalidAmount(request.getInvalidAmount());
        return po;
    }
}
