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
        ShopPO cached = setCache(tenantCode, shopCode, null);
        if (cached != null) {
            return cached;
        }

        ShopPO po = mapper.selectOne(tenantCode, shopCode, null);

        // 设置缓存
        setCache(tenantCode, shopCode, null, po);
        return po;
    }

    public ShopPO selectOneByName(String tenantCode, String shopName) {
        // 首先访问缓存
        ShopPO cached = setCache(tenantCode, null, shopName);
        if (cached != null) {
            return cached;
        }

        ShopPO po = mapper.selectOne(tenantCode, null, shopName);

        // 设置缓存
        setCache(tenantCode, null, shopName, po);
        return po;
    }

    public List<ShopPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<ShopPO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<ShopPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
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
        return mapper.insert(po);
    }

    public int update(ShopPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCacheAll(po.getTenantCode(), po.getShopCode(), null);
            deleteCacheAll(po.getTenantCode(), null, po.getShopName());
        }
        return updated;
    }

    public int delete(String tenantCode, String shopCode) {
        ShopPO po = selectOneByCode(tenantCode, shopCode);
        int deleted = mapper.delete(tenantCode, shopCode);
        if (deleted == 1) {
            deleteCacheAll(tenantCode, shopCode, po.getShopName());
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String shopCode, String shopName) {
        return "shop_acc_" + tenantCode + "-" + shopCode + "-" + shopName;
    }

    private String getCacheListKey(String tenantCode) {
        return "shop_acc_" + tenantCode;
    }

    private ShopPO setCache(String tenantCode, String shopCode, String shopName) {
        String key = getCacheKey(tenantCode, shopCode, shopName);
        Object cached = redisManager.getValue(key);
        ShopPO po = (ShopPO) cached;
        return po;
    }

    private List<ShopPO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<ShopPO> poList = (List<ShopPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<ShopPO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String shopCode, String shopName, ShopPO po) {
        String key = getCacheKey(tenantCode, shopCode, shopName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheAll(String tenantCode, String shopCode, String shopName) {
        redisManager.deleteKey(getCacheKey(tenantCode, shopCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, shopName));
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
