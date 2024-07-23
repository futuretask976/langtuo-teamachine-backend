package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.ruleset.CleanRuleExceptMapper;
import com.langtuo.teamachine.dao.po.ruleset.CleanRuleExceptPO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class CleanExceptRulePOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        delete();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        CleanRuleExceptMapper mapper = sqlSession.getMapper(CleanRuleExceptMapper.class);

        CleanRuleExceptPO po = null;

        po = new CleanRuleExceptPO();
        po.setTenantCode("tenant_001");
        po.setCleanRuleCode("clean_rule_001");
        po.setExceptToppingCode("except_001");
        mapper.insert(po);

        po = new CleanRuleExceptPO();
        po.setTenantCode("tenant_002");
        po.setCleanRuleCode("clean_rule_002");
        po.setExceptToppingCode("except_002");
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        CleanRuleExceptMapper mapper = sqlSession.getMapper(CleanRuleExceptMapper.class);

        List<CleanRuleExceptPO> list = mapper.selectList("tenant_002", "clean_rule_002");
        for (CleanRuleExceptPO po : list) {
            System.out.printf("$$$$$ list->po: %s\n", po);
        }

        CleanRuleExceptPO po = mapper.selectOne("tenant_002", "clean_rule_002", "except_002");
        System.out.printf("$$$$$ po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void delete() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        CleanRuleExceptMapper mapper = sqlSession.getMapper(CleanRuleExceptMapper.class);

        mapper.delete("tenant_001", "clean_rule_001");

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
