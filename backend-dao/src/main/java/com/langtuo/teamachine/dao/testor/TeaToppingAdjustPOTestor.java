package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.TeaToppingAdjustMapper;
import com.langtuo.teamachine.dao.po.TeaToppingAdjustPO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class TeaToppingAdjustPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaToppingAdjustMapper mapper = sqlSession.getMapper(TeaToppingAdjustMapper.class);

        TeaToppingAdjustPO po = null;

        po = new TeaToppingAdjustPO();
        po.setTenantCode("tenant_001");
        po.setTeaUnitCode("unit_001");
        po.setToppingCode("topping_001");
        po.setTeaCode("tea_001");
        po.setAdjustAmount(222);
        mapper.insert(po);

        po = new TeaToppingAdjustPO();
        po.setTenantCode("tenant_002");
        po.setTeaUnitCode("unit_002");
        po.setToppingCode("topping_002");
        po.setTeaCode("tea_002");
        po.setAdjustAmount(333);
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaToppingAdjustMapper mapper = sqlSession.getMapper(TeaToppingAdjustMapper.class);

        List<TeaToppingAdjustPO> list = mapper.selectList();
        for (TeaToppingAdjustPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        TeaToppingAdjustPO po = mapper.selectOne("tenant_002", "unit_002");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaToppingAdjustMapper mapper = sqlSession.getMapper(TeaToppingAdjustMapper.class);

        TeaToppingAdjustPO po = new TeaToppingAdjustPO();
        po.setTenantCode("tenant_002");
        po.setTeaUnitCode("unit_002");
        po.setToppingCode("topping_002222222");
        po.setTeaCode("tea_00222222");
        po.setAdjustAmount(99999);
        mapper.update(po);

        sqlSession.commit();
        sqlSession.close();
    }

//    public static void testBySpring() {
//        ApplicationContext context = startBySpring();
//
//        TeaToppingAdjustMapper mapper =
//                (TeaToppingAdjustMapper) context.getBean(TeaToppingAdjustMapper.class);
//        TeaToppingAdjustPOjo TeaToppingAdjustPOjo = mapper.getOne("machine_001");
//        System.out.printf("DaoDemo#testBySpring hotelGuestPojo=%s\n", TeaToppingAdjustPOjo);
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
