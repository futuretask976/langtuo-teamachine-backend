package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.SpecItemMapper;
import com.langtuo.teamachine.dao.po.SpecItemPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SpecItemAccessor {
    @Resource
    private SpecItemMapper mapper;

    public SpecItemPO selectOneByCode(String tenantCode, String specItemCode) {
        return mapper.selectOne(tenantCode, specItemCode, null);
    }

    public SpecItemPO selectOneByName(String tenantCode, String specItemName) {
        return mapper.selectOne(tenantCode, null, specItemName);
    }

    public List<SpecItemPO> selectList(String tenantCode, String specCode) {
        List<SpecItemPO> list = mapper.selectList(tenantCode, specCode);
        return list;
    }

    public int insert(SpecItemPO specItemPO) {
        return mapper.insert(specItemPO);
    }

    public int update(SpecItemPO specItemPO) {
        return mapper.update(specItemPO);
    }

    public int deleteBySpecCode(String tenantCode, String specCode) {
        return mapper.delete(tenantCode, specCode, null);
    }

    public int deleteBySpecSubCode(String tenantCode, String specItemCode) {
        return mapper.delete(tenantCode, null, specItemCode);
    }
}
