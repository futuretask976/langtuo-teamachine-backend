package com.langtuo.teamachine.dao.accessor.shopset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.shopset.ShopMapper;
import com.langtuo.teamachine.dao.po.shopset.ShopPO;
import com.langtuo.teamachine.dao.query.shopset.ShopQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ShopAccessor {
    @Resource
    private ShopMapper mapper;

    public ShopPO selectOneByCode(String tenantCode, String shopCode) {
        return mapper.selectOne(tenantCode, shopCode, null);
    }

    public ShopPO selectOneByName(String tenantCode, String shopName) {
        return mapper.selectOne(tenantCode, null, shopName);
    }

    public List<ShopPO> selectList(String tenantCode) {
        List<ShopPO> list = mapper.selectList(tenantCode);

        return list;
    }

    public PageInfo<ShopPO> search(String tenantCode, String shopName, String shopGroupCode, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        ShopQuery shopQuery = new ShopQuery();
        shopQuery.setTenantCode(tenantCode);
        shopQuery.setShopName(StringUtils.isBlank(shopName) ? null : shopName);
        shopQuery.setShopGroupCode(StringUtils.isBlank(shopGroupCode) ? null : shopGroupCode);
        List<ShopPO> list = mapper.search(shopQuery);

        PageInfo<ShopPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(ShopPO shopPO) {
        return mapper.insert(shopPO);
    }

    public int update(ShopPO shopPO) {
        return mapper.update(shopPO);
    }

    public int delete(String tenantCode, String shopGroupCode) {
        return mapper.delete(tenantCode, shopGroupCode);
    }
}
