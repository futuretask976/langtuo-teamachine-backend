package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.PermActMapper;
import com.langtuo.teamachine.dao.po.PermActPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class PermActPOTestor {
    public static void main(String args[]) {
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        PermActMapper mapper = sqlSession.getMapper(PermActMapper.class);

        PermActPO po = null;

        po = new PermActPO();
        po.setPermActCode("perm_act_tenant_manage");
        po.setPermActName("租户管理");
        po.setPermActGroupCode("perm_act_group_user");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_role_manage");
        po.setPermActName("角色管理");
        po.setPermActGroupCode("perm_act_group_user");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_org_struc_manage");
        po.setPermActName("组织架构管理");
        po.setPermActGroupCode("perm_act_group_user");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_admin_manage");
        po.setPermActName("管理员管理");
        po.setPermActGroupCode("perm_act_group_user");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_shop_manage");
        po.setPermActName("店铺管理");
        po.setPermActGroupCode("perm_act_group_shop");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_shop_group_manage");
        po.setPermActName("店铺组管理");
        po.setPermActGroupCode("perm_act_group_shop");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_machine_deploy_manage");
        po.setPermActName("预部署管理");
        po.setPermActGroupCode("perm_act_group_machine");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_machine_manage");
        po.setPermActName("设备管理");
        po.setPermActGroupCode("perm_act_group_machine");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_machine_detail_manage");
        po.setPermActName("设备详情管理");
        po.setPermActGroupCode("perm_act_group_machine");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_topping_type_manage");
        po.setPermActName("物料类型管理");
        po.setPermActGroupCode("perm_act_group_tea");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_topping_manage");
        po.setPermActName("物料管理");
        po.setPermActGroupCode("perm_act_group_tea");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_spec_manage");
        po.setPermActName("规格管理");
        po.setPermActGroupCode("perm_act_group_tea");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_tea_type_manage");
        po.setPermActName("茶饮类型管理");
        po.setPermActGroupCode("perm_act_group_tea");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_tea_manage");
        po.setPermActName("茶饮管理");
        po.setPermActGroupCode("perm_act_group_tea");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_series_manage");
        po.setPermActName("系列管理");
        po.setPermActGroupCode("perm_act_group_menu");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_menu_manage");
        po.setPermActName("菜单管理");
        po.setPermActGroupCode("perm_act_group_menu");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_menu_deploy_manage");
        po.setPermActName("菜单下发管理");
        po.setPermActGroupCode("perm_act_group_menu");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_open_rule_manage");
        po.setPermActName("营业准备管理");
        po.setPermActGroupCode("perm_act_group_food_safe_rule");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_close_rule_manage");
        po.setPermActName("打烊准备管理");
        po.setPermActGroupCode("perm_act_group_food_safe_rule");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_clean_rule_manage");
        po.setPermActName("清洗规则管理");
        po.setPermActGroupCode("perm_act_group_food_safe_rule");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_warning_rule_manage");
        po.setPermActName("预警规则管理");
        po.setPermActGroupCode("perm_act_group_food_safe_rule");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_invalid_topping_rec_manage");
        po.setPermActName("废料记录管理");
        po.setPermActGroupCode("perm_act_group_statistics");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_supply_topping_rec_manage");
        po.setPermActName("补料记录管理");
        po.setPermActGroupCode("perm_act_group_statistics");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_clean_rec_manage");
        po.setPermActName("清洗记录管理");
        po.setPermActGroupCode("perm_act_group_statistics");
        mapper.insert(po);

        po = new PermActPO();
        po.setPermActCode("perm_act_order_rec_manage");
        po.setPermActName("订单记录管理");
        po.setPermActGroupCode("perm_act_group_statistics");
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        PermActMapper mapper = sqlSession.getMapper(PermActMapper.class);

        List<PermActPO> list = mapper.selectList();
        for (PermActPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        PermActPO po = mapper.selectOne("100001");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        PermActMapper mapper = sqlSession.getMapper(PermActMapper.class);

        PermActPO po = new PermActPO();
        po.setPermActCode("perm_act_clean_rec_manage");
        po.setPermActName("清洗记录管理2222");
        po.setPermActGroupCode("perm_act_group_statistics");
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
