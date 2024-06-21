package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.MachineModelMapper;
import com.langtuo.teamachine.dao.po.MachineModelPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class MachineModelPOTestor {
    public static void main(String args[]) {
        insert();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineModelMapper mapper = sqlSession.getMapper(MachineModelMapper.class);

        MachineModelPO po = null;

        po = new MachineModelPO();
        po.setModelCode("model_001");
        po.setEnableFlowAll(0);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_002");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_003");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k3", "v3");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_004");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k4", "v4");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_005");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k5", "v5");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_006");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k6", "v6");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_007");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k7", "v7");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_008");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k8", "v8");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_009");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k9", "v9");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_010");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k10", "v10");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_011");
        po.setEnableFlowAll(0);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_012");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_013");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k3", "v3");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_014");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k4", "v4");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_015");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k5", "v5");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_016");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k6", "v6");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_017");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k7", "v7");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_018");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k8", "v8");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_019");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k9", "v9");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_020");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k10", "v20");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_021");
        po.setEnableFlowAll(0);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_022");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_023");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k3", "v3");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_024");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k4", "v4");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_025");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k5", "v5");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_026");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k6", "v6");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_027");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k7", "v7");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_028");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k8", "v8");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_029");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k9", "v9");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_030");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k10", "v10");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_031");
        po.setEnableFlowAll(0);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_032");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_033");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k3", "v3");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_034");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k4", "v4");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_035");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k5", "v5");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_036");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k6", "v6");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_037");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k7", "v7");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_038");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k8", "v8");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_039");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k9", "v9");
        }});
        mapper.insert(po);

        po = new MachineModelPO();
        po.setModelCode("model_040");
        po.setEnableFlowAll(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k10", "v10");
        }});
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineModelMapper mapper = sqlSession.getMapper(MachineModelMapper.class);

        List<MachineModelPO> list = mapper.selectList();
        for (MachineModelPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        MachineModelPO po = mapper.selectOne("100001");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineModelMapper mapper = sqlSession.getMapper(MachineModelMapper.class);

        MachineModelPO po = new MachineModelPO();
        po.setModelCode("model_001");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k11", "v1111");
            put("k22", "v2222");
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
