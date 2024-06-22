package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.MachineModelMapper;
import com.langtuo.teamachine.dao.mapper.MachineModelPipelineMapper;
import com.langtuo.teamachine.dao.po.MachineModelPO;
import com.langtuo.teamachine.dao.po.MachineModelPipelinePO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class MachineModelPipelinePOTestor {
    public static void main(String args[]) {
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineModelPipelineMapper mapper = sqlSession.getMapper(MachineModelPipelineMapper.class);

        MachineModelPipelinePO po = null;

        po = new MachineModelPipelinePO();
        po.setModelCode("model_001");
        po.setPipelineNum(1);
        po.setEnableWarm(0);
        po.setEnableFreeze(1);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new MachineModelPipelinePO();
        po.setModelCode("model_002");
        po.setPipelineNum(2);
        po.setEnableWarm(1);
        po.setEnableFreeze(0);
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
        MachineModelPipelineMapper mapper = sqlSession.getMapper(MachineModelPipelineMapper.class);

        List<MachineModelPipelinePO> list = mapper.selectList("model_001");
        for (MachineModelPipelinePO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        MachineModelPipelinePO po = mapper.selectOne("model_001", "1");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineModelPipelineMapper mapper = sqlSession.getMapper(MachineModelPipelineMapper.class);

        MachineModelPipelinePO po = new MachineModelPipelinePO();
        po.setModelCode("model_001");
        po.setPipelineNum(1);
        po.setEnableWarm(9);
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1111");
            put("k2", "v2");
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
