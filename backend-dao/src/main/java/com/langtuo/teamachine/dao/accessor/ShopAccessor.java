package com.langtuo.teamachine.dao.accessor;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.ShopMapper;
import com.langtuo.teamachine.dao.po.ShopPO;
import com.langtuo.teamachine.dao.query.ShopQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ShopAccessor {
    @Resource
    private ShopMapper shopMapper;

    public ShopPO selectOne(String tenantCode, String shopCode) {
        return shopMapper.selectOne(tenantCode, shopCode);
    }

    public List<ShopPO> selectList(String tenantCode) {
        List<ShopPO> list = shopMapper.selectList(tenantCode);

        return list;
    }

    public PageInfo<ShopPO> search(String tenantCode, String shopName, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        ShopQuery shopQuery = new ShopQuery();
        shopQuery.setTenantCode(tenantCode);
        shopQuery.setShopName(StringUtils.isBlank(shopName) ? null : shopName);
        List<ShopPO> list = shopMapper.search(shopQuery);

        PageInfo<ShopPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(ShopPO shopPO) {
        return shopMapper.insert(shopPO);
    }

    public int update(ShopPO shopPO) {
        return shopMapper.update(shopPO);
    }

    public int delete(String tenantCode, String shopGroupCode) {
        return shopMapper.delete(tenantCode, shopGroupCode);
    }
}
