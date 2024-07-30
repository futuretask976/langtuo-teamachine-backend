package com.langtuo.teamachine.dao.testor.recordset;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.recordset.InvalidActRecordMapper;
import com.langtuo.teamachine.dao.mapper.recordset.OrderSpecItemActRecordMapper;
import com.langtuo.teamachine.dao.mapper.recordset.OrderToppingActRecordMapper;
import com.langtuo.teamachine.dao.po.recordset.InvalidActRecordPO;
import com.langtuo.teamachine.dao.po.recordset.OrderSpecItemActRecordPO;
import com.langtuo.teamachine.dao.po.recordset.OrderToppingActRecordPO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class OrderToppingActRecordTestor {
    public static void main(String args[]) {
        insert();
//        select();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        OrderToppingActRecordMapper mapper = sqlSession.getMapper(OrderToppingActRecordMapper.class);

        OrderToppingActRecordPO po = null;
        int inserted = 0;

        po = new OrderToppingActRecordPO();
        po.setTenantCode("tenant_001");
        po.setIdempotentMark("1234");
        po.setStepIndex(1);
        po.setToppingCode("topping_002");
        po.setActualAmount(20);
        inserted = mapper.insert(po);
        System.out.println("inserted=" + inserted);

        po = new OrderToppingActRecordPO();
        po.setTenantCode("tenant_001");
        po.setIdempotentMark("1234");
        po.setStepIndex(1);
        po.setToppingCode("topping_003");
        po.setActualAmount(30);
        inserted = mapper.insert(po);
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
