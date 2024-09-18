package com.langtuo.teamachine.dao.accessor.menu;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.menu.SeriesTeaRelMapper;
import com.langtuo.teamachine.dao.po.menu.SeriesTeaRelPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SeriesTeaRelAccessor {
    @Resource
    private SeriesTeaRelMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<SeriesTeaRelPO> listBySeriesCode(String tenantCode, String seriesCode) {
        // 首先访问缓存
        List<SeriesTeaRelPO> cachedList = getCacheList(tenantCode, seriesCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<SeriesTeaRelPO> list = mapper.selectList(tenantCode, seriesCode);

        // 设置缓存
        setCacheList(tenantCode, seriesCode, list);
        return list;
    }

    public int countByTeaCode(String tenantCode, String teaCode) {
        int count = mapper.countByTeaCode(tenantCode, teaCode);
        return count;
    }

    public int insert(SeriesTeaRelPO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.DB_INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getSeriesCode());
        }
        return inserted;
    }

    public int deleteBySeriesCode(String tenantCode, String seriesCode) {
        int deleted = mapper.delete(tenantCode, seriesCode);
        if (deleted > CommonConsts.DB_DELETED_ZERO_ROW) {
            deleteCacheList(tenantCode, seriesCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String seriesCode) {
        return "seriesTeaRelAcc-" + tenantCode + "-" + seriesCode;
    }

    private List<SeriesTeaRelPO> getCacheList(String tenantCode, String seriesCode) {
        String key = getCacheListKey(tenantCode, seriesCode);
        Object cached = redisManager.getValue(key);
        List<SeriesTeaRelPO> poList = (List<SeriesTeaRelPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String seriesCode, List<SeriesTeaRelPO> poList) {
        String key = getCacheListKey(tenantCode, seriesCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String seriesCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, seriesCode));
    }
}
