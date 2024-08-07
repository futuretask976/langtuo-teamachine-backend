package com.langtuo.teamachine.dao.accessor.shop;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.shop.ShopGroupMapper;
import com.langtuo.teamachine.dao.po.shop.ShopGroupPO;
import com.langtuo.teamachine.dao.query.shop.ShopGroupQuery;
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
        ShopGroupPO cached = getCache(tenantCode, shopGroupCode);
        if (cached != null) {
            return cached;
        }

        ShopGroupPO po = mapper.selectOne(tenantCode, shopGroupCode);

        // 设置缓存
        setCache(tenantCode, shopGroupCode, po);
        return po;
    }

    public List<ShopGroupPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<ShopGroupPO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<ShopGroupPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
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

    public int insert(ShopGroupPO po) {
        int inserted = mapper.insert(po);
        if (inserted == 1) {
            deleteCacheOne(po.getTenantCode(), po.getShopGroupCode());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(ShopGroupPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCacheOne(po.getTenantCode(), po.getShopGroupCode());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String shopGroupCode) {
        int deleted = mapper.delete(tenantCode, shopGroupCode);
        if (deleted == 1) {
            deleteCacheOne(tenantCode, shopGroupCode);
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String shopGroupCode) {
        return "shop_group_acc_" + tenantCode + "-" + shopGroupCode;
    }

    private String getCacheListKey(String tenantCode) {
        return "shop_group_acc_" + tenantCode;
    }

    private ShopGroupPO getCache(String tenantCode, String shopGroupCode) {
        String key = getCacheKey(tenantCode, shopGroupCode);
        Object cached = redisManager.getValue(key);
        ShopGroupPO po = (ShopGroupPO) cached;
        return po;
    }

    private List<ShopGroupPO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<ShopGroupPO> poList = (List<ShopGroupPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<ShopGroupPO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String shopGroupCode, ShopGroupPO po) {
        String key = getCacheKey(tenantCode, shopGroupCode);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String shopGroupCode) {
        redisManager.deleteKey(getCacheKey(tenantCode, shopGroupCode));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
