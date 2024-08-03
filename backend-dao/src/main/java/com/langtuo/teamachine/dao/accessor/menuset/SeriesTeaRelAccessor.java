package com.langtuo.teamachine.dao.accessor.menuset;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.menuset.SeriesTeaRelMapper;
import com.langtuo.teamachine.dao.po.menuset.SeriesTeaRelPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SeriesTeaRelAccessor {
    @Resource
    private SeriesTeaRelMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<SeriesTeaRelPO> selectList(String tenantCode, String seriesCode) {
        // 首先访问缓存
        List<SeriesTeaRelPO> cachedList = getCachedSeriesList(tenantCode, seriesCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<SeriesTeaRelPO> list = mapper.selectList(tenantCode, seriesCode);

        // 设置缓存
        setCachedSeriesList(tenantCode, seriesCode, list);
        return list;
    }

    public int insert(SeriesTeaRelPO seriesPO) {
        return mapper.insert(seriesPO);
    }

    public int delete(String tenantCode, String seriesCode) {
        int deleted = mapper.delete(tenantCode, seriesCode);
        if (deleted == 1) {
            // TODO 需要想办法删除用name缓存的对象
            deleteCachedSeries(tenantCode, seriesCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String seriesCode) {
        return "series_tea_rel_acc_" + tenantCode + "-" + seriesCode;
    }

    private List<SeriesTeaRelPO> getCachedSeriesList(String tenantCode, String seriesCode) {
        String key = getCacheKey(tenantCode, seriesCode);
        Object cached = redisManager.getValue(key);
        List<SeriesTeaRelPO> poList = (List<SeriesTeaRelPO>) cached;
        return poList;
    }

    private void setCachedSeriesList(String tenantCode, String seriesCode, List<SeriesTeaRelPO> poList) {
        String key = getCacheKey(tenantCode, seriesCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCachedSeries(String tenantCode, String seriesCode) {
        redisManager.deleteKey(getCacheKey(tenantCode, seriesCode));
    }
}
