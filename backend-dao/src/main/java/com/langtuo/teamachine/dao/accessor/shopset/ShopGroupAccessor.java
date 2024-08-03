package com.langtuo.teamachine.dao.accessor.shopset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
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

    @Resource
    private RedisManager redisManager;

    public ShopGroupPO selectOne(String tenantCode, String shopGroupCode) {
        // 首先访问缓存
        ShopGroupPO cached = getCachedShopGroup(tenantCode, shopGroupCode);
        if (cached != null) {
            return cached;
        }

        ShopGroupPO po = mapper.selectOne(tenantCode, shopGroupCode);

        // 设置缓存
        setCachedShopGroup(tenantCode, shopGroupCode, po);
        return po;
    }

    public List<ShopGroupPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<ShopGroupPO> cachedList = getCachedShopGroupList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<ShopGroupPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCachedShopGroupList(tenantCode, list);
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
        int updated = mapper.update(shopGroupPO);
        if (updated == 1) {
            deleteCachedShopGroup(shopGroupPO.getTenantCode(), shopGroupPO.getShopGroupCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String shopGroupCode) {
        int deleted = mapper.delete(tenantCode, shopGroupCode);
        if (deleted == 1) {
            deleteCachedShopGroup(tenantCode, shopGroupCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String shopGroupCode) {
        return "shop_group_acc_" + tenantCode + "-" + shopGroupCode;
    }

    private String getCacheKey(String tenantCode) {
        return "shop_group_acc_" + tenantCode;
    }

    private ShopGroupPO getCachedShopGroup(String tenantCode, String shopGroupCode) {
        String key = getCacheKey(tenantCode, shopGroupCode);
        Object cached = redisManager.getValue(key);
        ShopGroupPO po = (ShopGroupPO) cached;
        return po;
    }

    private List<ShopGroupPO> getCachedShopGroupList(String tenantCode) {
        String key = getCacheKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<ShopGroupPO> poList = (List<ShopGroupPO>) cached;
        return poList;
    }

    private void setCachedShopGroupList(String tenantCode, List<ShopGroupPO> poList) {
        String key = getCacheKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCachedShopGroup(String tenantCode, String shopGroupCode, ShopGroupPO po) {
        String key = getCacheKey(tenantCode, shopGroupCode);
        redisManager.setValue(key, po);
    }

    private void deleteCachedShopGroup(String tenantCode, String shopGroupCode) {
        redisManager.deleteKey(getCacheKey(tenantCode, shopGroupCode));
        redisManager.deleteKey(getCacheKey(tenantCode));
    }
}
