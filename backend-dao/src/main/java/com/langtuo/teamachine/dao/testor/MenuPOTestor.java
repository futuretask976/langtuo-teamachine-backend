package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.MenuMapper;
import com.langtuo.teamachine.dao.po.MenuPO;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MenuPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MenuMapper mapper = sqlSession.getMapper(MenuMapper.class);

        MenuPO po = null;

        po = new MenuPO();
        po.setTenantCode("tenant_001");
        po.setMenuCode("menu_code_001");
        po.setMenuName("菜单_001");
        po.setImgLink("https://hushangayi.jpg");
        po.setValidFrom(new Date());
        po.setComment("这里是备注001");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new MenuPO();
        po.setTenantCode("tenant_002");
        po.setMenuCode("menu_code_002");
        po.setMenuName("菜单_002");
        po.setImgLink("https://hushangayi.jpg");
        po.setValidFrom(new Date());
        po.setComment("这里是备注002");
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
        MenuMapper mapper = sqlSession.getMapper(MenuMapper.class);

        List<MenuPO> list = mapper.selectList();
        for (MenuPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        MenuPO po = mapper.selectOne("tenant_002", "menu_code_002");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MenuMapper mapper = sqlSession.getMapper(MenuMapper.class);

        MenuPO po = new MenuPO();
        po.setTenantCode("tenant_002");
        po.setMenuCode("menu_code_002");
        po.setMenuName("菜单_00288888888");
        po.setImgLink("https://hushangayi.jpg88888888");
        po.setValidFrom(new Date());
        po.setComment("这里是备注00288");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v188888");
            put("k2", "v28888");
        }});
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
