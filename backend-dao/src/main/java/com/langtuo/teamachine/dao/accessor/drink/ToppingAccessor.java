package com.langtuo.teamachine.dao.accessor.drink;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DaoConsts;
import com.langtuo.teamachine.dao.mapper.drink.ToppingMapper;
import com.langtuo.teamachine.dao.po.drink.ToppingPO;
import com.langtuo.teamachine.dao.query.drink.ToppingQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ToppingAccessor {
    @Resource
    private ToppingMapper mapper;

    @Resource
    private RedisManager redisManager;

    public ToppingPO selectOneByToppingCode(String tenantCode, String toppingCode) {
        // 首先访问缓存
        ToppingPO cached = getCache(tenantCode, toppingCode, null);
        if (cached != null) {
            return cached;
        }

        ToppingPO po = mapper.selectOne(tenantCode, toppingCode, null);

        // 设置缓存
        setCache(tenantCode, toppingCode, null, po);
        return po;
    }

    public ToppingPO selectOneByToppingName(String tenantCode, String toppingName) {
        // 首先访问缓存
        ToppingPO cached = getCache(tenantCode, null, toppingName);
        if (cached != null) {
            return cached;
        }
        
        ToppingPO po = mapper.selectOne(tenantCode, null, toppingName);

        // 设置缓存
        setCache(tenantCode, null, toppingName, po);
        return po;
    }

    public List<ToppingPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<ToppingPO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }
        
        List<ToppingPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<ToppingPO> search(String tenantCode, String toppingCode, String toppingName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        ToppingQuery query = new ToppingQuery();
        query.setTenantCode(tenantCode);
        query.setToppingName(StringUtils.isBlank(toppingName) ? null : toppingName);
        query.setToppingCode(StringUtils.isBlank(toppingCode) ? null : toppingCode);
        List<ToppingPO> list = mapper.search(query);

        PageInfo<ToppingPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(ToppingPO po) {
        int inserted = mapper.insert(po);
        if (inserted == DaoConsts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getToppingCode(), po.getToppingName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(ToppingPO po) {
        int updated = mapper.update(po);
        if (updated == DaoConsts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getToppingCode(), po.getToppingName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int deleteByToppingCode(String tenantCode, String toppingCode) {
        ToppingPO po = selectOneByToppingCode(tenantCode, toppingCode);
        if (po == null) {
            return DaoConsts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, toppingCode);
        if (deleted == DaoConsts.DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, po.getToppingCode(), po.getToppingName());
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    public int countByToppingTypeCode(String tenantCode, String toppingTypeCode) {
        // 首先访问缓存
        Integer cached = getCacheCount(tenantCode, toppingTypeCode);
        if (cached != null) {
            return cached;
        }

        int count = mapper.countByToppingTypeCode(tenantCode, toppingTypeCode);

        setCacheCount(tenantCode, toppingTypeCode, count);
        return count;
    }

    private String getCacheKey(String tenantCode, String toppingCode, String toppingName) {
        return "toppingAcc-" + tenantCode + "-" + toppingCode + "-" + toppingName;
    }

    private String getCacheListKey(String tenantCode) {
        return "toppingAcc-" + tenantCode;
    }

    private String getCacheCountKey(String tenantCode, String toppingTypeCode) {
        return "toppingAcc-cnt-" + tenantCode + "-" + toppingTypeCode;
    }

    private Integer getCacheCount(String tenantCode, String toppingTypeCode) {
        String key = getCacheCountKey(tenantCode, toppingTypeCode);
        Object cached = redisManager.getValue(key);
        Integer count = (Integer) cached;
        return count;
    }

    private void setCacheCount(String tenantCode, String toppingTypeCode, Integer count) {
        String key = getCacheCountKey(tenantCode, toppingTypeCode);
        redisManager.setValue(key, count);
    }

    private ToppingPO getCache(String tenantCode, String toppingCode, String toppingName) {
        String key = getCacheKey(tenantCode, toppingCode, toppingName);
        Object cached = redisManager.getValue(key);
        ToppingPO po = (ToppingPO) cached;
        return po;
    }

    private List<ToppingPO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<ToppingPO> poList = (List<ToppingPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<ToppingPO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String toppingCode, String toppingName, ToppingPO po) {
        String key = getCacheKey(tenantCode, toppingCode, toppingName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String toppingCode, String toppingName) {
        redisManager.deleteKey(getCacheKey(tenantCode, toppingCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, toppingName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
