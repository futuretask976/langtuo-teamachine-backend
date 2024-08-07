package com.langtuo.teamachine.dao.accessor.drink;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.drink.TeaMapper;
import com.langtuo.teamachine.dao.po.drink.TeaPO;
import com.langtuo.teamachine.dao.query.drink.TeaQuery;
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

    public TeaPO selectOneByCode(String tenantCode, String teaCode) {
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

    public TeaPO selectOneByName(String tenantCode, String teaName) {
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

    public List<TeaPO> selectList(String tenantCode) {
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
        if (inserted == 1) {
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(TeaPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCacheOne(po.getTenantCode(), po.getTeaCode(), po.getTeaName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String teaCode) {
        TeaPO po = selectOneByCode(tenantCode, teaCode);
        if (po == null) {
            return 0;
        }

        int deleted = mapper.delete(tenantCode, teaCode);
        if (deleted == 1) {
            deleteCacheOne(tenantCode, po.getTeaCode(), po.getTeaName());
            deleteCacheList(tenantCode);
        }
        return deleted;
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
}
