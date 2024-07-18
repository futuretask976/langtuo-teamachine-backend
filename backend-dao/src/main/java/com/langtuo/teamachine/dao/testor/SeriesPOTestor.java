package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.menuset.SeriesMapper;
import com.langtuo.teamachine.dao.po.menuset.SeriesPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class SeriesPOTestor {
    public static void main(String args[]) {
//        insert();
//        select();
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        SeriesMapper mapper = sqlSession.getMapper(SeriesMapper.class);

        SeriesPO po = null;

        po = new SeriesPO();
        po.setTenantCode("tenant_001");
        po.setSeriesCode("series_code_001");
        po.setSeriesName("春秋精选");
        po.setImgLink("https://hushangayi.jpg");
        po.setComment("这里是备注001");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new SeriesPO();
        po.setTenantCode("tenant_002");
        po.setSeriesCode("series_code_002");
        po.setSeriesName("活动精选");
        po.setImgLink("https://hushangayidfdfaf.jpg");
        po.setComment("这里是备注002");
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
        SeriesMapper mapper = sqlSession.getMapper(SeriesMapper.class);

        List<SeriesPO> list = mapper.selectList("tenant_002");
        for (SeriesPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        SeriesPO po = mapper.selectOne("tenant_002", "series_code_002", null);
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        SeriesMapper mapper = sqlSession.getMapper(SeriesMapper.class);

        SeriesPO po = new SeriesPO();
        po.setTenantCode("tenant_002");
        po.setSeriesCode("series_code_002");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v55551");
            put("k2", "v777772");
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
