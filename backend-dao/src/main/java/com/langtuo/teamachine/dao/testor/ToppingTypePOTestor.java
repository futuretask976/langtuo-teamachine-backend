package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.ToppingTypeMapper;
import com.langtuo.teamachine.dao.po.ToppingTypePO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class ToppingTypePOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        ToppingTypeMapper mapper = sqlSession.getMapper(ToppingTypeMapper.class);

        ToppingTypePO po = null;

        po = new ToppingTypePO();
        po.setTenantCode("tenant_001");
        po.setToppingTypeCode("topping_type_001");
        po.setToppingTypeName("topping_name_001");
        po.setState(1);
        po.setComment("comment_001");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new ToppingTypePO();
        po.setTenantCode("tenant_002");
        po.setToppingTypeCode("topping_type_002");
        po.setToppingTypeName("topping_name_002");
        po.setState(1);
        po.setComment("comment_002");
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
        ToppingTypeMapper mapper = sqlSession.getMapper(ToppingTypeMapper.class);

        List<ToppingTypePO> list = mapper.selectList("tenant_001");
        for (ToppingTypePO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        ToppingTypePO po = mapper.selectOne("tenant_001", "deploy_001", null);
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        ToppingTypeMapper mapper = sqlSession.getMapper(ToppingTypeMapper.class);

        ToppingTypePO po = new ToppingTypePO();
        po.setTenantCode("tenant_002");
        po.setToppingTypeCode("topping_type_002");
        po.setToppingTypeName("topping_name_002666666666");
        po.setState(0);
        mapper.update(po);

        sqlSession.commit();
        sqlSession.close();
    }

//    public static void testBySpring() {
//        ApplicationContext context = startBySpring();
//
//        TeaToppingTypeMapper mapper =
//                (TeaToppingTypeMapper) context.getBean(TeaToppingTypeMapper.class);
//        TeaToppingTypePOjo TeaToppingTypePOjo = mapper.getOne("machine_001");
//        System.out.printf("DaoDemo#testBySpring hotelGuestPojo=%s\n", TeaToppingTypePOjo);
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
