package com.langtuo.teamachine.dao.accessor.drink;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DaoConsts;
import com.langtuo.teamachine.dao.mapper.drink.ToppingTypeMapper;
import com.langtuo.teamachine.dao.po.drink.ToppingTypePO;
import com.langtuo.teamachine.dao.query.drink.ToppingTypeQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ToppingTypeAccessor {
    @Resource
    private ToppingTypeMapper mapper;

    @Resource
    private RedisManager redisManager;

    public ToppingTypePO selectOneByToppingTypeCode(String tenantCode, String toppingTypeCode) {
        // 首先访问缓存
        ToppingTypePO cached = getCache(tenantCode, toppingTypeCode, null);
        if (cached != null) {
            return cached;
        }

        ToppingTypePO po = mapper.selectOne(tenantCode, toppingTypeCode, null);

        // 设置缓存
        setCache(tenantCode, toppingTypeCode, null, po);
        return po;
    }

    public ToppingTypePO selectOneByToppingTypeName(String tenantCode, String toppingTypeName) {
        // 首先访问缓存
        ToppingTypePO cached = getCache(tenantCode, null, toppingTypeName);
        if (cached != null) {
            return cached;
        }

        ToppingTypePO po = mapper.selectOne(tenantCode, null, toppingTypeName);

        // 设置缓存
        setCache(tenantCode, null, toppingTypeName, po);
        return po;
    }

    public List<ToppingTypePO> selectList(String tenantCode) {
        // 首先访问缓存
        List<ToppingTypePO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<ToppingTypePO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<ToppingTypePO> search(String tenantCode, String toppingTypeCode, String toppingTypeName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        ToppingTypeQuery query = new ToppingTypeQuery();
        query.setTenantCode(tenantCode);
        query.setToppingTypeName(StringUtils.isBlank(toppingTypeName) ? null : toppingTypeName);
        query.setToppingTypeCode(StringUtils.isBlank(toppingTypeCode) ? null : toppingTypeCode);
        List<ToppingTypePO> list = mapper.search(query);

        PageInfo<ToppingTypePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(ToppingTypePO po) {
        int inserted = mapper.insert(po);
        if (inserted == DaoConsts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getToppingTypeCode(), po.getToppingTypeName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(ToppingTypePO po) {
        int updated = mapper.update(po);
        if (updated == DaoConsts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getToppingTypeCode(), po.getToppingTypeName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String toppingTypeCode) {
        ToppingTypePO po = selectOneByToppingTypeCode(tenantCode, toppingTypeCode);
        if (po == null) {
            return DaoConsts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, toppingTypeCode);
        if (deleted == DaoConsts.DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, po.getToppingTypeCode(), po.getToppingTypeName());
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String toppingTypeCode, String toppingTypeName) {
        return "toppingTypeAcc-" + tenantCode + "-" + toppingTypeCode + "-" + toppingTypeName;
    }

    private String getCacheListKey(String tenantCode) {
        return "toppingTypeAcc-" + tenantCode;
    }

    private ToppingTypePO getCache(String tenantCode, String toppingTypeCode, String toppingTypeName) {
        String key = getCacheKey(tenantCode, toppingTypeCode, toppingTypeName);
        Object cached = redisManager.getValue(key);
        ToppingTypePO po = (ToppingTypePO) cached;
        return po;
    }

    private List<ToppingTypePO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<ToppingTypePO> poList = (List<ToppingTypePO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<ToppingTypePO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String toppingTypeCode, String toppingTypeName, ToppingTypePO po) {
        String key = getCacheKey(tenantCode, toppingTypeCode, toppingTypeName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String toppingTypeCode, String toppingTypeName) {
        redisManager.deleteKey(getCacheKey(tenantCode, toppingTypeCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, toppingTypeName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
