package com.langtuo.teamachine.biz.service.testor.record;

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

        OrderActRecordPO po = null;

        po = new OrderActRecordPO();
        po.setTenantCode("tenant_001");
        po.setExtraInfo(new HashMap(){{
            put("abc", "def");
        }});
        po.setIdempotentMark("1234");
        po.setMachineCode("abcd");
        po.setShopCode("shop_001");
        po.setShopGroupCode("shopGroup_02");
        po.setOrderGmtCreated(new Date());
        po.setOuterOrderId("111111");
        po.setState(0);
        int inserted = mapper.insert(po);
        System.out.println("inserted=" + inserted);

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

//    public static void testBySpring() {
//        ApplicationContext context = startBySpring();
//
//        AdminMapper mapper =
//                (AdminMapper) context.getBean(AdminMapper.class);
//        AdminPOjo AdminPOjo = mapper.getOne("machine_001");
//        System.out.printf("DaoDemo#testBySpring hotelGuestPojo=%s\n", AdminPOjo);
//
//        stopBySpring(context);
//    }
//
//    public static ApplicationContext startBySpring() {
//        ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-config.xml");
//        if (context == null) {
//            throw new RuntimeException("DaoDemo#startBySpring初始化Spring失败");
//        }
//        return context;
//    }
//
//    public static void stopBySpring(ApplicationContext context) {
//        if (context == null) {
//            return;
//        }
//        if (context instanceof ClassPathXmlApplicationContext) {
//            ((ClassPathXmlApplicationContext) context).close();
//        } else {
//            System.out.println("DaoDemo#stopBySpring上下文参数异常");
//        }
//    }
}
