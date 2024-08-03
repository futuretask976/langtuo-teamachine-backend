package com.langtuo.teamachine.dao.accessor.shopset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.shopset.ShopMapper;
import com.langtuo.teamachine.dao.po.shopset.ShopGroupPO;
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

    @Resource
    private RedisManager redisManager;

    public ShopPO selectOneByCode(String tenantCode, String shopCode) {
        // 首先访问缓存
        ShopPO cached = getCachedShop(tenantCode, shopCode, null);
        if (cached != null) {
            return cached;
        }

        ShopPO po = mapper.selectOne(tenantCode, shopCode, null);

        // 设置缓存
        setCachedShop(tenantCode, shopCode, null, po);
        return po;
    }

    public ShopPO selectOneByName(String tenantCode, String shopName) {
        // 首先访问缓存
        ShopPO cached = getCachedShop(tenantCode, null, shopName);
        if (cached != null) {
            return cached;
        }

        ShopPO po = mapper.selectOne(tenantCode, null, shopName);

        // 设置缓存
        setCachedShop(tenantCode, null, shopName, po);
        return po;
    }

    public List<ShopPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<ShopPO> cachedList = getCachedShopList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<ShopPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCachedShopList(tenantCode, list);
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
        int updated = mapper.update(shopPO);
        if (updated == 1) {
            deleteCachedShop(shopPO.getTenantCode(), shopPO.getShopCode(), null);
            deleteCachedShop(shopPO.getTenantCode(), null, shopPO.getShopName());
        }
        return updated;
    }

    public int delete(String tenantCode, String shopCode) {
        int deleted = mapper.delete(tenantCode, shopCode);
        if (deleted == 1) {
            // TODO 需要想办法删除用name缓存的对象
            deleteCachedShop(tenantCode, shopCode, null);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String shopCode, String shopName) {
        return "shop_acc_" + tenantCode + "-" + shopCode + "-" + shopName;
    }

    private String getCacheKey(String tenantCode) {
        return "shop_acc_" + tenantCode;
    }

    private ShopPO getCachedShop(String tenantCode, String shopCode, String shopName) {
        String key = getCacheKey(tenantCode, shopCode, shopName);
        Object cached = redisManager.getValue(key);
        ShopPO po = (ShopPO) cached;
        return po;
    }

    private List<ShopPO> getCachedShopList(String tenantCode) {
        String key = getCacheKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<ShopPO> poList = (List<ShopPO>) cached;
        return poList;
    }

    private void setCachedShopList(String tenantCode, List<ShopPO> poList) {
        String key = getCacheKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCachedShop(String tenantCode, String shopCode, String shopName, ShopPO po) {
        String key = getCacheKey(tenantCode, shopCode, shopName);
        redisManager.setValue(key, po);
    }

    private void deleteCachedShop(String tenantCode, String shopCode, String shopName) {
        redisManager.deleteKey(getCacheKey(tenantCode, shopCode, shopName));
        redisManager.deleteKey(getCacheKey(tenantCode));
    }
}
