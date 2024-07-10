package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.TeaToppingRelMapper;
import com.langtuo.teamachine.dao.po.TeaToppingRelPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TeaToppingRelAccessor {
    @Resource
    private TeaToppingRelMapper mapper;

    public TeaToppingRelPO selectOne(String tenantCode, String teaCode, String toppingCode) {
        return mapper.selectOne(tenantCode, teaCode, toppingCode);
    }

    public List<TeaToppingRelPO> selectList(String tenantCode, String teaCode) {
        List<TeaToppingRelPO> list = mapper.selectList(tenantCode, teaCode);
        return list;
    }

    public int insert(TeaToppingRelPO toppingTypePO) {
        return mapper.insert(toppingTypePO);
    }

    public int update(TeaToppingRelPO toppingTypePO) {
        return mapper.update(toppingTypePO);
    }

    public int deleteByTeaCode(String tenantCode, String teaCode) {
        return mapper.delete(tenantCode, teaCode, null);
    }

    public int delete(String tenantCode, String teaCode, String toppingCode) {
        return mapper.delete(tenantCode, teaCode, toppingCode);
    }
}
