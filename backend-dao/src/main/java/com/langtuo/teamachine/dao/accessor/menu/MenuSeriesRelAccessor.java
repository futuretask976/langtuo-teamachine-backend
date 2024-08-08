package com.langtuo.teamachine.dao.accessor.menu;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.menu.MenuSeriesRelMapper;
import com.langtuo.teamachine.dao.po.menu.MenuSeriesRelPO;
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
        List<MenuSeriesRelPO> cachedList = getCacheList(tenantCode, seriesCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<MenuSeriesRelPO> list = mapper.selectList(tenantCode, seriesCode);

        // 设置缓存
        setCacheList(tenantCode, seriesCode, list);
        return list;
    }

    public int insert(MenuSeriesRelPO po) {
        int inserted = mapper.insert(po);
        if (inserted == 1) {
            deleteCacheList(po.getTenantCode(), po.getMenuCode());
        }
        return inserted;
    }

    public int delete(String tenantCode, String seriesCode) {
        int deleted = mapper.delete(tenantCode, seriesCode);
        if (deleted == 1) {
            deleteCacheList(tenantCode, seriesCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String seriesCode) {
        return "menuSeriesRelAcc-" + tenantCode + "-" + seriesCode;
    }

    private List<MenuSeriesRelPO> getCacheList(String tenantCode, String seriesCode) {
        String key = getCacheListKey(tenantCode, seriesCode);
        Object cached = redisManager.getValue(key);
        List<MenuSeriesRelPO> poList = (List<MenuSeriesRelPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String seriesCode, List<MenuSeriesRelPO> poList) {
        String key = getCacheListKey(tenantCode, seriesCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String seriesCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, seriesCode));
    }
}
