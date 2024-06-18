package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.TeaSpecSubMapper;
import com.langtuo.teamachine.dao.po.TeaSpecSubPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class TeaSpecSubPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaSpecSubMapper mapper = sqlSession.getMapper(TeaSpecSubMapper.class);

        TeaSpecSubPO po = null;

        po = new TeaSpecSubPO();
        po.setTenantCode("tenant_001");
        po.setSpecCode("spec_001");
        po.setSpecSubCode("spec_sub_code_001");
        po.setSpecSubName("spec_sub_name_001");
        po.setOuterSpecSubCode("outer_spec_sub_code_001");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new TeaSpecSubPO();
        po.setTenantCode("tenant_002");
        po.setSpecCode("spec_002");
        po.setSpecSubCode("spec_sub_code_002");
        po.setSpecSubName("spec_sub_name_002");
        po.setOuterSpecSubCode("outer_spec_sub_code_002");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k11", "v1");
            put("k22", "v2");
        }});
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaSpecSubMapper mapper = sqlSession.getMapper(TeaSpecSubMapper.class);

        List<TeaSpecSubPO> list = mapper.selectList();
        for (TeaSpecSubPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        TeaSpecSubPO po = mapper.selectOne("tenant_001", "spec_sub_code_001");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaSpecSubMapper mapper = sqlSession.getMapper(TeaSpecSubMapper.class);

        TeaSpecSubPO po = new TeaSpecSubPO();
        po.setTenantCode("tenant_002");
        po.setSpecSubCode("spec_sub_code_002");
        po.setOuterSpecSubCode("outer_spec_sub_code_00277777");
        mapper.update(po);

        sqlSession.commit();
        sqlSession.close();
    }

//    public static void testBySpring() {
//        ApplicationContext context = startBySpring();
//
//        TeaSpecSubMapper mapper =
//                (TeaSpecSubMapper) context.getBean(TeaSpecSubMapper.class);
//        TeaSpecSubPOjo TeaSpecSubPOjo = mapper.getOne("machine_001");
//        System.out.printf("DaoDemo#testBySpring hotelGuestPojo=%s\n", TeaSpecSubPOjo);
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
