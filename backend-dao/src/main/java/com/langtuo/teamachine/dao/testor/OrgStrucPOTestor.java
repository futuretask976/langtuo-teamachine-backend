package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.OrgStrucMapper;
import com.langtuo.teamachine.dao.po.OrgStrucPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class OrgStrucPOTestor {
    public static void main(String args[]) {
        insert();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        OrgStrucMapper mapper = sqlSession.getMapper(OrgStrucMapper.class);

        OrgStrucPO po = null;

        po = new OrgStrucPO();
        po.setTenantCode("tenant_001");
        po.setOrgName("总公司");
        po.setParentOrgName(null);
        mapper.insert(po);

        po = new OrgStrucPO();
        po.setTenantCode("tenant_001");
        po.setOrgName("江苏分公司");
        po.setParentOrgName("总公司");
        mapper.insert(po);

        po = new OrgStrucPO();
        po.setTenantCode("tenant_001");
        po.setOrgName("南京分公司");
        po.setParentOrgName("江苏分公司");
        mapper.insert(po);

        po = new OrgStrucPO();
        po.setTenantCode("tenant_001");
        po.setOrgName("北京分公司");
        po.setParentOrgName("总公司");
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        OrgStrucMapper mapper = sqlSession.getMapper(OrgStrucMapper.class);

        List<OrgStrucPO> list = mapper.selectList("tenant_001");
        for (OrgStrucPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        OrgStrucPO po = mapper.selectOne("tenant_001", "总公司");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        OrgStrucMapper mapper = sqlSession.getMapper(OrgStrucMapper.class);

        OrgStrucPO po = new OrgStrucPO();
        po.setTenantCode("tenant_001");
        po.setOrgName("江苏分公司");
        po.setParentOrgName("总公司222");
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
