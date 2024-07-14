package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.TeaUnitMapper;
import com.langtuo.teamachine.dao.po.TeaUnitPO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class TeaUnitTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaUnitMapper mapper = sqlSession.getMapper(TeaUnitMapper.class);

        TeaUnitPO po = null;

        po = new TeaUnitPO();
        po.setTenantCode("tenant_001");
        po.setTeaUnitCode("unit_001");
        po.setTeaCode("tea_001");
        mapper.insert(po);

        po = new TeaUnitPO();
        po.setTenantCode("tenant_002");
        po.setTeaUnitCode("unit_002");
        po.setTeaCode("tea_002");
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaUnitMapper mapper = sqlSession.getMapper(TeaUnitMapper.class);

        List<TeaUnitPO> list = mapper.selectList("tenant_001", "unit_001");
        for (TeaUnitPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        TeaUnitPO po = mapper.selectOne("tenant_001", "unit_001", null, null);
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaUnitMapper mapper = sqlSession.getMapper(TeaUnitMapper.class);

        TeaUnitPO po = new TeaUnitPO();
        po.setTenantCode("tenant_002");
        po.setTeaUnitCode("unit_002");
        po.setTeaCode("tea_0026666666");
        mapper.update(po);

        sqlSession.commit();
        sqlSession.close();
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
