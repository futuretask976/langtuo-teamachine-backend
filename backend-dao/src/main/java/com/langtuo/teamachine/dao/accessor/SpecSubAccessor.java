package com.langtuo.teamachine.dao.accessor;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.SpecMapper;
import com.langtuo.teamachine.dao.mapper.SpecSubMapper;
import com.langtuo.teamachine.dao.po.SpecPO;
import com.langtuo.teamachine.dao.po.SpecSubPO;
import com.langtuo.teamachine.dao.query.SpecQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SpecSubAccessor {
    @Resource
    private SpecSubMapper mapper;

    public SpecSubPO selectOneByCode(String tenantCode, String specSubCode) {
        return mapper.selectOne(tenantCode, specSubCode, null);
    }

    public SpecSubPO selectOneByName(String tenantCode, String specSubName) {
        return mapper.selectOne(tenantCode, null, specSubName);
    }

    public List<SpecSubPO> selectList(String tenantCode, String specCode) {
        List<SpecSubPO> list = mapper.selectList(tenantCode, specCode);
        return list;
    }

    public int insert(SpecSubPO specSubPO) {
        return mapper.insert(specSubPO);
    }

    public int update(SpecSubPO specSubPO) {
        return mapper.update(specSubPO);
    }

    public int deleteBySpecCode(String tenantCode, String specCode) {
        return mapper.delete(tenantCode, specCode, null);
    }

    public int deleteBySpecSubCode(String tenantCode, String specSubCode) {
        return mapper.delete(tenantCode, null, specSubCode);
    }
}
