package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.CleanStepMapper;
import com.langtuo.teamachine.dao.po.CleanStepPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class CleanStepPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        CleanStepMapper mapper = sqlSession.getMapper(CleanStepMapper.class);

        CleanStepPO po = null;

        po = new CleanStepPO();
        po.setTenantCode("tenant_001");
        po.setCleanRuleCode("clean_rule_001");
        po.setStepNum(1);
        po.setCleanContent(0);
        po.setWashTime(10);
        po.setSoakTime(null);
        po.setSoakWashInterval(null);
        po.setSoakWashTime(null);
        po.setRemindTitle("排除空气");
        po.setRemindContent("排除空气进行中");
        mapper.insert(po);

        po = new CleanStepPO();
        po.setTenantCode("tenant_001");
        po.setCleanRuleCode("clean_rule_002");
        po.setStepNum(2);
        po.setCleanContent(1);
        po.setWashTime(null);
        po.setSoakTime(100);
        po.setSoakWashInterval(10);
        po.setSoakWashTime(5);
        po.setRemindTitle("浸泡");
        po.setRemindContent("浸泡进行中");
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        CleanStepMapper mapper = sqlSession.getMapper(CleanStepMapper.class);

        List<CleanStepPO> list = mapper.selectList();
        for (CleanStepPO po : list) {
            System.out.printf("$$$$$$$$$$ list->po: %s\n", po);
        }

        CleanStepPO po = mapper.selectOne("tenant_001", "clean_rule_001", 1);
        System.out.printf("$$$$$$$$$$ po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        CleanStepMapper mapper = sqlSession.getMapper(CleanStepMapper.class);

        CleanStepPO po = new CleanStepPO();
        po.setTenantCode("tenant_001");
        po.setCleanRuleCode("clean_rule_002");
        po.setStepNum(2);
        po.setCleanContent(1);
        po.setWashTime(null);
        po.setSoakTime(200);
        po.setSoakWashInterval(20);
        po.setSoakWashTime(40);
        po.setRemindTitle("浸泡");
        po.setRemindContent("浸泡进行中");
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
