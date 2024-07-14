package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.ToppingAdjustRuleMapper;
import com.langtuo.teamachine.dao.po.ToppingAdjustRulePO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class TeaToppingAdjustPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        ToppingAdjustRuleMapper mapper = sqlSession.getMapper(ToppingAdjustRuleMapper.class);

        ToppingAdjustRulePO po = null;

        po = new ToppingAdjustRulePO();
        po.setTenantCode("tenant_001");
        po.setTeaUnitCode("unit_001");
        po.setToppingCode("topping_001");
        po.setTeaCode("tea_001");
        po.setAdjustAmount(222);
        mapper.insert(po);

        po = new ToppingAdjustRulePO();
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
        ToppingAdjustRuleMapper mapper = sqlSession.getMapper(ToppingAdjustRuleMapper.class);

        List<ToppingAdjustRulePO> list = mapper.selectList("tenant_002", null,"unit_002");
        for (ToppingAdjustRulePO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        ToppingAdjustRulePO po = mapper.selectOne("tenant_002", null,"unit_002",
                null);
        System.out.printf("po: %s\n", po);

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
