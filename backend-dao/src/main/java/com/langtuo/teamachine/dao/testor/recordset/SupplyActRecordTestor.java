package com.langtuo.teamachine.dao.testor.recordset;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.recordset.SupplyActRecordMapper;
import com.langtuo.teamachine.dao.po.recordset.SupplyActRecordPO;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SupplyActRecordTestor {
    public static void main(String args[]) {
        insert();
//        select();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        SupplyActRecordMapper mapper = sqlSession.getMapper(SupplyActRecordMapper.class);

        SupplyActRecordPO po = null;

        po = new SupplyActRecordPO();
        po.setTenantCode("tenant_001");
        po.setExtraInfo(new HashMap<String, String>(){{}});
        po.setIdempotentMark("1111");
        po.setMachineCode("1234");
        po.setShopGroupCode("shopGroup_02");
        po.setShopCode("shop_001");
        po.setSupplyTime(new Date());
        po.setToppingCode("topping_001");
        po.setPipelineNum(1);
        po.setSupplyAmount(10);
        mapper.insert(po);

        po = new SupplyActRecordPO();
        po.setTenantCode("tenant_001");
        po.setExtraInfo(new HashMap<String, String>(){{}});
        po.setIdempotentMark("2222");
        po.setMachineCode("1234");
        po.setShopGroupCode("shopGroup_03");
        po.setShopCode("shop_001");
        po.setSupplyTime(new Date());
        po.setToppingCode("topping_002");
        po.setPipelineNum(2);
        po.setSupplyAmount(22);
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        SupplyActRecordMapper mapper = sqlSession.getMapper(SupplyActRecordMapper.class);

        List<SupplyActRecordPO> list = mapper.selectList("tenant_001");
        for (SupplyActRecordPO po : list) {
            System.out.printf("$$$$$ list->po: %s\n", po);
        }

        SupplyActRecordPO po = mapper.selectOne("tenant_001", "1234567890");
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
