package com.gx.sp3.demo.dao.testor;

import com.gx.sp3.demo.dao.helper.SqlSessionFactoryHelper;
import com.gx.sp3.demo.dao.mapper.langtuo.*;
import com.gx.sp3.demo.dao.pojo.langtuo.*;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class LangTuoTestor {
    public static void main(String args[]) {
//        insertTestRec4Machine();
        selectTestRec4Machine();

//        insertTestRec4MachineTea();
//        selectTestRec4MachineTea();

//        insertTestRec4MachineTopping();
//        selectTestRec4MachineTopping();

//        insertTestRec4MachineTeaTopping();
//        selectTestRec4MachineTeaTopping();

//        insertTestRec4MachineTeaOrder();
//        selectTestRec4MachineTeaOrder();


//        selectSequenceDemo();
    }

    public static void selectSequenceDemo() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        SequenceDemoMapper mapper = sqlSession.getMapper(SequenceDemoMapper.class);

        long rtn = mapper.getNextSequenceValue();
        System.out.printf("rtn: %s\n", rtn);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void selectTestRec4MachineTeaOrder() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineTeaOrderMapper mapper = sqlSession.getMapper(MachineTeaOrderMapper.class);

        List<MachineTeaOrderPojo> list = mapper.list();
        for (MachineTeaOrderPojo pojo : list) {
            System.out.printf("item: %s\n", pojo);
        }

        MachineTeaOrderPojo pojo = mapper.get("100001");
        System.out.printf("single: %s\n", pojo);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void insertTestRec4MachineTeaOrder() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineTeaOrderMapper mapper = sqlSession.getMapper(MachineTeaOrderMapper.class);

        MachineTeaOrderPojo pojo = null;

        pojo = new MachineTeaOrderPojo();
        pojo.setMachineCode("machine_001");
        pojo.setOrderId("100001");
        pojo.setMachineCode("machine_001");
        pojo.setTeaCode("tea_001");
        pojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(pojo);

        pojo = new MachineTeaOrderPojo();
        pojo.setMachineCode("machine_001");
        pojo.setOrderId("100002");
        pojo.setMachineCode("machine_001");
        pojo.setTeaCode("tea_001");
        pojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(pojo);

        pojo = new MachineTeaOrderPojo();
        pojo.setMachineCode("machine_001");
        pojo.setOrderId("100003");
        pojo.setMachineCode("machine_001");
        pojo.setTeaCode("tea_002");
        pojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(pojo);

        pojo = new MachineTeaOrderPojo();
        pojo.setMachineCode("machine_001");
        pojo.setOrderId("100004");
        pojo.setMachineCode("machine_001");
        pojo.setTeaCode("tea_003");
        pojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(pojo);

        pojo = new MachineTeaOrderPojo();
        pojo.setMachineCode("machine_001");
        pojo.setOrderId("100005");
        pojo.setMachineCode("machine_001");
        pojo.setTeaCode("tea_004");
        pojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(pojo);

        pojo = new MachineTeaOrderPojo();
        pojo.setMachineCode("machine_001");
        pojo.setOrderId("100006");
        pojo.setMachineCode("machine_001");
        pojo.setTeaCode("tea_004");
        pojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(pojo);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void selectTestRec4MachineTeaTopping() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineTeaToppingMapper mapper = sqlSession.getMapper(MachineTeaToppingMapper.class);

        List<MachineTeaToppingPojo> list = mapper.list();
        for (MachineTeaToppingPojo machineTeaToppingPojo : list) {
            System.out.printf("item: %s\n", machineTeaToppingPojo);
        }

        MachineTeaToppingPojo machineTeaToppingPojo = mapper.get("machine_001", "tea_001", "topping_001");
        System.out.printf("single: %s\n", machineTeaToppingPojo);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void insertTestRec4MachineTeaTopping() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineTeaToppingMapper mapper = sqlSession.getMapper(MachineTeaToppingMapper.class);

        MachineTeaToppingPojo machineTeaToppingPojo = null;

        machineTeaToppingPojo = new MachineTeaToppingPojo();
        machineTeaToppingPojo.setMachineCode("machine_001");
        machineTeaToppingPojo.setTeaCode("tea_001");
        machineTeaToppingPojo.setToppingCode("topping_001");
        machineTeaToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaToppingPojo);

        machineTeaToppingPojo = new MachineTeaToppingPojo();
        machineTeaToppingPojo.setMachineCode("machine_001");
        machineTeaToppingPojo.setTeaCode("tea_001");
        machineTeaToppingPojo.setToppingCode("topping_002");
        machineTeaToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaToppingPojo);

        machineTeaToppingPojo = new MachineTeaToppingPojo();
        machineTeaToppingPojo.setMachineCode("machine_001");
        machineTeaToppingPojo.setTeaCode("tea_001");
        machineTeaToppingPojo.setToppingCode("topping_003");
        machineTeaToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaToppingPojo);

        machineTeaToppingPojo = new MachineTeaToppingPojo();
        machineTeaToppingPojo.setMachineCode("machine_001");
        machineTeaToppingPojo.setTeaCode("tea_002");
        machineTeaToppingPojo.setToppingCode("topping_004");
        machineTeaToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaToppingPojo);

        machineTeaToppingPojo = new MachineTeaToppingPojo();
        machineTeaToppingPojo.setMachineCode("machine_001");
        machineTeaToppingPojo.setTeaCode("tea_002");
        machineTeaToppingPojo.setToppingCode("topping_005");
        machineTeaToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaToppingPojo);

        machineTeaToppingPojo = new MachineTeaToppingPojo();
        machineTeaToppingPojo.setMachineCode("machine_001");
        machineTeaToppingPojo.setTeaCode("tea_003");
        machineTeaToppingPojo.setToppingCode("topping_002");
        machineTeaToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaToppingPojo);

        machineTeaToppingPojo = new MachineTeaToppingPojo();
        machineTeaToppingPojo.setMachineCode("machine_001");
        machineTeaToppingPojo.setTeaCode("tea_003");
        machineTeaToppingPojo.setToppingCode("topping_004");
        machineTeaToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaToppingPojo);

        machineTeaToppingPojo = new MachineTeaToppingPojo();
        machineTeaToppingPojo.setMachineCode("machine_001");
        machineTeaToppingPojo.setTeaCode("tea_003");
        machineTeaToppingPojo.setToppingCode("topping_006");
        machineTeaToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaToppingPojo);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void selectTestRec4MachineTopping() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineToppingMapper mapper = sqlSession.getMapper(MachineToppingMapper.class);

        List<MachineToppingPojo> list = mapper.list();
        for (MachineToppingPojo machineToppingPojo : list) {
            System.out.printf("item: %s\n", machineToppingPojo);
        }

        MachineToppingPojo machineToppingPojo = mapper.get("machine_001", "topping_001");
        System.out.printf("single: %s\n", machineToppingPojo);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void insertTestRec4MachineTopping() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineToppingMapper mapper = sqlSession.getMapper(MachineToppingMapper.class);

        MachineToppingPojo machineToppingPojo = null;

        machineToppingPojo = new MachineToppingPojo();
        machineToppingPojo.setMachineCode("machine_001");
        machineToppingPojo.setToppingCode("topping_001");
        machineToppingPojo.setToppingName("绿茶");
        machineToppingPojo.setToppingImgLink("https://img1.baidu.com/it/u=821008430,4127402875&fm=253&fmt=auto&app=120&f=JPEG?w=380&h=380");
        machineToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineToppingPojo);

        machineToppingPojo = new MachineToppingPojo();
        machineToppingPojo.setMachineCode("machine_001");
        machineToppingPojo.setToppingCode("topping_002");
        machineToppingPojo.setToppingName("牛奶");
        machineToppingPojo.setToppingImgLink("https://img1.baidu.com/it/u=821008430,4127402875&fm=253&fmt=auto&app=120&f=JPEG?w=380&h=380");
        machineToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineToppingPojo);

        machineToppingPojo = new MachineToppingPojo();
        machineToppingPojo.setMachineCode("machine_001");
        machineToppingPojo.setToppingCode("topping_003");
        machineToppingPojo.setToppingName("香果");
        machineToppingPojo.setToppingImgLink("https://img1.baidu.com/it/u=821008430,4127402875&fm=253&fmt=auto&app=120&f=JPEG?w=380&h=380");
        machineToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineToppingPojo);

        machineToppingPojo = new MachineToppingPojo();
        machineToppingPojo.setMachineCode("machine_001");
        machineToppingPojo.setToppingCode("topping_004");
        machineToppingPojo.setToppingName("葡萄");
        machineToppingPojo.setToppingImgLink("https://img1.baidu.com/it/u=821008430,4127402875&fm=253&fmt=auto&app=120&f=JPEG?w=380&h=380");
        machineToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineToppingPojo);

        machineToppingPojo = new MachineToppingPojo();
        machineToppingPojo.setMachineCode("machine_001");
        machineToppingPojo.setToppingCode("topping_005");
        machineToppingPojo.setToppingName("草莓");
        machineToppingPojo.setToppingImgLink("https://img1.baidu.com/it/u=821008430,4127402875&fm=253&fmt=auto&app=120&f=JPEG?w=380&h=380");
        machineToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineToppingPojo);

        machineToppingPojo = new MachineToppingPojo();
        machineToppingPojo.setMachineCode("machine_001");
        machineToppingPojo.setToppingCode("topping_006");
        machineToppingPojo.setToppingName("水蜜桃");
        machineToppingPojo.setToppingImgLink("https://img1.baidu.com/it/u=821008430,4127402875&fm=253&fmt=auto&app=120&f=JPEG?w=380&h=380");
        machineToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineToppingPojo);

        machineToppingPojo = new MachineToppingPojo();
        machineToppingPojo.setMachineCode("machine_001");
        machineToppingPojo.setToppingCode("topping_007");
        machineToppingPojo.setToppingName("乌龙茶");
        machineToppingPojo.setToppingImgLink("https://img1.baidu.com/it/u=821008430,4127402875&fm=253&fmt=auto&app=120&f=JPEG?w=380&h=380");
        machineToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineToppingPojo);

        machineToppingPojo = new MachineToppingPojo();
        machineToppingPojo.setMachineCode("machine_001");
        machineToppingPojo.setToppingCode("topping_008");
        machineToppingPojo.setToppingName("巧克力");
        machineToppingPojo.setToppingImgLink("https://img1.baidu.com/it/u=821008430,4127402875&fm=253&fmt=auto&app=120&f=JPEG?w=380&h=380");
        machineToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineToppingPojo);

        machineToppingPojo = new MachineToppingPojo();
        machineToppingPojo.setMachineCode("machine_001");
        machineToppingPojo.setToppingCode("topping_009");
        machineToppingPojo.setToppingName("椰果");
        machineToppingPojo.setToppingImgLink("https://img1.baidu.com/it/u=821008430,4127402875&fm=253&fmt=auto&app=120&f=JPEG?w=380&h=380");
        machineToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineToppingPojo);

        machineToppingPojo = new MachineToppingPojo();
        machineToppingPojo.setMachineCode("machine_001");
        machineToppingPojo.setToppingCode("topping_010");
        machineToppingPojo.setToppingName("珍珠");
        machineToppingPojo.setToppingImgLink("https://img1.baidu.com/it/u=821008430,4127402875&fm=253&fmt=auto&app=120&f=JPEG?w=380&h=380");
        machineToppingPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineToppingPojo);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void selectTestRec4MachineTea() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineTeaMapper mapper = sqlSession.getMapper(MachineTeaMapper.class);

        List<MachineTeaPojo> list = mapper.list();
        for (MachineTeaPojo machineTeaPojo : list) {
            System.out.printf("item: %s\n", machineTeaPojo);
        }

        MachineTeaPojo machineTeaPojo = mapper.get("machine_001", "tea_001");
        System.out.printf("single: %s\n", machineTeaPojo);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void insertTestRec4MachineTea() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineTeaMapper mapper = sqlSession.getMapper(MachineTeaMapper.class);

        MachineTeaPojo machineTeaPojo = null;

        machineTeaPojo = new MachineTeaPojo();
        machineTeaPojo.setMachineCode("machine_001");
        machineTeaPojo.setTeaCode("tea_001");
        machineTeaPojo.setTeaName("奶茶三兄弟");
        machineTeaPojo.setTeaImgLink("https://aimg8.dlssyht.cn/u/2011304/ueditor/image/1006/2011304/1635764085259367.jpg");
        machineTeaPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaPojo);

        machineTeaPojo = new MachineTeaPojo();
        machineTeaPojo.setMachineCode("machine_001");
        machineTeaPojo.setTeaCode("tea_002");
        machineTeaPojo.setTeaName("杨枝甘露");
        machineTeaPojo.setTeaImgLink("https://aimg8.dlssyht.cn/u/2011304/ueditor/image/1006/2011304/1635764085259367.jpg");
        machineTeaPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaPojo);

        machineTeaPojo = new MachineTeaPojo();
        machineTeaPojo.setMachineCode("machine_001");
        machineTeaPojo.setTeaCode("tea_003");
        machineTeaPojo.setTeaName("乌龙茶");
        machineTeaPojo.setTeaImgLink("https://aimg8.dlssyht.cn/u/2011304/ueditor/image/1006/2011304/1635764085259367.jpg");
        machineTeaPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaPojo);

        machineTeaPojo = new MachineTeaPojo();
        machineTeaPojo.setMachineCode("machine_001");
        machineTeaPojo.setTeaCode("tea_004");
        machineTeaPojo.setTeaName("巧克力奶茶");
        machineTeaPojo.setTeaImgLink("https://aimg8.dlssyht.cn/u/2011304/ueditor/image/1006/2011304/1635764085259367.jpg");
        machineTeaPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaPojo);

        machineTeaPojo = new MachineTeaPojo();
        machineTeaPojo.setMachineCode("machine_001");
        machineTeaPojo.setTeaCode("tea_005");
        machineTeaPojo.setTeaName("橙汁");
        machineTeaPojo.setTeaImgLink("https://aimg8.dlssyht.cn/u/2011304/ueditor/image/1006/2011304/1635764085259367.jpg");
        machineTeaPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaPojo);

        machineTeaPojo = new MachineTeaPojo();
        machineTeaPojo.setMachineCode("machine_001");
        machineTeaPojo.setTeaCode("tea_006");
        machineTeaPojo.setTeaName("水晶葡萄");
        machineTeaPojo.setTeaImgLink("https://aimg8.dlssyht.cn/u/2011304/ueditor/image/1006/2011304/1635764085259367.jpg");
        machineTeaPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaPojo);

        machineTeaPojo = new MachineTeaPojo();
        machineTeaPojo.setMachineCode("machine_001");
        machineTeaPojo.setTeaCode("tea_007");
        machineTeaPojo.setTeaName("水蜜桃奶茶");
        machineTeaPojo.setTeaImgLink("https://aimg8.dlssyht.cn/u/2011304/ueditor/image/1006/2011304/1635764085259367.jpg");
        machineTeaPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaPojo);

        machineTeaPojo = new MachineTeaPojo();
        machineTeaPojo.setMachineCode("machine_001");
        machineTeaPojo.setTeaCode("tea_008");
        machineTeaPojo.setTeaName("莓莓奶茶");
        machineTeaPojo.setTeaImgLink("https://aimg8.dlssyht.cn/u/2011304/ueditor/image/1006/2011304/1635764085259367.jpg");
        machineTeaPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaPojo);

        machineTeaPojo = new MachineTeaPojo();
        machineTeaPojo.setMachineCode("machine_001");
        machineTeaPojo.setTeaCode("tea_009");
        machineTeaPojo.setTeaName("酸酸莓莓");
        machineTeaPojo.setTeaImgLink("https://aimg8.dlssyht.cn/u/2011304/ueditor/image/1006/2011304/1635764085259367.jpg");
        machineTeaPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaPojo);

        machineTeaPojo = new MachineTeaPojo();
        machineTeaPojo.setMachineCode("machine_001");
        machineTeaPojo.setTeaCode("tea_010");
        machineTeaPojo.setTeaName("绿茶");
        machineTeaPojo.setTeaImgLink("https://aimg8.dlssyht.cn/u/2011304/ueditor/image/1006/2011304/1635764085259367.jpg");
        machineTeaPojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machineTeaPojo);

        sqlSession.commit();
        sqlSession.close();
    }


    public static void selectTestRec4Machine() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineMapper mapper = sqlSession.getMapper(MachineMapper.class);

        List<MachinePojo> list = mapper.list();
        for (MachinePojo machinePojo : list) {
            System.out.printf("item: %s\n", machinePojo);
        }

        MachinePojo machinePojo = mapper.get("machine_001");
        System.out.printf("single: %s\n", machinePojo);

        sqlSession.commit();
        sqlSession.close();
    }

    public static void insertTestRec4Machine() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineMapper mapper = sqlSession.getMapper(MachineMapper.class);

        MachinePojo machinePojo = null;

        machinePojo = new MachinePojo();
        machinePojo.setMachineCode("machine_001");
        machinePojo.setMachineName("机器_001");
        machinePojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machinePojo);

        machinePojo = new MachinePojo();
        machinePojo.setMachineCode("machine_002");
        machinePojo.setMachineName("机器_002");
        machinePojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machinePojo);

        machinePojo = new MachinePojo();
        machinePojo.setMachineCode("machine_003");
        machinePojo.setMachineName("机器_003");
        machinePojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machinePojo);

        machinePojo = new MachinePojo();
        machinePojo.setMachineCode("machine_004");
        machinePojo.setMachineName("机器_004");
        machinePojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machinePojo);

        machinePojo = new MachinePojo();
        machinePojo.setMachineCode("machine_005");
        machinePojo.setMachineName("机器_005");
        machinePojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machinePojo);

        machinePojo = new MachinePojo();
        machinePojo.setMachineCode("machine_006");
        machinePojo.setMachineName("机器_006");
        machinePojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machinePojo);

        machinePojo = new MachinePojo();
        machinePojo.setMachineCode("machine_007");
        machinePojo.setMachineName("机器_007");
        machinePojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machinePojo);

        machinePojo = new MachinePojo();
        machinePojo.setMachineCode("machine_008");
        machinePojo.setMachineName("机器_008");
        machinePojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machinePojo);

        machinePojo = new MachinePojo();
        machinePojo.setMachineCode("machine_009");
        machinePojo.setMachineName("机器_009");
        machinePojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machinePojo);

        machinePojo = new MachinePojo();
        machinePojo.setMachineCode("machine_010");
        machinePojo.setMachineName("机器_010");
        machinePojo.setExtraInfoMap(new HashMap<String, String>(){{
            put("k1", "v1");
            put("k2", "v2");
        }});
        mapper.insert(machinePojo);

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
