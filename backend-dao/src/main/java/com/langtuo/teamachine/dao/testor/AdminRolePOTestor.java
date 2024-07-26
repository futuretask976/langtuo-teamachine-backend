package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.userset.RoleMapper;
import com.langtuo.teamachine.dao.po.userset.RolePO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class AdminRolePOTestor {
    public static void main(String args[]) {
//        insert();
        select();
//        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        RoleMapper mapper = sqlSession.getMapper(RoleMapper.class);

        RolePO po = null;

        po = new RolePO();
        po.setRoleCode("role_sys_super_admin");
        po.setRoleName("系统超级管理员");
        po.setComment("系统超级管理员");
        po.setTenantCode("tenant_001");
        po.setExtraInfo(new HashMap<String, String>(){{}});
        mapper.insert(po);

        po = new RolePO();
        po.setRoleCode("role_sys_admin");
        po.setRoleName("系统管理员");
        po.setComment("系统管理员");
        po.setTenantCode("tenant_001");
        po.setExtraInfo(new HashMap<String, String>(){{}});
        mapper.insert(po);

        po = new RolePO();
        po.setRoleCode("role_tenant_super_admin");
        po.setRoleName("租户超级管理员");
        po.setComment("租户超级管理员");
        po.setTenantCode("tenant_001");
        po.setExtraInfo(new HashMap<String, String>(){{}});
        mapper.insert(po);

        po = new RolePO();
        po.setRoleCode("role_tenant_admin");
        po.setRoleName("租户管理员");
        po.setComment("租户管理员");
        po.setTenantCode("tenant_001");
        po.setExtraInfo(new HashMap<String, String>(){{}});
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        RoleMapper mapper = sqlSession.getMapper(RoleMapper.class);

        List<RolePO> list = mapper.selectList("tenant_001");
        for (RolePO po : list) {
            System.out.printf("$$$$$ list->po: %s\n", po);
        }

        RolePO po = mapper.selectOne("tenant_001", "role_tenant_admin");
        System.out.printf("$$$$$ po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        RoleMapper mapper = sqlSession.getMapper(RoleMapper.class);

        RolePO po = new RolePO();
        po.setRoleCode("role_tenant_admin");
        po.setTenantCode("tenant_001");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("aaa", "bbb");
        }});
        mapper.update(po);

        sqlSession.commit();
        sqlSession.close();
    }

//    public static void testBySpring() {
//        ApplicationContext context = startBySpring();
//
//        AdminRoleMapper mapper =
//                (AdminRoleMapper) context.getBean(AdminRoleMapper.class);
//        AdminRolePOjo AdminRolePOjo = mapper.getOne("machine_001");
//        System.out.printf("DaoDemo#testBySpring hotelGuestPojo=%s\n", AdminRolePOjo);
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
