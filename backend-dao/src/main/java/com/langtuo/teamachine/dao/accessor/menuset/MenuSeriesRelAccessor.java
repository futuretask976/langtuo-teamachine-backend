package com.langtuo.teamachine.dao.accessor.menuset;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.menuset.MenuSeriesRelMapper;
import com.langtuo.teamachine.dao.po.menuset.MenuSeriesRelPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MenuSeriesRelAccessor {
    @Resource
    private MenuSeriesRelMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<MenuSeriesRelPO> selectList(String tenantCode, String seriesCode) {
        // 首先访问缓存
        List<MenuSeriesRelPO> cachedList = getCachedMenuDispatchList(tenantCode, seriesCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<MenuSeriesRelPO> list = mapper.selectList(tenantCode, seriesCode);

        // 设置缓存
        setCachedMenuDispatchList(tenantCode, seriesCode, list);
        return list;
    }

    public int insert(MenuSeriesRelPO menuSeriesRelPO) {
        return mapper.insert(menuSeriesRelPO);
    }

    public int delete(String tenantCode, String seriesCode) {
        int deleted = mapper.delete(tenantCode, seriesCode);
        if (deleted == 1) {
            deleteCachedMenuDispatch(tenantCode, seriesCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String seriesCode) {
        return "menu_series_rel_acc_" + tenantCode + "-" + seriesCode;
    }

    private List<MenuSeriesRelPO> getCachedMenuDispatchList(String tenantCode, String seriesCode) {
        String key = getCacheKey(tenantCode, seriesCode);
        Object cached = redisManager.getValue(key);
        List<MenuSeriesRelPO> poList = (List<MenuSeriesRelPO>) cached;
        return poList;
    }

    private void setCachedMenuDispatchList(String tenantCode, String seriesCode, List<MenuSeriesRelPO> poList) {
        String key = getCacheKey(tenantCode, seriesCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCachedMenuDispatch(String tenantCode, String seriesCode) {
        redisManager.deleteKey(getCacheKey(tenantCode, seriesCode));
    }
}
