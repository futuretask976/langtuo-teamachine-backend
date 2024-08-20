package com.langtuo.teamachine.biz.service.testor.record;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.langtuo.teamachine.api.request.record.OrderActRecordPutRequest;
import com.langtuo.teamachine.api.request.record.OrderSpecItemActRecordPutRequest;
import com.langtuo.teamachine.api.request.record.OrderToppingActRecordPutRequest;
import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.record.InvalidActRecordMapper;
import com.langtuo.teamachine.dao.mapper.record.OrderActRecordMapper;
import com.langtuo.teamachine.dao.po.record.InvalidActRecordPO;
import com.langtuo.teamachine.dao.po.record.OrderActRecordPO;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderActRecordTestor {
    public static void main(String args[]) {
        insert();
//        select();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        OrderActRecordMapper mapper = sqlSession.getMapper(OrderActRecordMapper.class);

        OrderActRecordPutRequest request = new OrderActRecordPutRequest();
        request.setTenantCode("tenant_001");
        request.setExtraInfo(new HashMap(){{
            put("abc", "def");
        }});
        request.setIdempotentMark(String.valueOf(System.currentTimeMillis()));
        request.setMachineCode("abcd");
        request.setShopCode("shop_001");
        request.setShopGroupCode("shopGroup_02");
        request.setOrderGmtCreated(new Date());
        request.setOuterOrderId("111111");
        request.setState(0);

        List<OrderSpecItemActRecordPutRequest> specItemList = Lists.newArrayList();
        OrderSpecItemActRecordPutRequest specItemReq1 = new OrderSpecItemActRecordPutRequest();
        specItemReq1.setSpecCode("SPEC_SWEET");
        specItemReq1.setSpecName("甜度");
        specItemReq1.setSpecItemCode("SPEC_ITEM_7_SWEET");
        specItemReq1.setSpecItemName("7分糖");
        specItemList.add(specItemReq1);
        OrderSpecItemActRecordPutRequest specItemReq2 = new OrderSpecItemActRecordPutRequest();
        specItemReq2.setSpecCode("SPEC_BEIXING");
        specItemReq2.setSpecName("杯型");
        specItemReq2.setSpecItemCode("SPEC_ITEM_BIG");
        specItemReq2.setSpecItemName("大杯");
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

        // mapper.insert(convertToOrderActRecordPO(request));
        System.out.println(JSONObject.toJSONString(convertToOrderActRecordPO(request)));

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        InvalidActRecordMapper mapper = sqlSession.getMapper(InvalidActRecordMapper.class);

        List<InvalidActRecordPO> list = mapper.selectList("tenant_001");
        for (InvalidActRecordPO po : list) {
            System.out.printf("$$$$$ list->po: %s\n", po);
        }

        InvalidActRecordPO po = mapper.selectOne("tenant_001", "1234567890");
        System.out.printf("$$$$$ po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    private static OrderActRecordPO convertToOrderActRecordPO(OrderActRecordPutRequest request) {
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
        po.setOuterOrderId(request.getOuterOrderId());
        po.setState(request.getState());
        return po;
    }
}
