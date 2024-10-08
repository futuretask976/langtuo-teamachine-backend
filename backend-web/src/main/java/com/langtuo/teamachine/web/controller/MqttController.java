package com.langtuo.teamachine.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.OrderActRecordMgtService;
import com.langtuo.teamachine.biz.util.BizUtils;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.DateUtils;
import com.langtuo.teamachine.internal.util.MessageUtils;
import com.langtuo.teamachine.mqtt.consume.worker.record.CleanActRecordWorker;
import com.langtuo.teamachine.mqtt.consume.worker.record.OrderActRecordWorker;
import com.langtuo.teamachine.mqtt.request.record.OrderActRecordPutRequest;
import com.langtuo.teamachine.mqtt.request.record.OrderSpecItemActRecordPutRequest;
import com.langtuo.teamachine.mqtt.request.record.OrderToppingActRecordPutRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author Jiaqing
 */
@RestController
@RequestMapping("/mqtt")
@Slf4j
public class MqttController {
    @Resource
    private OrderActRecordMgtService orderActRecordMgtService;

    /**
     * url: http://localhost:8080/teamachinebackend/mqtt/test
     * @return
     */
    @GetMapping(value = "/test")
    public TeaMachineResult<Void> test(Model model) {
        log.info("/mqtt/test entering: " + (model == null ? null : model.toString()));
        try {
            // mqttService.sendMsgByTopic("tenant_001", "testMq4Iot", "here is testMq4Iot test: " + System.currentTimeMillis());
            // mqttService.sendMsgByP2P("tenant_001", MQTTConfig.CLIENT_ID, "here is p2p test: " + System.currentTimeMillis());
        } catch (Exception e) {
            log.error("test error: " + e.getMessage(), e);
        }
        log.info("/mqtt/test exiting");
        return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.TEST_ERR_ONLY_TEST));
    }

    /**
     * url: http://localhost:8080/teamachinebackend/mqtt/testorderact
     * @param model
     * @return
     */
    @GetMapping(value = "/testorderact")
    public TeaMachineResult<Void> testOrderAct(Model model) {
        log.info("/mqtt/testorderact entering: " + (model == null ? null : model.toString()));
        try {
            log.info("mqttController|loop|begin");
            for (int i = 0; i < 5; i++) {
                OrderActRecordPutRequest request = new OrderActRecordPutRequest();
                request.setTenantCode("tenant_001");
                request.setExtraInfo(new HashMap(){{
                    put("abc", "def");
                }});
                request.setIdempotentMark(String.valueOf(System.currentTimeMillis()));
                request.setMachineCode("machine_444");
                request.setShopCode("shop_004");
                request.setShopGroupCode("shopGroup_02");
                request.setOrderGmtCreated(DateUtils.getDate(-4));
                request.setTeaTypeCode("TEA_TYPE_01");

                request.setTeaCode("TEA_06");
//                request.setTeaCode("TEA_07");
//                request.setTeaCode("TEA_08");

                request.setOuterOrderId(String.valueOf(System.currentTimeMillis()));
                request.setState(2);

                List<OrderSpecItemActRecordPutRequest> specItemList = Lists.newArrayList();
                OrderSpecItemActRecordPutRequest specItemReq1 = new OrderSpecItemActRecordPutRequest();

                specItemReq1.setSpecCode("SPEC_SWEET");
                specItemReq1.setSpecItemCode("SPEC_ITEM_7_SWEET");
//                specItemReq1.setSpecItemCode("SPEC_ITEM_5_SWEET");

                specItemList.add(specItemReq1);
                OrderSpecItemActRecordPutRequest specItemReq2 = new OrderSpecItemActRecordPutRequest();

                specItemReq2.setSpecCode("SPEC_BEIXING");
//                specItemReq2.setSpecItemCode("SPEC_ITEM_BIG");
                specItemReq2.setSpecItemCode("SPEC_ITEM_SMALL");

                specItemList.add(specItemReq2);
                request.setSpecItemList(specItemList);

                List<OrderToppingActRecordPutRequest> toppingList = Lists.newArrayList();
                OrderToppingActRecordPutRequest toppingReq1 = new OrderToppingActRecordPutRequest();
                toppingReq1.setStepIndex(1);
                toppingReq1.setToppingCode("topping_001");
                toppingReq1.setActualAmount(BizUtils.calcRandom(1, 99));
                toppingList.add(toppingReq1);
                OrderToppingActRecordPutRequest toppingReq2 = new OrderToppingActRecordPutRequest();
                toppingReq2.setStepIndex(2);
                toppingReq2.setToppingCode("topping_003");
                toppingReq2.setActualAmount(BizUtils.calcRandom(1, 99));
                toppingList.add(toppingReq2);
                request.setToppingList(toppingList);

                OrderActRecordWorker orderActRecordWorker = new OrderActRecordWorker(request);
                orderActRecordWorker.run();

                Thread.sleep(100);
            }
            log.info("mqttController|loop|end");
        } catch (Exception e) {
            log.error("test error: " + e.getMessage(), e);
        }
        return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.TEST_ERR_ONLY_TEST));
    }

    /**
     * url: http://localhost:8080/teamachinebackend/mqtt/testcleanact
     * @param model
     * @return
     */
    @GetMapping(value = "/testcleanact")
    public TeaMachineResult<Void> testCleanAct(Model model) {
        log.info("/mqtt/testcleanact entering: " + (model == null ? null : model.toString()));
        String c = "{\"bizCode\":\"cleanActRecord\",\"list\":[{\"cleanContent\":1,\"cleanEndTime\":1728367883993,\"cleanRuleCode\":\"CLEAN_02\",\"cleanStartTime\":1728367883994,\"cleanType\":1,\"closeRuleCode\":\"CLEAN_02\",\"flushIntervalMin\":40,\"flushSec\":30,\"idempotentMark\":\"20241008021123_20240904000004_cleanAct_2\",\"machineCode\":\"20240904000004\",\"openRuleCode\":\"CLEAN_02\",\"pipelineNum\":1,\"shopCode\":\"shop_333\",\"shopGroupCode\":\"shopGroup_07\",\"soakMin\":20,\"tenantCode\":\"tenant_001\",\"toppingCode\":\"topping_333\",\"valid\":true,\"washSec\":10}]}";
        try {
            CleanActRecordWorker cleanActRecordWorker = new CleanActRecordWorker(JSONObject.parseObject(c));
            cleanActRecordWorker.run();
        } catch (Exception e) {
            log.error("test error: " + e.getMessage(), e);
        }
        return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.TEST_ERR_ONLY_TEST));
    }
}
