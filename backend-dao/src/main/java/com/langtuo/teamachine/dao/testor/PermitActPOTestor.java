package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.userset.PermitActMapper;
import com.langtuo.teamachine.dao.po.userset.PermitActPO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class PermitActPOTestor {
    public static void main(String args[]) {
        insert();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        PermitActMapper mapper = sqlSession.getMapper(PermitActMapper.class);

        PermitActPO po = null;

        po = new PermitActPO();
        po.setPermitActCode("permit_act_tenant_mgt");
        po.setPermitActName("租户管理");
        po.setPermitActGroupCode("permit_act_group_user");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_role_mgt");
        po.setPermitActName("角色管理");
        po.setPermitActGroupCode("permit_act_group_user");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_org_struc_mgt");
        po.setPermitActName("组织架构管理");
        po.setPermitActGroupCode("permit_act_group_user");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_admin_mgt");
        po.setPermitActName("管理员管理");
        po.setPermitActGroupCode("permit_act_group_user");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_shop_mgt");
        po.setPermitActName("店铺管理");
        po.setPermitActGroupCode("permit_act_group_shop");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_shop_group_mgt");
        po.setPermitActName("店铺组管理");
        po.setPermitActGroupCode("permit_act_group_shop");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_machine_deploy_mgt");
        po.setPermitActName("预部署管理");
        po.setPermitActGroupCode("permit_act_group_machine");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_machine_mgt");
        po.setPermitActName("设备管理");
        po.setPermitActGroupCode("permit_act_group_machine");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_machine_detail_mgt");
        po.setPermitActName("设备详情管理");
        po.setPermitActGroupCode("permit_act_group_machine");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_topping_type_mgt");
        po.setPermitActName("物料类型管理");
        po.setPermitActGroupCode("permit_act_group_tea");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_topping_mgt");
        po.setPermitActName("物料管理");
        po.setPermitActGroupCode("permit_act_group_tea");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_spec_mgt");
        po.setPermitActName("规格管理");
        po.setPermitActGroupCode("permit_act_group_tea");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_tea_type_mgt");
        po.setPermitActName("茶饮类型管理");
        po.setPermitActGroupCode("permit_act_group_tea");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_tea_mgt");
        po.setPermitActName("茶饮管理");
        po.setPermitActGroupCode("permit_act_group_tea");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_series_mgt");
        po.setPermitActName("系列管理");
        po.setPermitActGroupCode("permit_act_group_menu");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_menu_mgt");
        po.setPermitActName("菜单管理");
        po.setPermitActGroupCode("permit_act_group_menu");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_menu_deploy_mgt");
        po.setPermitActName("菜单下发管理");
        po.setPermitActGroupCode("permit_act_group_menu");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_open_rule_mgt");
        po.setPermitActName("营业准备管理");
        po.setPermitActGroupCode("permit_act_group_food_safe_rule");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_close_rule_mgt");
        po.setPermitActName("打烊准备管理");
        po.setPermitActGroupCode("permit_act_group_food_safe_rule");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_clean_rule_mgt");
        po.setPermitActName("清洗规则管理");
        po.setPermitActGroupCode("permit_act_group_food_safe_rule");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_warning_rule_mgt");
        po.setPermitActName("预警规则管理");
        po.setPermitActGroupCode("permit_act_group_food_safe_rule");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_invalid_topping_rec_mgt");
        po.setPermitActName("废料记录管理");
        po.setPermitActGroupCode("permit_act_group_statistics");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_supply_topping_rec_mgt");
        po.setPermitActName("补料记录管理");
        po.setPermitActGroupCode("permit_act_group_statistics");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_clean_rec_mgt");
        po.setPermitActName("清洗记录管理");
        po.setPermitActGroupCode("permit_act_group_statistics");
        mapper.insert(po);

        po = new PermitActPO();
        po.setPermitActCode("permit_act_order_rec_mgt");
        po.setPermitActName("订单记录管理");
        po.setPermitActGroupCode("permit_act_group_statistics");
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        PermitActMapper mapper = sqlSession.getMapper(PermitActMapper.class);

        List<PermitActPO> list = mapper.selectList();
        for (PermitActPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        PermitActPO po = mapper.selectOne("100001");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        PermitActMapper mapper = sqlSession.getMapper(PermitActMapper.class);

        PermitActPO po = new PermitActPO();
        po.setPermitActCode("permit_act_clean_rec_mgt");
        po.setPermitActName("清洗记录管理2222");
        po.setPermitActGroupCode("permit_act_group_statistics");
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
