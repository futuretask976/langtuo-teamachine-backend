package com.langtuo.teamachine.dao.accessor.drinkset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.drinkset.SpecMapper;
import com.langtuo.teamachine.dao.po.drinkset.SpecPO;
import com.langtuo.teamachine.dao.query.drinkset.SpecQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SpecAccessor {
    @Resource
    private SpecMapper mapper;

    public SpecPO selectOneByCode(String tenantCode, String specCode) {
        return mapper.selectOne(tenantCode, specCode, null);
    }

    public SpecPO selectOneByName(String tenantCode, String specName) {
        return mapper.selectOne(tenantCode, null, specName);
    }

    public List<SpecPO> selectList(String tenantCode) {
        List<SpecPO> list = mapper.selectList(tenantCode);
        return list;
    }

    public PageInfo<SpecPO> search(String tenantCode, String specCode, String specName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        SpecQuery query = new SpecQuery();
        query.setTenantCode(tenantCode);
        query.setSpecCode(StringUtils.isBlank(specCode) ? null : specCode);
        query.setSpecName(StringUtils.isBlank(specName) ? null : specName);
        List<SpecPO> list = mapper.search(query);

        PageInfo<SpecPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(SpecPO toppingTypePO) {
        return mapper.insert(toppingTypePO);
    }

    public int update(SpecPO toppingTypePO) {
        return mapper.update(toppingTypePO);
    }

    public int delete(String tenantCode, String toppingCode) {
        return mapper.delete(tenantCode, toppingCode);
    }
}
