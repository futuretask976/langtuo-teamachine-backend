package com.langtuo.teamachine.dao.accessor.shop;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.shop.ShopMapper;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import com.langtuo.teamachine.dao.query.shop.ShopQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ShopAccessor {
    @Resource
    private ShopMapper mapper;

    @Resource
    private RedisManager redisManager;

    public ShopPO selectOneByCode(String tenantCode, String shopCode) {
        // 首先访问缓存
        ShopPO cached = getCache(tenantCode, shopCode, null);
        if (cached != null) {
            return cached;
        }

        ShopPO po = mapper.selectOne(tenantCode, shopCode, null);

        // 设置缓存
        getCache(tenantCode, shopCode, null, po);
        return po;
    }

    public ShopPO selectOneByName(String tenantCode, String shopName) {
        // 首先访问缓存
        ShopPO cached = getCache(tenantCode, null, shopName);
        if (cached != null) {
            return cached;
        }

        ShopPO po = mapper.selectOne(tenantCode, null, shopName);

        // 设置缓存
        getCache(tenantCode, null, shopName, po);
        return po;
    }

    public List<ShopPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<ShopPO> cachedList = getCacheList(tenantCode, null);
        if (cachedList != null) {
            return cachedList;
        }

        List<ShopPO> list = mapper.selectList(tenantCode, null);

        // 设置缓存
        setCacheList(tenantCode, null, list);
        return list;
    }

    public List<ShopPO> selectList(String tenantCode, String shopGroupCode) {
        // 首先访问缓存
        List<ShopPO> cachedList = getCacheList(tenantCode, shopGroupCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<ShopPO> list = mapper.selectList(tenantCode, shopGroupCode);

        // 设置缓存
        setCacheList(tenantCode, shopGroupCode, list);
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

    public int insert(ShopPO po) {
        int inserted = mapper.insert(po);
        if (inserted == 1) {
            deleteCacheOne(po.getTenantCode(), po.getShopCode(), po.getShopName());
            deleteCacheList(po.getTenantCode(), po.getShopGroupCode());
        }
        return inserted;
    }

    public int update(ShopPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCacheOne(po.getTenantCode(), po.getShopCode(), po.getShopName());
            deleteCacheList(po.getTenantCode(), po.getShopGroupCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String shopCode) {
        ShopPO po = selectOneByCode(tenantCode, shopCode);
        if (po == null) {
            return 0;
        }

        int deleted = mapper.delete(tenantCode, shopCode);
        if (deleted == 1) {
            deleteCacheOne(tenantCode, po.getShopCode(), po.getShopName());
            deleteCacheList(tenantCode, po.getShopGroupCode());
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String shopCode, String shopName) {
        return "shop_acc_" + tenantCode + "-" + shopCode + "-" + shopName;
    }

    private String getCacheListKey(String tenantCode, String shopGroupCode) {
        return "shop_acc_" + tenantCode + "-" +shopGroupCode;
    }

    private ShopPO getCache(String tenantCode, String shopCode, String shopName) {
        String key = getCacheKey(tenantCode, shopCode, shopName);
        Object cached = redisManager.getValue(key);
        ShopPO po = (ShopPO) cached;
        return po;
    }

    private List<ShopPO> getCacheList(String tenantCode, String shopGroupCode) {
        String key = getCacheListKey(tenantCode, shopGroupCode);
        Object cached = redisManager.getValue(key);
        List<ShopPO> poList = (List<ShopPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String shopGroupCode, List<ShopPO> poList) {
        String key = getCacheListKey(tenantCode, shopGroupCode);
        redisManager.setValue(key, poList);
    }

    private void getCache(String tenantCode, String shopCode, String shopName, ShopPO po) {
        String key = getCacheKey(tenantCode, shopCode, shopName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String shopCode, String shopName) {
        redisManager.deleteKey(getCacheKey(tenantCode, shopCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, shopName));
    }

    private void deleteCacheList(String tenantCode, String shopGroupCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, shopGroupCode));
        redisManager.deleteKey(getCacheListKey(tenantCode, null));
    }
}
