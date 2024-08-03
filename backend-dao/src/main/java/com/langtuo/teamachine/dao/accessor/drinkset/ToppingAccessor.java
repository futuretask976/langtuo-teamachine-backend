package com.langtuo.teamachine.dao.accessor.drinkset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.drinkset.ToppingMapper;
import com.langtuo.teamachine.dao.po.drinkset.ToppingPO;
import com.langtuo.teamachine.dao.query.drinkset.ToppingQuery;
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

    public ToppingPO selectOneByCode(String tenantCode, String toppingCode) {
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

    public ToppingPO selectOneByName(String tenantCode, String toppingName) {
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
        return mapper.insert(po);
    }

    public int update(ToppingPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCacheAll(po.getTenantCode(), po.getToppingCode(), null);
            deleteCacheAll(po.getTenantCode(), null, po.getToppingName());
        }
        return updated;
    }

    public int delete(String tenantCode, String toppingCode) {
        ToppingPO po = selectOneByCode(tenantCode, toppingCode);
        int deleted = mapper.delete(tenantCode, toppingCode);
        if (deleted == 1) {
            deleteCacheAll(tenantCode, toppingCode, po.getToppingName());
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String toppingCode, String toppingName) {
        return "spec_acc_" + tenantCode + "-" + toppingCode + "-" + toppingName;
    }

    private String getCacheListKey(String tenantCode) {
        return "spec_acc_" + tenantCode;
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

    private void deleteCacheAll(String tenantCode, String toppingCode, String toppingName) {
        redisManager.deleteKey(getCacheKey(tenantCode, toppingCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, toppingName));
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
