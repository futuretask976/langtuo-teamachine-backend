package com.langtuo.teamachine.mqtt.consume.worker.record;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.dao.accessor.record.CleanActRecordAccessor;
import com.langtuo.teamachine.dao.po.record.CleanActRecordPO;
import com.langtuo.teamachine.dao.util.SpringAccessorUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.request.record.CleanActRecordPutRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Slf4j
public class CleanActRecordWorker implements Runnable {
    /**
     *
     */
    private List<CleanActRecordPutRequest> requestList = Lists.newArrayList();

    public CleanActRecordWorker(JSONObject jsonPayload) {
        JSONArray jsonList = jsonPayload.getJSONArray(MqttConsts.RECEIVE_KEY_LIST);
        jsonList.forEach(jsonObject -> {
            CleanActRecordPutRequest request = TypeUtils.castToJavaBean(jsonObject, CleanActRecordPutRequest.class);
            requestList.add(request);
        });
    }

    @Override
    public void run() {
        if (CollectionUtils.isEmpty(requestList)) {
            log.error("cleanActRecordWorker|run|illegalArgument|requestListEmpty");
        }

        for (CleanActRecordPutRequest request : requestList) {
            put(request);
        }
    }

    private void put(CleanActRecordPutRequest request) {
        if (request == null || !request.isValid()) {
            log.error("cleanActRecordWorker|put|illegalArgument|"
                    + request == null ? null : JSONObject.toJSONString(request));
            return;
        }

        CleanActRecordPO po = convert(request);
        try {
            CleanActRecordAccessor cleanActRecordAccessor = SpringAccessorUtils.getCleanActRecordAccessor();
            CleanActRecordPO exist = cleanActRecordAccessor.getByIdempotentMark(po.getTenantCode(), po.getIdempotentMark());
            if (exist == null) {
                int inserted = cleanActRecordAccessor.insert(po);
                if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                    log.error("cleanActRecordWorker|put|error|" + inserted + "|" + JSONObject.toJSONString(po));
                }
            }
        } catch (Exception e) {
            log.error("cleanActRecordWorker|put|fatal|" + e.getMessage(), e);
        }
    }

    private CleanActRecordPO convert(CleanActRecordPutRequest request) {
        if (request == null) {
            return null;
        }

        CleanActRecordPO po = new CleanActRecordPO();
        po.setExtraInfo(request.getExtraInfo());
        po.setIdempotentMark(request.getIdempotentMark());
        po.setMachineCode(request.getMachineCode());
        po.setShopCode(request.getShopCode());
        po.setShopGroupCode(request.getShopGroupCode());
        po.setCleanStartTime(request.getCleanStartTime());
        po.setCleanEndTime(request.getCleanEndTime());
        po.setToppingCode(request.getToppingCode());
        po.setPipelineNum(request.getPipelineNum());
        po.setCleanType(request.getCleanType());
        po.setCleanRuleCode(request.getCleanRuleCode());
        po.setCleanContent(request.getCleanContent());
        po.setWashSec(request.getWashSec());
        po.setSoakMin(request.getSoakMin());
        po.setFlushSec(request.getFlushSec());
        po.setFlushIntervalMin(request.getFlushIntervalMin());
        return po;
    }
}
