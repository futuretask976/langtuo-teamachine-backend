package com.langtuo.teamachine.dao.accessor.drink;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.drink.SpecMapper;
import com.langtuo.teamachine.dao.po.drink.SpecPO;
import com.langtuo.teamachine.dao.query.drink.SpecQuery;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SpecAccessor {
    @Resource
    private SpecMapper mapper;

    @Resource
    private RedisManager redisManager;

    public SpecPO selectOneBySpecCode(String tenantCode, String specCode) {
        // 首先访问缓存
        SpecPO cached = getCache(tenantCode, specCode, null);
        if (cached != null) {
            return cached;
        }

        SpecPO po = mapper.selectOne(tenantCode, specCode, null);

        // 设置缓存
        setCache(tenantCode, specCode, null, po);
        return po;
    }

    public SpecPO selectOneBySpecName(String tenantCode, String specName) {
        // 首先访问缓存
        SpecPO cached = getCache(tenantCode, null, specName);
        if (cached != null) {
            return cached;
        }

        SpecPO po = mapper.selectOne(tenantCode, null, specName);

        // 设置缓存
        setCache(tenantCode, null, specName, po);
        return po;
    }

    public List<SpecPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<SpecPO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }
        
        List<SpecPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<SpecPO> search(String tenantCode, String specCode, String specName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        SpecQuery query = new SpecQuery();
        query.setTenantCode(tenantCode);
        query.setSpecCode(StringUtils.isBlank(specCode) ? null : specCode);
        query.setSpecName(StringUtils.isBlank(specName) ? null : specName);
        List<SpecPO> list = mapper.search(query);

        PageInfo<SpecPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(SpecPO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getSpecCode(), po.getSpecName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(SpecPO po) {
        int updated = mapper.update(po);
        if (updated == CommonConsts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getSpecCode(), po.getSpecName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int deleteBySpecCode(String tenantCode, String specCode) {
        SpecPO po = selectOneBySpecCode(tenantCode, specCode);
        if (po == null) {
            return CommonConsts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, specCode);
        if (deleted == CommonConsts.DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, po.getSpecCode(), po.getSpecName());
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String specCode, String specName) {
        return "specAcc-" + tenantCode + "-" + specCode + "-" + specName;
    }

    private String getCacheListKey(String tenantCode) {
        return "specAcc-" + tenantCode;
    }

    private SpecPO getCache(String tenantCode, String specCode, String specName) {
        String key = getCacheKey(tenantCode, specCode, specName);
        Object cached = redisManager.getValue(key);
        SpecPO po = (SpecPO) cached;
        return po;
    }

    private List<SpecPO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<SpecPO> poList = (List<SpecPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<SpecPO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String specCode, String specName, SpecPO po) {
        String key = getCacheKey(tenantCode, specCode, specName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String specCode, String specName) {
        redisManager.deleteKey(getCacheKey(tenantCode, specCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, specName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
