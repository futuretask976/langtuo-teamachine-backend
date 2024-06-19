package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.SeriesTeaRelMapper;
import com.langtuo.teamachine.dao.po.SeriesTeaRelPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class SeriesTeaRelPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        delete();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        SeriesTeaRelMapper mapper = sqlSession.getMapper(SeriesTeaRelMapper.class);

        SeriesTeaRelPO po = null;

        po = new SeriesTeaRelPO();
        po.setTenantCode("tenant_001");
        po.setTeaCode("tea_001");
        po.setSeriesCode("series_001");
        mapper.insert(po);

        po = new SeriesTeaRelPO();
        po.setTenantCode("tenant_002");
        po.setTeaCode("tea_002");
        po.setSeriesCode("series_002");
        mapper.insert(po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        SeriesTeaRelMapper mapper = sqlSession.getMapper(SeriesTeaRelMapper.class);

        List<SeriesTeaRelPO> list = mapper.selectList();
        for (SeriesTeaRelPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        SeriesTeaRelPO po = mapper.selectOne("tenant_002", "tea_002", "series_002");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void delete() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        SeriesTeaRelMapper mapper = sqlSession.getMapper(SeriesTeaRelMapper.class);

        mapper.delete("tenant_001", "tea_001", "series_001");

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
