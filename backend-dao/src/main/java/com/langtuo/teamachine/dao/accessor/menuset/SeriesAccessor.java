package com.langtuo.teamachine.dao.accessor.menuset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.menuset.SeriesMapper;
import com.langtuo.teamachine.dao.po.menuset.SeriesPO;
import com.langtuo.teamachine.dao.query.menuset.SeriesQuery;
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

    public SeriesPO selectOneByCode(String tenantCode, String seriesCode) {
        // 首先访问缓存
        SeriesPO cached = getCachedSeries(tenantCode, seriesCode, null);
        if (cached != null) {
            return cached;
        }

        SeriesPO po = mapper.selectOne(tenantCode, seriesCode, null);

        // 设置缓存
        setCachedSeries(tenantCode, seriesCode, null, po);
        return po;
    }

    public SeriesPO selectOneByName(String tenantCode, String seriesName) {
        // 首先访问缓存
        SeriesPO cached = getCachedSeries(tenantCode, null, seriesName);
        if (cached != null) {
            return cached;
        }

        SeriesPO po = mapper.selectOne(tenantCode, null, seriesName);

        // 设置缓存
        setCachedSeries(tenantCode, null, seriesName, po);
        return po;
    }

    public List<SeriesPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<SeriesPO> cachedList = getCachedSeriesList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<SeriesPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCachedSeriesList(tenantCode, list);
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

    public int insert(SeriesPO seriesPO) {
        return mapper.insert(seriesPO);
    }

    public int update(SeriesPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCachedSeries(po.getTenantCode(), po.getSeriesCode(), null);
            deleteCachedSeries(po.getTenantCode(), null, po.getSeriesName());
        }
        return updated;
    }

    public int delete(String tenantCode, String seriesCode) {
        int deleted = mapper.delete(tenantCode, seriesCode);
        if (deleted == 1) {
            // TODO 需要想办法删除用name缓存的对象
            deleteCachedSeries(tenantCode, seriesCode, null);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String seriesCode, String seriesName) {
        return "series_acc_" + tenantCode + "-" + seriesCode + "-" + seriesName;
    }

    private String getCacheKey(String tenantCode) {
        return "series_acc_" + tenantCode;
    }

    private SeriesPO getCachedSeries(String tenantCode, String seriesCode, String seriesName) {
        String key = getCacheKey(tenantCode, seriesCode, seriesName);
        Object cached = redisManager.getValue(key);
        SeriesPO po = (SeriesPO) cached;
        return po;
    }

    private List<SeriesPO> getCachedSeriesList(String tenantCode) {
        String key = getCacheKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<SeriesPO> poList = (List<SeriesPO>) cached;
        return poList;
    }

    private void setCachedSeriesList(String tenantCode, List<SeriesPO> poList) {
        String key = getCacheKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCachedSeries(String tenantCode, String seriesCode, String seriesName, SeriesPO po) {
        String key = getCacheKey(tenantCode, seriesCode, seriesName);
        redisManager.setValue(key, po);
    }

    private void deleteCachedSeries(String tenantCode, String seriesCode, String seriesName) {
        redisManager.deleteKey(getCacheKey(tenantCode, seriesCode, seriesName));
        redisManager.deleteKey(getCacheKey(tenantCode));
    }
}
