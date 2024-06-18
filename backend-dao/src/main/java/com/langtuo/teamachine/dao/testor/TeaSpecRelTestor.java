package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.TeaSpecRelMapper;
import com.langtuo.teamachine.dao.po.TeaSpecRelPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class TeaSpecRelTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaSpecRelMapper mapper = sqlSession.getMapper(TeaSpecRelMapper.class);

        TeaSpecRelPO po = null;

        po = new TeaSpecRelPO();
        po.setTenantCode("tenant_001");
        po.setTeaUnitCode("unit_001");
        po.setTeaCode("tea_001");
        po.setSpecSubCode("spec_sub_001");
        mapper.insert(po);

        po = new TeaSpecRelPO();
        po.setTenantCode("tenant_002");
        po.setTeaUnitCode("unit_002");
        po.setTeaCode("tea_002");
        po.setSpecSubCode("spec_sub_002");
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaSpecRelMapper mapper = sqlSession.getMapper(TeaSpecRelMapper.class);

        List<TeaSpecRelPO> list = mapper.selectList();
        for (TeaSpecRelPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        TeaSpecRelPO po = mapper.selectOne("tenant_001", "unit_001");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaSpecRelMapper mapper = sqlSession.getMapper(TeaSpecRelMapper.class);

        TeaSpecRelPO po = new TeaSpecRelPO();
        po.setTenantCode("tenant_002");
        po.setTeaUnitCode("unit_002");
        po.setTeaCode("tea_0026666666");
        po.setSpecSubCode("spec_sub_00277777777777");
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
