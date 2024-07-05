package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.ToppingMapper;
import com.langtuo.teamachine.dao.po.ToppingPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class TeaToppingPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        ToppingMapper mapper = sqlSession.getMapper(ToppingMapper.class);

        ToppingPO po = null;

        po = new ToppingPO();
        po.setTenantCode("tenant_001");
        po.setToppingCode("topping_001");
        po.setToppingName("topping_name_001");
        po.setToppingTypeCode("topping_type_001");
        po.setMeasureUnit(0);
        po.setState(1);
        po.setValidHourPeriod(10);
        po.setCleanHourPeriod(20);
        po.setConvertCoefficient(2.4);
        po.setFlowSpeed(11);
        po.setComment("comment_001");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new ToppingPO();
        po.setTenantCode("tenant_002");
        po.setToppingCode("topping_002");
        po.setToppingName("topping_name_002");
        po.setToppingTypeCode("topping_type_002");
        po.setMeasureUnit(1);
        po.setState(3);
        po.setValidHourPeriod(111);
        po.setCleanHourPeriod(222);
        po.setConvertCoefficient(4.4);
        po.setFlowSpeed(44);
        po.setComment("comment_004");
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
        ToppingMapper mapper = sqlSession.getMapper(ToppingMapper.class);

        List<ToppingPO> list = mapper.selectList("tenant_001");
        for (ToppingPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        ToppingPO po = mapper.selectOne("tenant_001", "deploy_001", null);
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        ToppingMapper mapper = sqlSession.getMapper(ToppingMapper.class);

        ToppingPO po = new ToppingPO();
        po.setTenantCode("tenant_002");
        po.setToppingCode("topping_002");
        po.setMeasureUnit(99);
        po.setState(33);
        mapper.update(po);

        sqlSession.commit();
        sqlSession.close();
    }

//    public static void testBySpring() {
//        ApplicationContext context = startBySpring();
//
//        TeaToppingMapper mapper =
//                (TeaToppingMapper) context.getBean(TeaToppingMapper.class);
//        TeaToppingPOjo TeaToppingPOjo = mapper.getOne("machine_001");
//        System.out.printf("DaoDemo#testBySpring hotelGuestPojo=%s\n", TeaToppingPOjo);
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
