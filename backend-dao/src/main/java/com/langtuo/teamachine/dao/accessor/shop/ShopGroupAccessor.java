package com.langtuo.teamachine.dao.accessor.shop;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.shop.ShopGroupMapper;
import com.langtuo.teamachine.dao.po.shop.ShopGroupPO;
import com.langtuo.teamachine.dao.query.shop.ShopGroupQuery;
import com.langtuo.teamachine.internal.constant.CommonConsts;
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

    public ShopGroupPO getByShopGroupCode(String tenantCode, String shopGroupCode) {
        // 首先访问缓存
        ShopGroupPO cached = getCache(tenantCode, shopGroupCode, null);
        if (cached != null) {
            return cached;
        }

        ShopGroupPO po = mapper.selectOne(tenantCode, shopGroupCode, null);

        // 设置缓存
        setCache(tenantCode, shopGroupCode, null, po);
        return po;
    }

    public ShopGroupPO getByShopGroupName(String tenantCode, String shopGroupName) {
        // 首先访问缓存
        ShopGroupPO cached = getCache(tenantCode, null, shopGroupName);
        if (cached != null) {
            return cached;
        }

        ShopGroupPO po = mapper.selectOne(tenantCode, null, shopGroupName);

        // 设置缓存
        setCache(tenantCode, null, shopGroupName, po);
        return po;
    }

    public List<ShopGroupPO> list(String tenantCode) {
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

    public List<ShopGroupPO> listByOrgName(String tenantCode, List<String> orgNameList) {
        // 首先访问缓存
        List<ShopGroupPO> cachedList = getCacheListByOrgNameList(tenantCode, orgNameList);
        if (cachedList != null) {
            return cachedList;
        }

        List<ShopGroupPO> list = mapper.selectListByOrgNameList(tenantCode, orgNameList);

        // 设置缓存
        setCacheListByOrgNameList(tenantCode, orgNameList, list);
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
        if (inserted == CommonConsts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getShopGroupCode(), po.getShopGroupName());
            deleteCacheList(po.getTenantCode());
            deleteCacheCountByOrgName(po.getTenantCode(), po.getOrgName());
        }
        return inserted;
    }

    public int update(ShopGroupPO po) {
        ShopGroupPO exist = mapper.selectOne(po.getTenantCode(), po.getShopGroupCode(), po.getShopGroupName());
        if (exist == null) {
            return CommonConsts.NUM_ZERO;
        }

        int updated = mapper.update(po);
        if (updated == CommonConsts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getShopGroupCode(), po.getShopGroupName());
            deleteCacheList(po.getTenantCode());
            deleteCacheCountByOrgName(exist.getTenantCode(), exist.getOrgName());
            deleteCacheCountByOrgName(po.getTenantCode(), po.getOrgName());
        }
        return updated;
    }

    public int deleteByShopGroupCode(String tenantCode, String shopGroupCode) {
        ShopGroupPO po = getByShopGroupCode(tenantCode, shopGroupCode);
        if (po == null) {
            return CommonConsts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, shopGroupCode);
        if (deleted == CommonConsts.DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, po.getShopGroupCode(), po.getShopGroupName());
            deleteCacheList(tenantCode);
            deleteCacheCountByOrgName(tenantCode, po.getOrgName());
        }
        return deleted;
    }

    public int countByOrgName(String tenantCode, String orgName) {
        // 首先访问缓存
        Integer cached = getCacheCountByOrgName(tenantCode, orgName);
        if (cached != null) {
            return cached;
        }

        int count = mapper.countByOrgName(tenantCode, orgName);

        setCacheCountByOrgName(tenantCode, orgName, count);
        return count;
    }

    private Integer getCacheCountByOrgName(String tenantCode, String orgName) {
        String key = getCacheCountKeyByOrgName(tenantCode, orgName);
        Object cached = redisManager.getValue(key);
        Integer count = (Integer) cached;
        return count;
    }

    private void setCacheCountByOrgName(String tenantCode, String orgName, Integer count) {
        String key = getCacheCountKeyByOrgName(tenantCode, orgName);
        redisManager.setValue(key, count);
    }

    private String getCacheCountKeyByOrgName(String tenantCode, String orgName) {
        return "shopGroupAcc-cnt-orgName-" + tenantCode + "-" + orgName;
    }

    private String getCacheKey(String tenantCode, String shopGroupCode, String shopGroupName) {
        return "shopGroupAcc-" + tenantCode + "-" + shopGroupCode + "-" + shopGroupName;
    }

    private String getCacheListKeyByOrgNameList(String tenantCode, List<String> orgNameList) {
        String key = "shopGroupAcc-" + tenantCode;
        for (String orgName : orgNameList) {
            key = key + "-" + orgName;
        }
        return key;
    }

    private String getCacheListKey(String tenantCode) {
        return "shopGroupAcc-" + tenantCode;
    }

    private ShopGroupPO getCache(String tenantCode, String shopGroupCode, String shopGroupName) {
        String key = getCacheKey(tenantCode, shopGroupCode, shopGroupName);
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

    private void setCache(String tenantCode, String shopGroupCode, String shopGroupName, ShopGroupPO po) {
        String key = getCacheKey(tenantCode, shopGroupCode, shopGroupName);
        redisManager.setValue(key, po);
    }

    private List<ShopGroupPO> getCacheListByOrgNameList(String tenantCode, List<String> orgNameList) {
        String key = getCacheListKeyByOrgNameList(tenantCode, orgNameList);
        Object cached = redisManager.getValue(key);
        List<ShopGroupPO> poList = (List<ShopGroupPO>) cached;
        return poList;
    }

    private void setCacheListByOrgNameList(String tenantCode, List<String> orgNameList, List<ShopGroupPO> poList) {
        String key = getCacheListKeyByOrgNameList(tenantCode, orgNameList);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheOne(String tenantCode, String shopGroupCode, String shopGroupName) {
        redisManager.deleteKey(getCacheKey(tenantCode, shopGroupCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, shopGroupName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }

    private void deleteCacheCountByOrgName(String tenantCode, String orgName) {
        redisManager.deleteKey(getCacheCountKeyByOrgName(tenantCode, orgName));
    }
}
