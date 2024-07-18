package com.langtuo.teamachine.dao.accessor.drinkset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.drinkset.ToppingTypeMapper;
import com.langtuo.teamachine.dao.po.drinkset.ToppingTypePO;
import com.langtuo.teamachine.dao.query.drinkset.ToppingTypeQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ToppingTypeAccessor {
    @Resource
    private ToppingTypeMapper mapper;

    public ToppingTypePO selectOneByCode(String tenantCode, String toppingTypeCode) {
        return mapper.selectOne(tenantCode, toppingTypeCode, null);
    }

    public ToppingTypePO selectOneByName(String tenantCode, String toppingTypeName) {
        return mapper.selectOne(tenantCode, null, toppingTypeName);
    }

    public List<ToppingTypePO> selectList(String tenantCode) {
        List<ToppingTypePO> list = mapper.selectList(tenantCode);

        return list;
    }

    public PageInfo<ToppingTypePO> search(String tenantCode, String toppingTypeCode, String toppingTypeName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        ToppingTypeQuery query = new ToppingTypeQuery();
        query.setTenantCode(tenantCode);
        query.setToppingTypeName(StringUtils.isBlank(toppingTypeName) ? null : toppingTypeName);
        query.setToppingTypeCode(StringUtils.isBlank(toppingTypeCode) ? null : toppingTypeCode);
        List<ToppingTypePO> list = mapper.search(query);

        PageInfo<ToppingTypePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(ToppingTypePO po) {
        return mapper.insert(po);
    }

    public int update(ToppingTypePO po) {
        return mapper.update(po);
    }

    public int delete(String tenantCode, String toppingTypeCode) {
        return mapper.delete(tenantCode, toppingTypeCode);
    }
}
