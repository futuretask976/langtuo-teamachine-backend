package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.TeaToppingNormalMapper;
import com.langtuo.teamachine.dao.po.TeaToppingNormalPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class TeaToppingNormalPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaToppingNormalMapper mapper = sqlSession.getMapper(TeaToppingNormalMapper.class);

        TeaToppingNormalPO po = null;

        po = new TeaToppingNormalPO();
        po.setTenantCode("tenant_001");
        po.setToppingCode("topping_001");
        po.setTeaCode("tea_001");
        po.setAmount(1);
        mapper.insert(po);

        po = new TeaToppingNormalPO();
        po.setTenantCode("tenant_002");
        po.setToppingCode("topping_002");
        po.setTeaCode("tea_002");
        po.setAmount(222);
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaToppingNormalMapper mapper = sqlSession.getMapper(TeaToppingNormalMapper.class);

        List<TeaToppingNormalPO> list = mapper.selectList();
        for (TeaToppingNormalPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        TeaToppingNormalPO po = mapper.selectOne("tenant_002", "tea_002", "topping_002");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaToppingNormalMapper mapper = sqlSession.getMapper(TeaToppingNormalMapper.class);

        TeaToppingNormalPO po = new TeaToppingNormalPO();
        po.setTenantCode("tenant_002");
        po.setToppingCode("topping_002");
        po.setTeaCode("tea_002");
        po.setAmount(333333333);
        mapper.update(po);

        sqlSession.commit();
        sqlSession.close();
    }

//    public static void testBySpring() {
//        ApplicationContext context = startBySpring();
//
//        TeaToppingNormalMapper mapper =
//                (TeaToppingNormalMapper) context.getBean(TeaToppingNormalMapper.class);
//        TeaToppingNormalPOjo TeaToppingNormalPOjo = mapper.getOne("machine_001");
//        System.out.printf("DaoDemo#testBySpring hotelGuestPojo=%s\n", TeaToppingNormalPOjo);
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
