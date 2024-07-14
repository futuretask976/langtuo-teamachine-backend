package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.TeaUnitMapper;
import com.langtuo.teamachine.dao.po.TeaUnitPO;
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

    public int insert(TeaUnitPO teaUnitPO) {
        return mapper.insert(teaUnitPO);
    }

    public int update(TeaUnitPO teaUnitPO) {
        return mapper.update(teaUnitPO);
    }

    public int delete(String tenantCode, String teaCode) {
        return mapper.delete(tenantCode, teaCode);
    }
}
