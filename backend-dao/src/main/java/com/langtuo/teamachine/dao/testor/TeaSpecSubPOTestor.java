package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.drinkset.SpecItemMapper;
import com.langtuo.teamachine.dao.po.drinkset.SpecItemPO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class TeaSpecSubPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        SpecItemMapper mapper = sqlSession.getMapper(SpecItemMapper.class);

        SpecItemPO po = null;

        po = new SpecItemPO();
        po.setTenantCode("tenant_001");
        po.setSpecCode("spec_001");
        po.setSpecItemCode("spec_sub_code_001");
        po.setSpecItemName("spec_sub_name_001");
        po.setOuterSpecItemCode("outer_spec_sub_code_001");
        mapper.insert(po);

        po = new SpecItemPO();
        po.setTenantCode("tenant_002");
        po.setSpecCode("spec_002");
        po.setSpecItemCode("spec_sub_code_002");
        po.setSpecItemName("spec_sub_name_002");
        po.setOuterSpecItemCode("outer_spec_sub_code_002");
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        SpecItemMapper mapper = sqlSession.getMapper(SpecItemMapper.class);

        List<SpecItemPO> list = mapper.selectList("tenant_001", "spec_001");
        for (SpecItemPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        SpecItemPO po = mapper.selectOne("tenant_001", "spec_sub_code_001", null);
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        SpecItemMapper mapper = sqlSession.getMapper(SpecItemMapper.class);

        SpecItemPO po = new SpecItemPO();
        po.setTenantCode("tenant_002");
        po.setSpecItemCode("spec_sub_code_002");
        po.setOuterSpecItemCode("outer_spec_sub_code_00277777");
        mapper.update(po);

        sqlSession.commit();
        sqlSession.close();
    }

//    public static void testBySpring() {
//        ApplicationContext context = startBySpring();
//
//        TeaSpecSubMapper mapper =
//                (TeaSpecSubMapper) context.getBean(TeaSpecSubMapper.class);
//        TeaSpecSubPOjo TeaSpecSubPOjo = mapper.getOne("machine_001");
//        System.out.printf("DaoDemo#testBySpring hotelGuestPojo=%s\n", TeaSpecSubPOjo);
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
