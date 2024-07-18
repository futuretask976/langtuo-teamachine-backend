package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.deviceset.MachineDeployMapper;
import com.langtuo.teamachine.dao.po.deviceset.MachineDeployPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class MachineDeployPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineDeployMapper mapper = sqlSession.getMapper(MachineDeployMapper.class);

        MachineDeployPO po = null;

        po = new MachineDeployPO();
        po.setTenantCode("tenant_001");
        po.setDeployCode("deploy_001");
        po.setModelCode("model_001");
        po.setMachineCode("machine_001");
        po.setShopCode("shop_001");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new MachineDeployPO();
        po.setTenantCode("tenant_002");
        po.setDeployCode("deploy_002");
        po.setModelCode("model_002");
        po.setMachineCode("machine_002");
        po.setShopCode("shop_002");
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
        MachineDeployMapper mapper = sqlSession.getMapper(MachineDeployMapper.class);

        List<MachineDeployPO> list = mapper.selectList("tenant_001");
        for (MachineDeployPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        MachineDeployPO po = mapper.selectOne("tenant_001", "deploy_001");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineDeployMapper mapper = sqlSession.getMapper(MachineDeployMapper.class);

        MachineDeployPO po = new MachineDeployPO();
        po.setTenantCode("tenant_002");
        po.setDeployCode("deploy_002");
        po.setShopCode("shop_002444444");
        mapper.update(po);

        sqlSession.commit();
        sqlSession.close();
    }

//    public static void testBySpring() {
//        ApplicationContext context = startBySpring();
//
//        MachineDeployMapper mapper =
//                (MachineDeployMapper) context.getBean(MachineDeployMapper.class);
//        MachineDeployPOjo MachineDeployPOjo = mapper.getOne("machine_001");
//        System.out.printf("DaoDemo#testBySpring hotelGuestPojo=%s\n", MachineDeployPOjo);
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
