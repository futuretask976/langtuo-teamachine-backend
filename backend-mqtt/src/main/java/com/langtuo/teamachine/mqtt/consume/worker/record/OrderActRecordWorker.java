package com.langtuo.teamachine.mqtt.consume.worker.record;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.langtuo.teamachine.api.request.record.OrderActRecordPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.OrderActRecordMgtService;
import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Slf4j
public class OrderActRecordWorker implements Runnable {
    String msg = "{\n" +
            "\t\"bizCode\": \"orderActRecord\",\n" +
            "\t\"list\": [{\n" +
            "  \t\"extraInfo\": {\n" +
            "  \t\t\"abc\": \"def\"\n" +
            "  \t},\n" +
            "  \t\"idempotentMark\": \"1723700246767\",\n" +
            "  \t\"machineCode\": \"abcd\",\n" +
            "  \t\"orderGmtCreated\": 1723700246767,\n" +
            "  \t\"outerOrderId\": \"111111\",\n" +
            "  \t\"shopCode\": \"shop_001\",\n" +
            "    \"shopName\": \"shop_001\",\n" +
            "  \t\"shopGroupCode\": \"shopGroup_02\",\n" +
            "    \"shopGroupName\": \"shopGroup_02\",\n" +
            "  \t\"specItemList\": [{\n" +
            "  \t\t\"specCode\": \"SPEC_SWEET\",\n" +
            "  \t\t\"specItemCode\": \"SPEC_ITEM_7_SWEET\",\n" +
            "  \t\t\"specItemName\": \"7分糖\",\n" +
            "  \t\t\"specName\": \"甜度\"\n" +
            "  \t}, {\n" +
            "  \t\t\"specCode\": \"SPEC_BEIXING\",\n" +
            "  \t\t\"specItemCode\": \"SPEC_ITEM_BIG\",\n" +
            "  \t\t\"specItemName\": \"大杯\",\n" +
            "  \t\t\"specName\": \"杯型\"\n" +
            "  \t}],\n" +
            "  \t\"state\": 0,\n" +
            "  \t\"tenantCode\": \"tenant_001\",\n" +
            "  \t\"toppingList\": [{\n" +
            "  \t\t\"actualAmount\": 20,\n" +
            "  \t\t\"stepIndex\": 1,\n" +
            "  \t\t\"toppingCode\": \"topping_002\",\n" +
            "  \t\t\"toppingName\": \"物料3\"\n" +
            "  \t}, {\n" +
            "  \t\t\"actualAmount\": 30,\n" +
            "  \t\t\"stepIndex\": 1,\n" +
            "  \t\t\"toppingCode\": \"topping_003\",\n" +
            "        \"toppingName\": \"物料4\"\n" +
            "  \t}]\n" +
            "  }]\n" +
            "}";

    /**
     *
     */
    private List<OrderActRecordPutRequest> requestList = Lists.newArrayList();

    public OrderActRecordWorker(JSONObject jsonPayload) {
        jsonPayload = JSON.parseObject(msg);
        JSONArray jsonList = jsonPayload.getJSONArray(MqttConsts.RECEIVE_KEY_LIST);
        jsonList.forEach(jsonObject -> {
            OrderActRecordPutRequest request = TypeUtils.castToJavaBean(jsonObject, OrderActRecordPutRequest.class);
            if (request.isValid()) {
                requestList.add(request);
            } else {
                log.error("request is invalid: " + jsonObject == null ? null : JSON.toJSONString(jsonObject));
            }
        });
        if (CollectionUtils.isEmpty(requestList)) {
            throw new IllegalArgumentException("request list is empty");
        }
    }

    @Override
    public void run() {
        OrderActRecordMgtService orderActRecordMgtService = SpringUtils.getOrderActRecordMgtService();
        for (OrderActRecordPutRequest request : requestList) {
            TeaMachineResult<Void> result = orderActRecordMgtService.put(request);
            if (result == null || !result.isSuccess()) {
                log.error("insert invalid act record error: " + result == null ? null : result.getErrorMsg());
            }
        }
    }

    public static void main(String args[]) {
        OrderActRecordWorker o = new OrderActRecordWorker(null);
    }
}
