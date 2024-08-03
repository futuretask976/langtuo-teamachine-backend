package com.langtuo.teamachine.dao.accessor.drinkset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.drinkset.SpecMapper;
import com.langtuo.teamachine.dao.po.drinkset.SpecPO;
import com.langtuo.teamachine.dao.query.drinkset.SpecQuery;
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

    public SpecPO selectOneByCode(String tenantCode, String specCode) {
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

    public SpecPO selectOneByName(String tenantCode, String specName) {
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
        return mapper.insert(po);
    }

    public int update(SpecPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCacheAll(po.getTenantCode(), po.getSpecCode(), null);
            deleteCacheAll(po.getTenantCode(), null, po.getSpecName());
        }
        return updated;
    }

    public int delete(String tenantCode, String specCode) {
        SpecPO po = selectOneByCode(tenantCode, specCode);
        int deleted = mapper.delete(tenantCode, specCode);
        if (deleted == 1) {
            deleteCacheAll(tenantCode, specCode, po.getSpecName());
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String specCode, String specName) {
        return "spec_acc_" + tenantCode + "-" + specCode + "-" + specName;
    }

    private String getCacheListKey(String tenantCode) {
        return "spec_acc_" + tenantCode;
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

    private void deleteCacheAll(String tenantCode, String specCode, String specName) {
        redisManager.deleteKey(getCacheKey(tenantCode, specCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, specName));
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
