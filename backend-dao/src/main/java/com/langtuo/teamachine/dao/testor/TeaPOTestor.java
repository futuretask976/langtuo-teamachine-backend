package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.drinkset.TeaMapper;
import com.langtuo.teamachine.dao.po.drinkset.TeaPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class TeaPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaMapper mapper = sqlSession.getMapper(TeaMapper.class);

        TeaPO po = null;

        po = new TeaPO();
        po.setTenantCode("tenant_001");
        po.setTeaTypeCode("tea_type_001");
        po.setTeaCode("tea_code_001");
        po.setTeaName("tea_name_001");
        po.setOuterTeaCode("outer_tea_code_001");
        po.setState(1);
        po.setComment("comment_001");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new TeaPO();
        po.setTenantCode("tenant_002");
        po.setTeaTypeCode("tea_type_002");
        po.setTeaCode("tea_code_002");
        po.setTeaName("tea_name_002");
        po.setOuterTeaCode("outer_tea_code_002");
        po.setState(1);
        po.setComment("comment_002");
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
        TeaMapper mapper = sqlSession.getMapper(TeaMapper.class);

        List<TeaPO> list = mapper.selectList("tenant_002");
        for (TeaPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        TeaPO po = mapper.selectOne("tenant_002", "tea_code_002", null);
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaMapper mapper = sqlSession.getMapper(TeaMapper.class);

        TeaPO po = new TeaPO();
        po.setTenantCode("tenant_002");
        po.setTeaCode("tea_code_002");
        po.setOuterTeaCode("outer_tea_code_0023333333");
        mapper.update(po);

        sqlSession.commit();
        sqlSession.close();
    }

//    public static void testBySpring() {
//        ApplicationContext context = startBySpring();
//
//        TeaMapper mapper =
//                (TeaMapper) context.getBean(TeaMapper.class);
//        TeaPOjo TeaPOjo = mapper.getOne("machine_001");
//        System.out.printf("DaoDemo#testBySpring hotelGuestPojo=%s\n", TeaPOjo);
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
