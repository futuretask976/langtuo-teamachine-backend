package com.langtuo.teamachine.dao.accessor.drink;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.drink.TeaMapper;
import com.langtuo.teamachine.dao.po.drink.TeaPO;
import com.langtuo.teamachine.dao.query.drink.TeaQuery;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TeaAccessor {
    @Resource
    private TeaMapper mapper;

    @Resource
    private RedisManager redisManager;

    public TeaPO getByTeaCode(String tenantCode, String teaCode) {
        // 首先访问缓存
        TeaPO cached = getCache(tenantCode, teaCode, null);
        if (cached != null) {
            return cached;
        }

        TeaPO po = mapper.selectOne(tenantCode, teaCode, null);

        // 设置缓存
        setCachedTea(tenantCode, teaCode, null, po);
        return po;
    }

    public TeaPO getByTeaName(String tenantCode, String teaName) {
        // 首先访问缓存
        TeaPO cached = getCache(tenantCode, null, teaName);
        if (cached != null) {
            return cached;
        }

        TeaPO po = mapper.selectOne(tenantCode, null, teaName);

        // 设置缓存
        setCachedTea(tenantCode, null, teaName, po);
        return po;
    }

    public List<TeaPO> list(String tenantCode) {
        // 首先访问缓存
        List<TeaPO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<TeaPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<TeaPO> search(String tenantCode, String teaCode, String teaName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TeaQuery query = new TeaQuery();
        query.setTenantCode(tenantCode);
        query.setTeaName(StringUtils.isBlank(teaName) ? null : teaName);
        query.setTeaCode(StringUtils.isBlank(teaCode) ? null : teaCode);
        List<TeaPO> list = mapper.search(query);

        PageInfo<TeaPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(TeaPO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode());
            deleteCacheCount(po.getTenantCode(), po.getTeaTypeCode());
        }
        return inserted;
    }

    public int update(TeaPO po) {
        int updated = mapper.update(po);
        if (updated == CommonConsts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getTeaCode(), po.getTeaName());
            deleteCacheList(po.getTenantCode());
            deleteCacheCount(po.getTenantCode(), po.getTeaTypeCode());
        }
        return updated;
    }

    public int deleteByTeaCode(String tenantCode, String teaCode) {
        TeaPO po = getByTeaCode(tenantCode, teaCode);
        if (po == null) {
            return CommonConsts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, teaCode);
        if (deleted == CommonConsts.DELETED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getTeaCode(), po.getTeaName());
            deleteCacheList(po.getTenantCode());
            deleteCacheCount(po.getTenantCode(), po.getTeaTypeCode());
        }
        return deleted;
    }

    public int countByTeaTypeCode(String tenantCode, String teaTypeCode) {
        // 首先访问缓存
        Integer cached = getCacheCount(tenantCode, teaTypeCode);
        if (cached != null) {
            return cached;
        }

        int count = mapper.countByTeaTypeCode(tenantCode, teaTypeCode);

        setCacheCount(tenantCode, teaTypeCode, count);
        return count;
    }

    private String getCacheCountKey(String tenantCode, String shopGroupCode) {
        return "teaAcc-cnt-" + tenantCode + "-" + shopGroupCode;
    }

    private Integer getCacheCount(String tenantCode, String teaTypeCode) {
        String key = getCacheCountKey(tenantCode, teaTypeCode);
        Object cached = redisManager.getValue(key);
        Integer count = (Integer) cached;
        return count;
    }

    private void setCacheCount(String tenantCode, String teaTypeCode, Integer count) {
        String key = getCacheCountKey(tenantCode, teaTypeCode);
        redisManager.setValue(key, count);
    }

    private String getCacheKey(String tenantCode, String teaCode, String teaName) {
        return "teaAcc-" + tenantCode + "-" + teaCode + "-" + teaName;
    }

    private String getCacheListKey(String tenantCode) {
        return "teaAcc-" + tenantCode;
    }

    private TeaPO getCache(String tenantCode, String teaCode, String teaName) {
        String key = getCacheKey(tenantCode, teaCode, teaName);
        Object cached = redisManager.getValue(key);
        TeaPO po = (TeaPO) cached;
        return po;
    }

    private List<TeaPO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<TeaPO> poList = (List<TeaPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<TeaPO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCachedTea(String tenantCode, String teaCode, String teaName, TeaPO po) {
        String key = getCacheKey(tenantCode, teaCode, teaName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String teaCode, String teaName) {
        redisManager.deleteKey(getCacheKey(tenantCode, teaCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, teaName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }

    private void deleteCacheCount(String tenantCode, String teaTypeCode) {
        redisManager.deleteKey(getCacheCountKey(tenantCode, teaTypeCode));
    }
}
