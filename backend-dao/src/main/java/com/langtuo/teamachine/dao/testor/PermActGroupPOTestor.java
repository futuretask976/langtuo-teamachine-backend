package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.PermActGroupMapper;
import com.langtuo.teamachine.dao.po.PermActGroupPO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class PermActGroupPOTestor {
    public static void main(String args[]) {
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        PermActGroupMapper mapper = sqlSession.getMapper(PermActGroupMapper.class);

        PermActGroupPO po = null;

        po = new PermActGroupPO();
        po.setPermActGroupCode("perm_act_group_user");
        po.setPermActGroupName("用户");
        mapper.insert(po);

        po = new PermActGroupPO();
        po.setPermActGroupCode("perm_act_group_shop");
        po.setPermActGroupName("店铺");
        mapper.insert(po);

        po = new PermActGroupPO();
        po.setPermActGroupCode("perm_act_group_machine");
        po.setPermActGroupName("设备");
        mapper.insert(po);

        po = new PermActGroupPO();
        po.setPermActGroupCode("perm_act_group_tea");
        po.setPermActGroupName("饮品");
        mapper.insert(po);

        po = new PermActGroupPO();
        po.setPermActGroupCode("perm_act_group_menu");
        po.setPermActGroupName("菜单");
        mapper.insert(po);

        po = new PermActGroupPO();
        po.setPermActGroupCode("perm_act_group_food_safe_rule");
        po.setPermActGroupName("食安规则");
        mapper.insert(po);

        po = new PermActGroupPO();
        po.setPermActGroupCode("perm_act_group_statistics");
        po.setPermActGroupName("日常报表");
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        PermActGroupMapper mapper = sqlSession.getMapper(PermActGroupMapper.class);

        List<PermActGroupPO> list = mapper.selectList();
        for (PermActGroupPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        PermActGroupPO po = mapper.selectOne("100001");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        PermActGroupMapper mapper = sqlSession.getMapper(PermActGroupMapper.class);

        PermActGroupPO po = new PermActGroupPO();
        po.setPermActGroupCode("perm_act_group_user");
        po.setPermActGroupName("用户3333");
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
