package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.drinkset.TeaTypeMapper;
import com.langtuo.teamachine.dao.po.drinkset.TeaTypePO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class TeaTypePOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaTypeMapper mapper = sqlSession.getMapper(TeaTypeMapper.class);

        TeaTypePO po = null;

        po = new TeaTypePO();
        po.setTenantCode("tenant_001");
        po.setTeaTypeCode("tea_type_001");
        po.setTeaTypeName("tea_name_001");
        po.setState(1);
        po.setComment("comment_001");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new TeaTypePO();
        po.setTenantCode("tenant_002");
        po.setTeaTypeCode("tea_type_002");
        po.setTeaTypeName("tea_name_002");
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
        TeaTypeMapper mapper = sqlSession.getMapper(TeaTypeMapper.class);

        List<TeaTypePO> list = mapper.selectList("tenant_001");
        for (TeaTypePO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        TeaTypePO po = mapper.selectOne("tenant_001", "tea_type_001", null);
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TeaTypeMapper mapper = sqlSession.getMapper(TeaTypeMapper.class);

        TeaTypePO po = new TeaTypePO();
        po.setTenantCode("tenant_002");
        po.setTeaTypeCode("tea_type_002");
        po.setState(88);
        mapper.update(po);

        sqlSession.commit();
        sqlSession.close();
    }

//    public static void testBySpring() {
//        ApplicationContext context = startBySpring();
//
//        TeaTypeMapper mapper =
//                (TeaTypeMapper) context.getBean(TeaTypeMapper.class);
//        TeaTypePOjo TeaTypePOjo = mapper.getOne("machine_001");
//        System.out.printf("DaoDemo#testBySpring hotelGuestPojo=%s\n", TeaTypePOjo);
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
