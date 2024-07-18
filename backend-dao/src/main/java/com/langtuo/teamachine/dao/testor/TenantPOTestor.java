package com.langtuo.teamachine.dao.testor;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.userset.TenantMapper;
import com.langtuo.teamachine.dao.po.userset.TenantPO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class TenantPOTestor {
    public static void main(String args[]) {
        update();
    }

    public static void insert() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TenantMapper mapper = sqlSession.getMapper(TenantMapper.class);

        TenantPO po = null;

        po = new TenantPO();
        po.setTenantCode("tenant_001");
        po.setTenantName("沪上阿姨");
        po.setContactPerson("阿姨");
        po.setContactPhone("123456789");
        po.setImgLink("https://hushangayi.jpg");
        po.setComment("hushangayi");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(po);

        po = new TenantPO();
        po.setTenantCode("tenant_002");
        po.setTenantName("霸王茶姬");
        po.setContactPerson("茶姬");
        po.setContactPhone("987654321");
        po.setImgLink("https://bawangchaji.jpg");
        po.setComment("bawangchaji");
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
        TenantMapper mapper = sqlSession.getMapper(TenantMapper.class);

        List<TenantPO> list = mapper.selectList();
        for (TenantPO po : list) {
            System.out.printf("list->po: %s\n", po);
        }

        TenantPO po = mapper.selectOne("100001");
        System.out.printf("po: %s\n", po);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void update() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        TenantMapper mapper = sqlSession.getMapper(TenantMapper.class);

        TenantPO po = new TenantPO();
        po.setTenantCode("tenant_001");
        po.setImgLink("https://replace.jpg");
        po.setExtraInfo(new HashMap<String, String>(){{
            put("k11", "v1111");
            put("k22", "v2222");
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
