package com.langtuo.teamachine.mqtt.consume.worker.record;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.dao.accessor.record.CleanActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.record.DrainActRecordAccessor;
import com.langtuo.teamachine.dao.po.record.CleanActRecordPO;
import com.langtuo.teamachine.dao.po.record.DrainActRecordPO;
import com.langtuo.teamachine.dao.util.SpringAccessorUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.request.record.DrainActRecordPutRequest;
import com.langtuo.teamachine.mqtt.util.SpringTemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * @author Jiaqing
 */
@Slf4j
public class DrainActRecordWorker implements Runnable {
    /**
     * 转换后的请求列表
     */
    private List<DrainActRecordPutRequest> requestList = Lists.newArrayList();

    /**
     * 事务管理器
     */
    private TransactionTemplate transactionTemplate;

    public DrainActRecordWorker(JSONObject jsonPayload) {
        JSONArray jsonList = jsonPayload.getJSONArray(MqttConsts.RECEIVE_KEY_LIST);
        jsonList.forEach(jsonObject -> {
            DrainActRecordPutRequest request = TypeUtils.castToJavaBean(jsonObject, DrainActRecordPutRequest.class);
            requestList.add(request);
        });
        transactionTemplate = SpringTemplateUtils.getTransactionTemplate();
    }

    @Override
    public void run() {
        if (CollectionUtils.isEmpty(requestList)) {
            log.error("drainActRecordWorker|run|illegalArgument|requestListEmpty");
        }

        for (DrainActRecordPutRequest request : requestList) {
            put(request);
        }
    }

    public void put(DrainActRecordPutRequest request) {
        if (request == null || !request.isValid()) {
            log.error("drainActRecordWorker|put|illegalArgument|"
                    + request == null ? null : JSONObject.toJSONString(request));
            return;
        }

        DrainActRecordPO po = convert(request);
        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus status) {
                try {
                    DrainActRecordAccessor drainActRecordAccessor = SpringAccessorUtils.getDrainActRecordAccessor();
                    DrainActRecordPO exist = drainActRecordAccessor.getByIdempotentMark(po.getTenantCode(), po.getIdempotentMark());
                    if (exist == null) {
                        int inserted = drainActRecordAccessor.insert(po);
                        if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                            log.error("drainActRecordWorker|put|error|" + inserted + "|" + JSONObject.toJSONString(po));
                        }
                    }
                } catch (Exception e) {
                    log.error("drainActRecordWorker|put|fatal|" + e.getMessage(), e);
                    status.setRollbackOnly();
                }
                return null;
            }
        });
    }

    private DrainActRecordPO convert(DrainActRecordPutRequest request) {
        if (request == null) {
            return null;
        }

        DrainActRecordPO po = new DrainActRecordPO();
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setIdempotentMark(request.getIdempotentMark());
        po.setMachineCode(request.getMachineCode());
        po.setShopCode(request.getShopCode());
        po.setShopGroupCode(request.getShopGroupCode());
        po.setDrainStartTime(request.getDrainStartTime());
        po.setDrainEndTime(request.getDrainEndTime());
        po.setToppingCode(request.getToppingCode());
        po.setPipelineNum(request.getPipelineNum());
        po.setDrainType(request.getDrainType());
        po.setDrainRuleCode(request.getDrainRuleCode());
        po.setFlushSec(request.getFlushSec());
        po.setFlushWeight(request.getFlushWeight());
        return po;
    }
}
