package com.langtuo.teamachine.mqtt.consume.worker.record;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.dao.accessor.record.OrderActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.record.OrderSpecItemActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.record.OrderToppingActRecordAccessor;
import com.langtuo.teamachine.dao.po.record.OrderActRecordPO;
import com.langtuo.teamachine.dao.po.record.OrderSpecItemActRecordPO;
import com.langtuo.teamachine.dao.po.record.OrderToppingActRecordPO;
import com.langtuo.teamachine.dao.util.SpringAccessorUtils;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.request.record.OrderActRecordPutRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jiaqing
 */
@Slf4j
public class OrderActRecordWorker implements Runnable {
    /**
     *
     */
    private List<OrderActRecordPutRequest> requestList = Lists.newArrayList();

    public OrderActRecordWorker(OrderActRecordPutRequest request) {
        requestList.add(request);
    }

    public OrderActRecordWorker(JSONObject jsonPayload) {
        JSONArray jsonList = jsonPayload.getJSONArray(MqttConsts.RECEIVE_KEY_LIST);
        jsonList.forEach(jsonObject -> {
            OrderActRecordPutRequest request = TypeUtils.castToJavaBean(jsonObject, OrderActRecordPutRequest.class);
            requestList.add(request);
        });
    }

    @Override
    public void run() {
        if (CollectionUtils.isEmpty(requestList)) {
            log.error("orderActRecordWorker|run|illegalArgument|requestListEmpty");
        }

        for (OrderActRecordPutRequest request : requestList) {
            put(request);
        }
    }

    public void put(OrderActRecordPutRequest request) {
        if (request == null || !request.isValid()) {
            log.error("orderActRecordWorker|put|illegalArgument|"
                    + request == null ? null : JSONObject.toJSONString(request));
            return;
        }

        OrderActRecordPO po = convertToOrderActRecordPO(request);
        List<OrderSpecItemActRecordPO> orderSpecItemActRecordPOList = convertToSpecItemActRecordPO(request);
        List<OrderToppingActRecordPO> orderToppingActRecordPOList = convertToOrderToppingActRecordPO(request);
        try {
            OrderActRecordAccessor orderActRecordAccessor = SpringAccessorUtils.getOrderActRecordAccessor();
            OrderActRecordPO exist = orderActRecordAccessor.getByIdempotentMark(po.getTenantCode(),
                    po.getIdempotentMark());
            if (exist == null) {
                int inserted = orderActRecordAccessor.insert(po);
            }

            OrderSpecItemActRecordAccessor orderSpecItemActRecordAccessor =
                    SpringAccessorUtils.getOrderSpecItemActRecordAccessor();
            int deleted4SpecItemActRec = orderSpecItemActRecordAccessor.delete(po.getTenantCode(),
                    po.getIdempotentMark());
            for (OrderSpecItemActRecordPO orderSpecItemActRecordPO : orderSpecItemActRecordPOList) {
                int inserted4SpecItemActRec = orderSpecItemActRecordAccessor.insert(orderSpecItemActRecordPO);
            }

            OrderToppingActRecordAccessor orderToppingActRecordAccessor =
                    SpringAccessorUtils.getOrderToppingActRecordAccessor();
            int deleted4ToppingActRec = orderToppingActRecordAccessor.delete(po.getTenantCode(),
                    po.getIdempotentMark());
            for (OrderToppingActRecordPO orderToppingActRecordPO : orderToppingActRecordPOList) {
                int inserted4ToppingActRec = orderToppingActRecordAccessor.insert(orderToppingActRecordPO);
            }
        } catch (Exception e) {
            log.error("orderActRecordWorker|put|fatal|" + e.getMessage(), e);
        }
    }

    public static OrderActRecordPO convertToOrderActRecordPO(OrderActRecordPutRequest request) {
        if (request == null) {
            return null;
        }

        OrderActRecordPO po = new OrderActRecordPO();
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setIdempotentMark(request.getIdempotentMark());
        po.setMachineCode(request.getMachineCode());
        po.setShopCode(request.getShopCode());
        po.setShopGroupCode(request.getShopGroupCode());
        po.setOrderGmtCreated(request.getOrderGmtCreated());
        po.setTeaTypeCode(request.getTeaTypeCode());
        po.setTeaCode(request.getTeaCode());
        po.setOuterOrderId(request.getOuterOrderId());
        po.setState(request.getState());
        return po;
    }

    public static List<OrderSpecItemActRecordPO> convertToSpecItemActRecordPO(OrderActRecordPutRequest request) {
        if (request == null || org.springframework.util.CollectionUtils.isEmpty(request.getSpecItemList())) {
            return null;
        }

        List<OrderSpecItemActRecordPO> specItemList = request.getSpecItemList().stream()
                .map(orderSpecItemActRecordPutRequest -> {
                    OrderSpecItemActRecordPO po = new OrderSpecItemActRecordPO();
                    po.setTenantCode(request.getTenantCode());
                    po.setIdempotentMark(request.getIdempotentMark());
                    po.setSpecCode(orderSpecItemActRecordPutRequest.getSpecCode());
                    po.setSpecItemCode(orderSpecItemActRecordPutRequest.getSpecItemCode());
                    return po;
                }).collect(Collectors.toList());
        return specItemList;
    }

    public static List<OrderToppingActRecordPO> convertToOrderToppingActRecordPO(OrderActRecordPutRequest request) {
        if (request == null || org.springframework.util.CollectionUtils.isEmpty(request.getSpecItemList())) {
            return null;
        }

        List<OrderToppingActRecordPO> specItemList = request.getToppingList().stream()
                .map(orderToppingActRecordPutRequest -> {
                    OrderToppingActRecordPO po = new OrderToppingActRecordPO();
                    po.setTenantCode(request.getTenantCode());
                    po.setIdempotentMark(request.getIdempotentMark());
                    po.setStepIndex(orderToppingActRecordPutRequest.getStepIndex());
                    po.setToppingCode(orderToppingActRecordPutRequest.getToppingCode());
                    po.setActualAmount(orderToppingActRecordPutRequest.getActualAmount());
                    return po;
                }).collect(Collectors.toList());
        return specItemList;
    }
}
