package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.ShopMapper;
import com.langtuo.teamachine.dao.po.ShopPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class ShopPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        ShopMapper mapper = sqlSession.getMapper(ShopMapper.class);

        ShopPO po = null;

        po = new ShopPO();
        po.setTenantCode("tenant_001");
        po.setShopGroupCode("shopGroup_001");
        po.setShopCode("shop_001");
        po.setShopName("松江1号店");
        po.setShopType(0);
        po.setOrgName("上海分公司");
        po.setComment("松江的门店");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new ShopPO();
        po.setTenantCode("tenant_002");
        po.setShopGroupCode("shopGroup_002");
        po.setShopCode("shop_002");
        po.setShopName("松江2号店");
        po.setShopType(1);
        po.setOrgName("上海分公司");
        po.setComment("松江的门店2");
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
        ShopMapper mapper = sqlSession.getMapper(ShopMapper.class);

        List<ShopPO> list = mapper.selectList("tenant_001");
        for (ShopPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        ShopPO po = mapper.selectOne("tenant_001", "shopGroup_001");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        ShopMapper mapper = sqlSession.getMapper(ShopMapper.class);

        ShopPO po = new ShopPO();
        po.setTenantCode("tenant_002");
        po.setShopCode("shop_002");
        po.setComment("松江的门店233333333");
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
