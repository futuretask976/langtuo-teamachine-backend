package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.MenuDispatchMapper;
import com.langtuo.teamachine.dao.po.MenuDispatchPO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MenuDispatchTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        delete();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MenuDispatchMapper mapper = sqlSession.getMapper(MenuDispatchMapper.class);

        MenuDispatchPO po = null;

        po = new MenuDispatchPO();
        po.setTenantCode("tenant_001");
        po.setMenuCode("menu_001");
        po.setShopCode("shop_001");
        mapper.insert(po);

        po = new MenuDispatchPO();
        po.setTenantCode("tenant_002");
        po.setMenuCode("menu_002");
        po.setShopCode("shop_002");
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MenuDispatchMapper mapper = sqlSession.getMapper(MenuDispatchMapper.class);

        List<MenuDispatchPO> list = mapper.selectList();
        for (MenuDispatchPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        MenuDispatchPO po = mapper.selectOne("tenant_002", "menu_002", "shop_002");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void delete() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MenuDispatchMapper mapper = sqlSession.getMapper(MenuDispatchMapper.class);

        mapper.delete("tenant_001", "menu_001", "shop_001");

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
