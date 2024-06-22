package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.WarningRuleMapper;
import com.langtuo.teamachine.dao.po.WarningRulePO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class WarningRuleTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        WarningRuleMapper mapper = sqlSession.getMapper(WarningRuleMapper.class);

        WarningRulePO po = null;

        po = new WarningRulePO();
        po.setTenantCode("tenant_001");
        po.setWarningRuleCode("warning_001");
        po.setWarningRuleName("预警规则001");
        po.setWarningType(0);
        po.setWarningContent("这里是预警");
        po.setThresholdType(0);
        po.setThreshold(20);
        po.setComment("松江的门店");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new WarningRulePO();
        po.setTenantCode("tenant_001");
        po.setWarningRuleCode("warning_003");
        po.setWarningRuleName("预警规则003");
        po.setWarningType(3);
        po.setWarningContent("这里是预警");
        po.setThresholdType(4);
        po.setThreshold(50);
        po.setComment("松江的门店3333");
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
        WarningRuleMapper mapper = sqlSession.getMapper(WarningRuleMapper.class);

        List<WarningRulePO> list = mapper.selectList();
        for (WarningRulePO po : list) {
            System.out.printf("$$$$$ list->po: %s\n", po);
        }

        WarningRulePO po = mapper.selectOne("tenant_001", "warning_003");
        System.out.printf("$$$$$ po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        WarningRuleMapper mapper = sqlSession.getMapper(WarningRuleMapper.class);

        WarningRulePO po = new WarningRulePO();
        po.setTenantCode("tenant_001");
        po.setWarningRuleCode("warning_003");
        po.setWarningRuleName("预警规则003444444");
        po.setWarningType(7777);
        po.setWarningContent("这里是预警44444");
        po.setThresholdType(555);
        po.setThreshold(99999);
        po.setComment("松江的门店333355555");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1555");
            put("k2", "v25555");
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
