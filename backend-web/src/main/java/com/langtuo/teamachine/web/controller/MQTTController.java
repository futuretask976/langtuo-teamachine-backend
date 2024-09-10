package com.langtuo.teamachine.web.controller;

import com.google.common.collect.Lists;
import com.langtuo.teamachine.api.request.record.OrderActRecordPutRequest;
import com.langtuo.teamachine.api.request.record.OrderSpecItemActRecordPutRequest;
import com.langtuo.teamachine.api.request.record.OrderToppingActRecordPutRequest;
import com.langtuo.teamachine.api.service.record.OrderActRecordMgtService;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.biz.service.util.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/mqtt")
@Slf4j
public class MQTTController {
    @Resource
    private OrderActRecordMgtService orderActRecordMgtService;

    @Autowired
    private MessageSource messageSource;

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
        return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.TEST_ERR_ONLY_TEST, messageSource));
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
            for (int i = 0; i < 10; i++) {
                OrderActRecordPutRequest request = new OrderActRecordPutRequest();
                request.setTenantCode("tenant_001");
                request.setExtraInfo(new HashMap(){{
                    put("abc", "def");
                }});
                request.setIdempotentMark(String.valueOf(System.currentTimeMillis()));
                request.setMachineCode("machine_444");
                request.setShopCode("shop_001");
                request.setShopGroupCode("shopGroup_02");
                request.setOrderGmtCreated(new Date());
                request.setTeaTypeCode("TEA_TYPE_01");
                request.setTeaCode("TEA_08");
                request.setOuterOrderId(String.valueOf(System.currentTimeMillis()));
                request.setState(2);

                List<OrderSpecItemActRecordPutRequest> specItemList = Lists.newArrayList();
                OrderSpecItemActRecordPutRequest specItemReq1 = new OrderSpecItemActRecordPutRequest();
                specItemReq1.setSpecCode("SPEC_SWEET");
                specItemReq1.setSpecName("甜度");
                specItemReq1.setSpecItemCode("SPEC_ITEM_5_SWEET");
                specItemReq1.setSpecItemName("5分糖");
                specItemList.add(specItemReq1);
                OrderSpecItemActRecordPutRequest specItemReq2 = new OrderSpecItemActRecordPutRequest();
                specItemReq2.setSpecCode("SPEC_BEIXING");
                specItemReq2.setSpecName("杯型");
                specItemReq2.setSpecItemCode("SPEC_ITEM_SMALL");
                specItemReq2.setSpecItemName("小杯");
                specItemList.add(specItemReq2);
                request.setSpecItemList(specItemList);

                List<OrderToppingActRecordPutRequest> toppingList = Lists.newArrayList();
                OrderToppingActRecordPutRequest toppingReq1 = new OrderToppingActRecordPutRequest();
                toppingReq1.setStepIndex(1);
                toppingReq1.setToppingCode("topping_002");
                toppingReq1.setToppingName("物料2");
                toppingReq1.setActualAmount(20);
                toppingList.add(toppingReq1);
                OrderToppingActRecordPutRequest toppingReq2 = new OrderToppingActRecordPutRequest();
                toppingReq2.setStepIndex(1);
                toppingReq2.setToppingCode("topping_003");
                toppingReq1.setToppingName("物料3");
                toppingReq2.setActualAmount(30);
                toppingList.add(toppingReq2);
                request.setToppingList(toppingList);

                orderActRecordMgtService.put(request);

                Thread.sleep(1000 * 2);
            }
        } catch (Exception e) {
            log.error("test error: " + e.getMessage(), e);
        }
        return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.TEST_ERR_ONLY_TEST, messageSource));
    }
}
