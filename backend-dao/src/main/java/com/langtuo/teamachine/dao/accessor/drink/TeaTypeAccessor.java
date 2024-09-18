package com.langtuo.teamachine.dao.accessor.drink;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.drink.TeaTypeMapper;
import com.langtuo.teamachine.dao.po.drink.TeaTypePO;
import com.langtuo.teamachine.dao.query.drink.TeaTypeQuery;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TeaTypeAccessor {
    @Resource
    private TeaTypeMapper mapper;

    @Resource
    private RedisManager redisManager;

    public TeaTypePO getByTeaTypeCode(String tenantCode, String teaTypeCode) {
        // 首先访问缓存
        TeaTypePO cached = getCache(tenantCode, teaTypeCode, null);
        if (cached != null) {
            return cached;
        }

        TeaTypePO po = mapper.selectOne(tenantCode, teaTypeCode, null);

        // 设置缓存
        setCache(tenantCode, teaTypeCode, null, po);
        return po;
    }

    public TeaTypePO getByTeaName(String tenantCode, String teaName) {
        // 首先访问缓存
        TeaTypePO cached = getCache(tenantCode, null, teaName);
        if (cached != null) {
            return cached;
        }

        TeaTypePO po = mapper.selectOne(tenantCode, null, teaName);

        // 设置缓存
        setCache(tenantCode, null, teaName, po);
        return po;
    }

    public List<TeaTypePO> list(String tenantCode) {
        // 首先访问缓存
        List<TeaTypePO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<TeaTypePO> list = mapper.selectList(tenantCode);

        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<TeaTypePO> search(String tenantCode, String teaTypeCode, String teaName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TeaTypeQuery query = new TeaTypeQuery();
        query.setTenantCode(tenantCode);
        query.setTeaTypeName(StringUtils.isBlank(teaName) ? null : teaName);
        query.setTeaTypeCode(StringUtils.isBlank(teaTypeCode) ? null : teaTypeCode);
        List<TeaTypePO> list = mapper.search(query);

        PageInfo<TeaTypePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(TeaTypePO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.DB_INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getTeaTypeCode(), po.getTeaTypeName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(TeaTypePO po) {
        int updated = mapper.update(po);
        if (updated == CommonConsts.DB_UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getTeaTypeCode(), po.getTeaTypeName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int deleteByTeaTypeCode(String tenantCode, String teaTypeCode) {
        TeaTypePO po = getByTeaTypeCode(tenantCode, teaTypeCode);
        if (po == null) {
            return CommonConsts.DB_DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, teaTypeCode);
        if (deleted == CommonConsts.DB_DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, po.getTeaTypeCode(), po.getTeaTypeName());
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String teaTypeCode, String teaName) {
        return "teaTypeAcc-" + tenantCode + "-" + teaTypeCode + "-" + teaName;
    }

    private String getCacheListKey(String tenantCode) {
        return "teaTypeAcc-" + tenantCode;
    }

    private TeaTypePO getCache(String tenantCode, String teaTypeCode, String teaName) {
        String key = getCacheKey(tenantCode, teaTypeCode, teaName);
        Object cached = redisManager.getValue(key);
        TeaTypePO po = (TeaTypePO) cached;
        return po;
    }

    private List<TeaTypePO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<TeaTypePO> poList = (List<TeaTypePO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<TeaTypePO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String teaTypeCode, String teaName, TeaTypePO po) {
        String key = getCacheKey(tenantCode, teaTypeCode, teaName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String teaTypeCode, String teaTypeName) {
        redisManager.deleteKey(getCacheKey(tenantCode, teaTypeCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, teaTypeName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
