package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.OrderActRecordMapper;
import com.langtuo.teamachine.dao.po.OrderActRecordPO;
import org.apache.ibatis.session.SqlSession;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderActRecordTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        delete();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        OrderActRecordMapper mapper = sqlSession.getMapper(OrderActRecordMapper.class);

        OrderActRecordPO po = null;

        po = new OrderActRecordPO();
        po.setMachineCode("machine_001");
        po.setShopCode("shop_001");
        po.setOrderGmtCreated("2025-05-05 20:02:00");
        po.setOuterOrderId("outer_001");
        po.setState(1);
        po.setTenantCode("tenant_001");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new OrderActRecordPO();
        po.setMachineCode("machine_002");
        po.setShopCode("shop_002");
        po.setOrderGmtCreated("2023-05-05 20:02:00");
        po.setOuterOrderId("outer_002");
        po.setState(3);
        po.setTenantCode("tenant_002");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        OrderActRecordMapper mapper = sqlSession.getMapper(OrderActRecordMapper.class);

        List<OrderActRecordPO> list = mapper.selectList();
        for (OrderActRecordPO po : list) {
            System.out.printf("$$$$$ list->po: %s\n", po);
        }

        OrderActRecordPO po = mapper.selectOne("tenant_002", "machine_002",
                "shop_002", "2023-05-05 20:02:00");
        System.out.printf("$$$$$ po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void delete() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        OrderActRecordMapper mapper = sqlSession.getMapper(OrderActRecordMapper.class);

        mapper.delete("tenant_002", "machine_002",
                "shop_002", "2023-05-05 20:02:00");

        sqlSession.commit();
        sqlSession.close();
    }

    private static Date getTestDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2024);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 12);
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 25);
        calendar.set(Calendar.SECOND, 15);
        return calendar.getTime();
    }

//    public static void testBySpring() {
//        ApplicationContext context = startBySpring();
//
//        MachineMapper mapper =
//                (MachineMapper) context.getBean(MachineMapper.class);
//        MachinePojo machinePojo = mapper.getOne("machine_001");
//        System.out.printf("DaoDemo#testBySpring hotelGuestPojo=%s\n", machinePojo);
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
