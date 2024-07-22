package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.ruleset.CleanRuleMapper;
import com.langtuo.teamachine.dao.po.ruleset.CleanRulePO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class CleanRulePOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        CleanRuleMapper mapper = sqlSession.getMapper(CleanRuleMapper.class);

        CleanRulePO po = null;

        po = new CleanRulePO();
        po.setTenantCode("tenant_001");
        po.setCleanRuleCode("clean_rule_001");
        po.setCleanRuleName("清洁规则001");
        po.setPermitBatch(0);
        po.setPermitRemind(1);
        po.setComment("松江的门店");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new CleanRulePO();
        po.setTenantCode("tenant_002");
        po.setCleanRuleCode("clean_rule_002");
        po.setCleanRuleName("清洁规则002");
        po.setPermitBatch(0);
        po.setPermitRemind(1);
        po.setComment("松江的门店2");
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
        CleanRuleMapper mapper = sqlSession.getMapper(CleanRuleMapper.class);

        List<CleanRulePO> list = mapper.selectList("tenant_002");
        for (CleanRulePO po : list) {
            System.out.printf("!!!!!!!!!! list->po: %s\n", po);
        }

        CleanRulePO po = mapper.selectOne("tenant_002", "clean_rule_002", null);
        System.out.printf("!!!!!!!!!! po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        CleanRuleMapper mapper = sqlSession.getMapper(CleanRuleMapper.class);

        CleanRulePO po = new CleanRulePO();
        po.setTenantCode("tenant_002");
        po.setCleanRuleCode("clean_rule_002");
        po.setCleanRuleName("清洁规则002777777");
        po.setPermitBatch(3);
        po.setPermitRemind(5);
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
