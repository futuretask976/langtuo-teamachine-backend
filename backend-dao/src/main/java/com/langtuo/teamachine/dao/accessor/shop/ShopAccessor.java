package com.langtuo.teamachine.dao.accessor.shop;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.shop.ShopMapper;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import com.langtuo.teamachine.dao.query.shop.ShopQuery;
import com.langtuo.teamachine.internal.constant.CommonConsts;
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

    public ShopPO getByShopCode(String tenantCode, String shopCode) {
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

    public ShopPO getByShopName(String tenantCode, String shopName) {
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

    public List<ShopPO> listByShopGroupCode(String tenantCode, String shopGroupCode) {
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

    public List<ShopPO> listByShopGroupCode(String tenantCode, List<String> shopGroupCodeList) {
        // 首先访问缓存
        List<ShopPO> cachedList = getCacheListByOrgNameList(tenantCode, shopGroupCodeList);
        if (cachedList != null) {
            return cachedList;
        }

        List<ShopPO> list = mapper.selectListByShopGroupCodeList(tenantCode, shopGroupCodeList);

        // 设置缓存
        setCacheListByOrgNameList(tenantCode, shopGroupCodeList, list);
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
        if (inserted == CommonConsts.INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getShopGroupCode());
            deleteCacheCount(po.getTenantCode(), po.getShopGroupCode());
        }
        return inserted;
    }

    public int update(ShopPO po) {
        ShopPO exist = mapper.selectOne(po.getTenantCode(), po.getShopCode(), po.getShopName());
        if (exist == null) {
            return CommonConsts.NUM_ZERO;
        }

        int updated = mapper.update(po);
        if (updated == CommonConsts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getShopCode(), po.getShopName());
            deleteCacheList(po.getTenantCode(), po.getShopGroupCode());
            deleteCacheCount(exist.getTenantCode(), exist.getShopGroupCode());
            deleteCacheCount(po.getTenantCode(), po.getShopGroupCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String shopCode) {
        ShopPO po = getByShopCode(tenantCode, shopCode);
        if (po == null) {
            return CommonConsts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, shopCode);
        if (deleted == CommonConsts.DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, po.getShopCode(), po.getShopName());
            deleteCacheList(tenantCode, po.getShopGroupCode());
            deleteCacheCount(po.getTenantCode(), po.getShopGroupCode());
        }
        return deleted;
    }

    public int countByShopGroupCode(String tenantCode, String shopGroupCode) {
        // 首先访问缓存
        Integer cached = getCacheCount(tenantCode, shopGroupCode);
        if (cached != null) {
            return cached;
        }

        int count = mapper.countByShopGroupCode(tenantCode, shopGroupCode);

        setCacheCount(tenantCode, shopGroupCode, count);
        return count;
    }

    private String getCacheKey(String tenantCode, String shopCode, String shopName) {
        return "shopAcc-" + tenantCode + "-" + shopCode + "-" + shopName;
    }

    private String getCacheListKey(String tenantCode, String shopGroupCode) {
        return "shopAcc-" + tenantCode + "-" +shopGroupCode;
    }

    private String getCacheListKeyByOrgNameList(String tenantCode, List<String> shopGroupCodeList) {
        String key = "shopAcc-" + tenantCode;
        for (String shopGroupCode : shopGroupCodeList) {
            key = key + "-" + shopGroupCode;
        }
        return key;
    }

    private String getCacheCountKey(String tenantCode, String shopGroupCode) {
        return "shopAcc-cnt-" + tenantCode + "-" + shopGroupCode;
    }

    private Integer getCacheCount(String tenantCode, String shopGroupCode) {
        String key = getCacheCountKey(tenantCode, shopGroupCode);
        Object cached = redisManager.getValue(key);
        Integer count = (Integer) cached;
        return count;
    }

    private void setCacheCount(String tenantCode, String shopGroupCode, Integer count) {
        String key = getCacheCountKey(tenantCode, shopGroupCode);
        redisManager.setValue(key, count);
    }

    private List<ShopPO> getCacheListByOrgNameList(String tenantCode, List<String> shopGroupCodeList) {
        String key = getCacheListKeyByOrgNameList(tenantCode, shopGroupCodeList);
        Object cached = redisManager.getValue(key);
        List<ShopPO> poList = (List<ShopPO>) cached;
        return poList;
    }

    private void setCacheListByOrgNameList(String tenantCode, List<String> orgNameList, List<ShopPO> poList) {
        String key = getCacheListKeyByOrgNameList(tenantCode, orgNameList);
        redisManager.setValue(key, poList);
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

    private void deleteCacheCount(String tenantCode, String shopGroupCode) {
        redisManager.deleteKey(getCacheCountKey(tenantCode, shopGroupCode));
    }
}
