package com.langtuo.teamachine.dao.accessor.menu;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.menu.MenuSeriesRelMapper;
import com.langtuo.teamachine.dao.po.menu.MenuSeriesRelPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MenuSeriesRelAccessor {
    @Resource
    private MenuSeriesRelMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<MenuSeriesRelPO> listBySeriesCode(String tenantCode, String seriesCode) {
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

    public int countBySeriesCode(String tenantCode, String seriesCode) {
        int count = mapper.countBySeriesCode(tenantCode, seriesCode);
        return count;
    }

    public int insert(MenuSeriesRelPO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.DB_INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getMenuCode());
        }
        return inserted;
    }

    public int deleteByMenuCode(String tenantCode, String menuCode) {
        int deleted = mapper.delete(tenantCode, menuCode);
        if (deleted == CommonConsts.DB_DELETED_ONE_ROW) {
            deleteCacheList(tenantCode, menuCode);
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
