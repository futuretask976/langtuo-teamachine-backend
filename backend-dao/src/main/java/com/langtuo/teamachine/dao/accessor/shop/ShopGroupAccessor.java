package com.langtuo.teamachine.dao.accessor.shop;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DaoConsts;
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

    public ShopGroupPO selectOneByShopGroupCode(String tenantCode, String shopGroupCode) {
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

    public ShopGroupPO selectOneByShopGroupName(String tenantCode, String shopGroupName) {
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

    public List<ShopGroupPO> selectListByOrgName(String tenantCode, List<String> orgNameList) {
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
        if (inserted == DaoConsts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getShopGroupCode(), po.getShopGroupName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(ShopGroupPO po) {
        int updated = mapper.update(po);
        if (updated == DaoConsts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getShopGroupCode(), po.getShopGroupName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int deleteByShopGroupCode(String tenantCode, String shopGroupCode) {
        ShopGroupPO po = selectOneByShopGroupCode(tenantCode, shopGroupCode);
        if (po == null) {
            return DaoConsts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, shopGroupCode);
        if (deleted == DaoConsts.DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, po.getShopGroupCode(), po.getShopGroupName());
            deleteCacheList(tenantCode);
        }
        return deleted;
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

    private void deleteCacheListByOrgNameList(String tenantCode, List<String> orgNameList) {
        // TODO 需要考虑如何删除缓存，实在不行用redis的hashmap形式
    }
}
