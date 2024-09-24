package com.langtuo.teamachine.web.controller;

import com.google.common.collect.Lists;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.OrderActRecordMgtService;
import com.langtuo.teamachine.biz.util.BizUtils;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.DateUtils;
import com.langtuo.teamachine.internal.util.MessageUtils;
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
            for (int i = 0; i < 3; i++) {
                OrderActRecordPutRequest request = new OrderActRecordPutRequest();
                request.setTenantCode("tenant_001");
                request.setExtraInfo(new HashMap(){{
                    put("abc", "def");
                }});
                request.setIdempotentMark(String.valueOf(System.currentTimeMillis()));
                request.setMachineCode("machine_444");
                request.setShopCode("shop_002");
                request.setShopGroupCode("shopGroup_03");
                request.setOrderGmtCreated(DateUtils.getDate(-5));
                request.setTeaTypeCode("TEA_TYPE_01");

                request.setTeaCode("TEA_06");
//                request.setTeaCode("TEA_07");
//                request.setTeaCode("TEA_08");

                request.setOuterOrderId(String.valueOf(System.currentTimeMillis()));
                request.setState(2);

                List<OrderSpecItemActRecordPutRequest> specItemList = Lists.newArrayList();
                OrderSpecItemActRecordPutRequest specItemReq1 = new OrderSpecItemActRecordPutRequest();
                specItemReq1.setSpecCode("SPEC_SWEET");

//                specItemReq1.setSpecItemCode("SPEC_ITEM_7_SWEET");
                specItemReq1.setSpecItemCode("SPEC_ITEM_5_SWEET");

                specItemList.add(specItemReq1);
                OrderSpecItemActRecordPutRequest specItemReq2 = new OrderSpecItemActRecordPutRequest();
                specItemReq2.setSpecCode("SPEC_BEIXING");

                specItemReq2.setSpecItemCode("SPEC_ITEM_BIG");
//                specItemReq2.setSpecItemCode("SPEC_ITEM_SMALL");

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

                Thread.sleep(1000 * 2);
            }
            log.info("mqttController|loop|end");
        } catch (Exception e) {
            log.error("test error: " + e.getMessage(), e);
        }
        return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.TEST_ERR_ONLY_TEST));
    }
}
