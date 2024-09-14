package com.langtuo.teamachine.mqtt.consume.worker.record;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.dao.accessor.record.SupplyActRecordAccessor;
import com.langtuo.teamachine.dao.po.record.SupplyActRecordPO;
import com.langtuo.teamachine.dao.util.SpringUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.request.record.SupplyActRecordPutRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Slf4j
public class SupplyActRecordWorker implements Runnable {
    /**
     *
     */
    private List<SupplyActRecordPutRequest> requestList = Lists.newArrayList();

    public SupplyActRecordWorker(JSONObject jsonPayload) {
        JSONArray jsonList = jsonPayload.getJSONArray(MqttConsts.RECEIVE_KEY_LIST);
        jsonList.forEach(jsonObject -> {
            SupplyActRecordPutRequest request = TypeUtils.castToJavaBean(jsonObject, SupplyActRecordPutRequest.class);
            requestList.add(request);
        });
    }

    public void run() {
        if (CollectionUtils.isEmpty(requestList)) {
            log.error("supplyActRecordWorker|run|illegalArgument|requestListEmpty");
        }

        for (SupplyActRecordPutRequest request : requestList) {
            put(request);
        }
    }

    public void put(SupplyActRecordPutRequest request) {
        if (request == null || !request.isValid()) {
            log.error("supplyActRecordWorker|put|illegalArgument|"
                    + request == null ? null : JSONObject.toJSONString(request));
            return;
        }

        SupplyActRecordPO po = convert(request);
        try {
            SupplyActRecordAccessor supplyActRecordAccessor = SpringUtils.getSupplyActRecordAccessor();
            SupplyActRecordPO exist = supplyActRecordAccessor.selectOne(po.getTenantCode(), po.getIdempotentMark());
            if (exist == null) {
                int inserted = supplyActRecordAccessor.insert(po);
                if (CommonConsts.NUM_ONE != inserted) {
                    log.error("supplyActRecordWorker|put|error|" + inserted + "|" + JSONObject.toJSONString(po));
                }
            }
        } catch (Exception e) {
            log.error("supplyActRecordWorker|put|fatal|" + e.getMessage(), e);
        }
    }

    private SupplyActRecordPO convert(SupplyActRecordPutRequest request) {
        if (request == null) {
            return null;
        }

        SupplyActRecordPO po = new SupplyActRecordPO();
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setIdempotentMark(request.getIdempotentMark());
        po.setMachineCode(request.getMachineCode());
        po.setShopCode(request.getShopCode());
        po.setShopGroupCode(request.getShopGroupCode());
        po.setSupplyTime(request.getSupplyTime());
        po.setToppingCode(request.getToppingCode());
        po.setPipelineNum(request.getPipelineNum());
        po.setSupplyAmount(request.getSupplyAmount());
        return po;
    }
}
