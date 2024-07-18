package com.langtuo.teamachine.dao.accessor.shopset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.shopset.ShopGroupMapper;
import com.langtuo.teamachine.dao.po.shopset.ShopGroupPO;
import com.langtuo.teamachine.dao.query.shopset.ShopGroupQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ShopGroupAccessor {
    @Resource
    private ShopGroupMapper mapper;

    public ShopGroupPO selectOne(String tenantCode, String shopGroupCode) {
        return mapper.selectOne(tenantCode, shopGroupCode);
    }

    public List<ShopGroupPO> selectList(String tenantCode) {
        List<ShopGroupPO> list = mapper.selectList(tenantCode);

        return list;
    }

    public PageInfo<ShopGroupPO> search(String tenantCode, String shopGroupName, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        ShopGroupQuery shopGroupQuery = new ShopGroupQuery();
        shopGroupQuery.setTenantCode(tenantCode);
        shopGroupQuery.setShopGroupName(StringUtils.isBlank(shopGroupName) ? null : shopGroupName);
        List<ShopGroupPO> list = mapper.search(shopGroupQuery);

        PageInfo<ShopGroupPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(ShopGroupPO shopGroupPO) {
        return mapper.insert(shopGroupPO);
    }

    public int update(ShopGroupPO shopGroupPO) {
        return mapper.update(shopGroupPO);
    }

    public int delete(String tenantCode, String shopGroupCode) {
        return mapper.delete(tenantCode, shopGroupCode);
    }
}
