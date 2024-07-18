package com.langtuo.teamachine.dao.accessor.drinkset;

import com.langtuo.teamachine.dao.mapper.drinkset.SpecItemMapper;
import com.langtuo.teamachine.dao.po.drinkset.SpecItemPO;
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

    public int insert(SpecItemPO po) {
        return mapper.insert(po);
    }

    public int update(SpecItemPO po) {
        return mapper.update(po);
    }

    public int deleteBySpecCode(String tenantCode, String specCode) {
        return mapper.delete(tenantCode, specCode, null);
    }

    public int deleteBySpecSubCode(String tenantCode, String specItemCode) {
        return mapper.delete(tenantCode, null, specItemCode);
    }
}
