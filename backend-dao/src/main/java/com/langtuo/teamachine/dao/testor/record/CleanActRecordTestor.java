package com.langtuo.teamachine.dao.testor.record;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.record.CleanActRecordMapper;
import com.langtuo.teamachine.dao.mapper.record.InvalidActRecordMapper;
import com.langtuo.teamachine.dao.po.record.CleanActRecordPO;
import com.langtuo.teamachine.dao.po.record.InvalidActRecordPO;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CleanActRecordTestor {
    public static void main(String args[]) {
        insert();
//        select();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        CleanActRecordMapper mapper = sqlSession.getMapper(CleanActRecordMapper.class);

        CleanActRecordPO po = null;

        po = new CleanActRecordPO();
        po.setTenantCode("tenant_001");
        po.setExtraInfo(new HashMap(){{
            put("abc", "def");
        }});
        po.setIdempotentMark("1234");
        po.setMachineCode("abcd");
        po.setShopCode("shop_001");
        po.setShopGroupCode("shopGroup_02");
        po.setCleanStartTime(new Date());
        po.setCleanEndTime(new Date());
        po.setToppingCode("topping_001");
        po.setPipelineNum(1);
        po.setCleanType(1);
        po.setCleanRuleCode("123");
        po.setOpenRuleCode("456");
        po.setCloseRuleCode("789");
        po.setCleanContent(1);
        po.setWashSec(10);
        po.setSoakMin(20);
        po.setFlushSec(30);
        po.setFlushIntervalMin(40);
        int inserted = mapper.insert(po);
        System.out.println("inserted=" + inserted);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        InvalidActRecordMapper mapper = sqlSession.getMapper(InvalidActRecordMapper.class);

        List<InvalidActRecordPO> list = mapper.selectList("tenant_001");
        for (InvalidActRecordPO po : list) {
            System.out.printf("$$$$$ list->po: %s\n", po);
        }

        InvalidActRecordPO po = mapper.selectOne("tenant_001", "1234567890");
        System.out.printf("$$$$$ po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

//    public static void testBySpring() {
//        ApplicationContext context = startBySpring();
//
//        AdminMapper mapper =
//                (AdminMapper) context.getBean(AdminMapper.class);
//        AdminPOjo AdminPOjo = mapper.getOne("machine_001");
//        System.out.printf("DaoDemo#testBySpring hotelGuestPojo=%s\n", AdminPOjo);
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
