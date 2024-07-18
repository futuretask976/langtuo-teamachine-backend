package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.deviceset.MachineMapper;
import com.langtuo.teamachine.dao.po.deviceset.MachinePO;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MachinePOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
//        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineMapper mapper = sqlSession.getMapper(MachineMapper.class);

        MachinePO po = null;

        po = new MachinePO();
        po.setTenantCode("tenant_001");
        po.setMachineCode("machine_001");
        po.setMachineName("machineName_001");
        po.setScreenCode("screen_001");
        po.setElecBoardCode("elec_001");
        po.setModelCode("model_001");
        po.setState(1);
        po.setValidUntil(new Date());
        po.setMaintainUntil(new Date());
        po.setTenantCode("tenant_001");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new MachinePO();
        po.setTenantCode("tenant_002");
        po.setMachineCode("machine_002");
        po.setMachineName("machineName_002");
        po.setScreenCode("screen_002");
        po.setElecBoardCode("elec_002");
        po.setModelCode("model_002");
        po.setState(1);
        po.setValidUntil(new Date());
        po.setMaintainUntil(new Date());
        po.setTenantCode("tenant_002");
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
        MachineMapper mapper = sqlSession.getMapper(MachineMapper.class);

        List<MachinePO> list = mapper.selectList("tenant_001");
        for (MachinePO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        MachinePO po = mapper.selectOne("tenant_001", "mahcine_001", null);
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineMapper mapper = sqlSession.getMapper(MachineMapper.class);

        MachinePO po = new MachinePO();
        po.setTenantCode("tenant_002");
        po.setMachineCode("machine_002");
        po.setMachineName("machineName_0022222");
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
