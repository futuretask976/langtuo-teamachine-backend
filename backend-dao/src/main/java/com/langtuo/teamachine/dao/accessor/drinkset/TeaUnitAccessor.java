package com.langtuo.teamachine.dao.accessor.drinkset;

import com.langtuo.teamachine.dao.mapper.drinkset.TeaUnitMapper;
import com.langtuo.teamachine.dao.po.drinkset.TeaUnitPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TeaUnitAccessor {
    @Resource
    private TeaUnitMapper mapper;

    public TeaUnitPO selectOneByCode(String tenantCode, String teaCode, String teaUnitCode) {
        return mapper.selectOne(tenantCode, teaCode, teaUnitCode, null);
    }

    public TeaUnitPO selectOneByName(String tenantCode, String teaCode, String teaUnitName) {
        return mapper.selectOne(tenantCode, teaCode, null, teaUnitName);
    }

    public List<TeaUnitPO> selectList(String tenantCode, String teaCode) {
        List<TeaUnitPO> list = mapper.selectList(tenantCode, teaCode);
        return list;
    }

    public int insert(TeaUnitPO po) {
        return mapper.insert(po);
    }

    public int update(TeaUnitPO po) {
        return mapper.update(teaUnitPO);
    }

    public int delete(String tenantCode, String teaCode) {
        return mapper.delete(tenantCode, teaCode);
    }
}
