package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.userset.RoleActRelMapper;
import com.langtuo.teamachine.dao.po.userset.AdminRoleActRelPO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class AdminRoleActPOTestor {
    public static void main(String args[]) {
        insert();
//        select();
//        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        RoleActRelMapper mapper = sqlSession.getMapper(RoleActRelMapper.class);

        AdminRoleActRelPO po = null;

        po = new AdminRoleActRelPO();
        po.setTenantCode("tenant_001");
        po.setRoleCode("role_sys_super_admin");
        po.setPermitActCode("permit_act_tenant_mgt");
        mapper.insert(po);

        po = new AdminRoleActRelPO();
        po.setTenantCode("tenant_001");
        po.setRoleCode("role_sys_super_admin");
        po.setPermitActCode("permit_act_org_struc_mgt");
        mapper.insert(po);

        po = new AdminRoleActRelPO();
        po.setTenantCode("tenant_001");
        po.setRoleCode("role_sys_super_admin");
        po.setPermitActCode("permit_act_shop_mgt");
        mapper.insert(po);

        po = new AdminRoleActRelPO();
        po.setTenantCode("tenant_001");
        po.setRoleCode("role_tenant_admin");
        po.setPermitActCode("permit_act_machine_deploy_mgt");
        mapper.insert(po);

        po = new AdminRoleActRelPO();
        po.setTenantCode("tenant_001");
        po.setRoleCode("role_tenant_admin");
        po.setPermitActCode("permit_act_machine_detail_mgt");
        mapper.insert(po);

        po = new AdminRoleActRelPO();
        po.setTenantCode("tenant_001");
        po.setRoleCode("role_tenant_admin");
        po.setPermitActCode("permit_act_spec_mgt");
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        RoleActRelMapper mapper = sqlSession.getMapper(RoleActRelMapper.class);

        List<AdminRoleActRelPO> list = mapper.selectList("tenant_001", "role_001");
        for (AdminRoleActRelPO po : list) {
            System.out.printf("$$$$$ list->po: %s\n", po);
        }

        AdminRoleActRelPO po = mapper.selectOne("tenant_001", "role_001", "act_001");
        System.out.printf("$$$$$ po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void delete() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        RoleActRelMapper mapper = sqlSession.getMapper(RoleActRelMapper.class);

        mapper.delete("tenant_001", "role_001");

        sqlSession.commit();
        sqlSession.close();
    }

//    public static void testBySpring() {
//        ApplicationContext context = startBySpring();
//
//        AdminRoleActRelMapper mapper =
//                (AdminRoleActRelMapper) context.getBean(AdminRoleActRelMapper.class);
//        AdminRoleActRelPOjo AdminRoleActRelPOjo = mapper.getOne("machine_001");
//        System.out.printf("DaoDemo#testBySpring hotelGuestPojo=%s\n", AdminRoleActRelPOjo);
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
