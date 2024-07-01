package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.ShopGroupMapper;
import com.langtuo.teamachine.dao.po.ShopGroupPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class ShopGroupPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        ShopGroupMapper mapper = sqlSession.getMapper(ShopGroupMapper.class);

        ShopGroupPO po = null;

        po = new ShopGroupPO();
        po.setTenantCode("tenant_001");
        po.setShopGroupCode("shopGroup_001");
        po.setShopGroupName("松江门店组");
        po.setComment("松江的门店组");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new ShopGroupPO();
        po.setTenantCode("tenant_002");
        po.setShopGroupCode("shopGroup_002");
        po.setShopGroupName("奉贤门店组");
        po.setComment("奉贤的门店组");
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
        ShopGroupMapper mapper = sqlSession.getMapper(ShopGroupMapper.class);

        List<ShopGroupPO> list = mapper.selectList("tenant_001");
        for (ShopGroupPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        ShopGroupPO po = mapper.selectOne("tenant_001", "shopGroup_001");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        ShopGroupMapper mapper = sqlSession.getMapper(ShopGroupMapper.class);

        ShopGroupPO po = new ShopGroupPO();
        po.setTenantCode("tenant_002");
        po.setShopGroupCode("shopGroup_002");
        po.setShopGroupName("奉贤区门店组");
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
