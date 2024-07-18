package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.menuset.MenuSeriesRelMapper;
import com.langtuo.teamachine.dao.po.menuset.MenuSeriesRelPO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MenuSeriesRelPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        delete();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MenuSeriesRelMapper mapper = sqlSession.getMapper(MenuSeriesRelMapper.class);

        MenuSeriesRelPO po = null;

        po = new MenuSeriesRelPO();
        po.setTenantCode("tenant_001");
        po.setMenuCode("menu_001");
        po.setSeriesCode("series_001");
        mapper.insert(po);

        po = new MenuSeriesRelPO();
        po.setTenantCode("tenant_002");
        po.setMenuCode("menu_002");
        po.setSeriesCode("series_002");
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MenuSeriesRelMapper mapper = sqlSession.getMapper(MenuSeriesRelMapper.class);

        List<MenuSeriesRelPO> list = mapper.selectList("tenant_002", "menu_002");
        for (MenuSeriesRelPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        MenuSeriesRelPO po = mapper.selectOne("tenant_002", "series_002", "menu_002");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void delete() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MenuSeriesRelMapper mapper = sqlSession.getMapper(MenuSeriesRelMapper.class);

        mapper.delete("tenant_001", "menu_001");

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
