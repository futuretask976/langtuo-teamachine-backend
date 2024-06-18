package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.TeaSpecMapper;
import com.langtuo.teamachine.dao.po.TeaSpecPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class TeaSpecPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaSpecMapper mapper = sqlSession.getMapper(TeaSpecMapper.class);

        TeaSpecPO po = null;

        po = new TeaSpecPO();
        po.setTenantCode("tenant_001");
        po.setSpecCode("spec_001");
        po.setSpecName("spec_name_001");
        po.setState(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new TeaSpecPO();
        po.setTenantCode("tenant_002");
        po.setSpecCode("spec_002");
        po.setSpecName("spec_name_002");
        po.setState(1);
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
        TeaSpecMapper mapper = sqlSession.getMapper(TeaSpecMapper.class);

        List<TeaSpecPO> list = mapper.selectList();
        for (TeaSpecPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        TeaSpecPO po = mapper.selectOne("tenant_001", "deploy_001");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaSpecMapper mapper = sqlSession.getMapper(TeaSpecMapper.class);

        TeaSpecPO po = new TeaSpecPO();
        po.setTenantCode("tenant_002");
        po.setSpecCode("spec_002");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k11", "v99999");
            put("k22", "v8888");
        }});
        mapper.update(po);

        sqlSession.commit();
        sqlSession.close();
    }

//    public static void testBySpring() {
//        ApplicationContext context = startBySpring();
//
//        TeaSpecMapper mapper =
//                (TeaSpecMapper) context.getBean(TeaSpecMapper.class);
//        TeaSpecPOjo TeaSpecPOjo = mapper.getOne("machine_001");
//        System.out.printf("DaoDemo#testBySpring hotelGuestPojo=%s\n", TeaSpecPOjo);
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
