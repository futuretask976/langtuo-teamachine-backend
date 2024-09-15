package com.langtuo.teamachine.dao.accessor.menu;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.menu.SeriesMapper;
import com.langtuo.teamachine.dao.po.menu.SeriesPO;
import com.langtuo.teamachine.dao.query.menu.SeriesQuery;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SeriesAccessor {
    @Resource
    private SeriesMapper mapper;

    @Resource
    private RedisManager redisManager;

    public SeriesPO selectOneBySeriesCode(String tenantCode, String seriesCode) {
        // 首先访问缓存
        SeriesPO cached = getCache(tenantCode, seriesCode, null);
        if (cached != null) {
            return cached;
        }

        SeriesPO po = mapper.selectOne(tenantCode, seriesCode, null);

        // 设置缓存
        setCache(tenantCode, seriesCode, null, po);
        return po;
    }

    public SeriesPO selectOneBySeriesName(String tenantCode, String seriesName) {
        // 首先访问缓存
        SeriesPO cached = getCache(tenantCode, null, seriesName);
        if (cached != null) {
            return cached;
        }

        SeriesPO po = mapper.selectOne(tenantCode, null, seriesName);

        // 设置缓存
        setCache(tenantCode, null, seriesName, po);
        return po;
    }

    public List<SeriesPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<SeriesPO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<SeriesPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<SeriesPO> search(String tenantCode, String seriesCode, String seriesName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        SeriesQuery query = new SeriesQuery();
        query.setTenantCode(tenantCode);
        query.setSeriesName(StringUtils.isBlank(seriesName) ? null : seriesName);
        query.setSeriesCode(StringUtils.isBlank(seriesCode) ? null : seriesCode);
        List<SeriesPO> list = mapper.search(query);

        PageInfo<SeriesPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(SeriesPO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getSeriesCode(), po.getSeriesName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(SeriesPO po) {
        int updated = mapper.update(po);
        if (updated == CommonConsts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getSeriesCode(), po.getSeriesName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int deleteBySeriesCode(String tenantCode, String seriesCode) {
        SeriesPO po = selectOneBySeriesCode(tenantCode, seriesCode);
        if (po == null) {
            return CommonConsts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, seriesCode);
        if (deleted == CommonConsts.DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, po.getSeriesCode(), po.getSeriesName());
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String seriesCode, String seriesName) {
        return "seriesAcc-" + tenantCode + "-" + seriesCode + "-" + seriesName;
    }

    private String getCacheListKey(String tenantCode) {
        return "seriesAcc-" + tenantCode;
    }

    private SeriesPO getCache(String tenantCode, String seriesCode, String seriesName) {
        String key = getCacheKey(tenantCode, seriesCode, seriesName);
        Object cached = redisManager.getValue(key);
        SeriesPO po = (SeriesPO) cached;
        return po;
    }

    private List<SeriesPO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<SeriesPO> poList = (List<SeriesPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<SeriesPO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String seriesCode, String seriesName, SeriesPO po) {
        String key = getCacheKey(tenantCode, seriesCode, seriesName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String seriesCode, String seriesName) {
        redisManager.deleteKey(getCacheKey(tenantCode, seriesCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, seriesName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
