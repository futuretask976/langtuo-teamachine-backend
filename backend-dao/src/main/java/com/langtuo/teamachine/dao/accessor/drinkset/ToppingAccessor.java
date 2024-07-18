package com.langtuo.teamachine.dao.accessor.drinkset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.drinkset.ToppingMapper;
import com.langtuo.teamachine.dao.po.drinkset.ToppingPO;
import com.langtuo.teamachine.dao.query.drinkset.ToppingQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ToppingAccessor {
    @Resource
    private ToppingMapper mapper;

    public ToppingPO selectOneByCode(String tenantCode, String toppingCode) {
        return mapper.selectOne(tenantCode, toppingCode, null);
    }

    public ToppingPO selectOneByName(String tenantCode, String toppingName) {
        return mapper.selectOne(tenantCode, null, toppingName);
    }

    public List<ToppingPO> selectList(String tenantCode) {
        List<ToppingPO> list = mapper.selectList(tenantCode);
        return list;
    }

    public PageInfo<ToppingPO> search(String tenantCode, String toppingCode, String toppingName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        ToppingQuery query = new ToppingQuery();
        query.setTenantCode(tenantCode);
        query.setToppingName(StringUtils.isBlank(toppingName) ? null : toppingName);
        query.setToppingCode(StringUtils.isBlank(toppingCode) ? null : toppingCode);
        List<ToppingPO> list = mapper.search(query);

        PageInfo<ToppingPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(ToppingPO toppingTypePO) {
        return mapper.insert(toppingTypePO);
    }

    public int update(ToppingPO toppingTypePO) {
        return mapper.update(toppingTypePO);
    }

    public int delete(String tenantCode, String toppingCode) {
        return mapper.delete(tenantCode, toppingCode);
    }
}
