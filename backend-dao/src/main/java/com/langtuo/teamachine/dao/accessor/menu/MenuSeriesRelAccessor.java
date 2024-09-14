package com.langtuo.teamachine.dao.accessor.menu;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DaoConsts;
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

    public List<MenuSeriesRelPO> selectListBySeriesCode(String tenantCode, String seriesCode) {
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
        // 首先访问缓存
        Integer cached = getCacheCount(tenantCode, seriesCode);
        if (cached != null) {
            return cached;
        }

        int count = mapper.countBySeriesCode(tenantCode, seriesCode);

        setCacheCount(tenantCode, seriesCode, count);
        return count;
    }

    public int insert(MenuSeriesRelPO po) {
        int inserted = mapper.insert(po);
        if (inserted == DaoConsts.INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getMenuCode());
        }
        return inserted;
    }

    public int deleteByMenuCode(String tenantCode, String menuCode) {
        int deleted = mapper.delete(tenantCode, menuCode);
        if (deleted == DaoConsts.DELETED_ONE_ROW) {
            deleteCacheList(tenantCode, menuCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String seriesCode) {
        return "menuSeriesRelAcc-" + tenantCode + "-" + seriesCode;
    }

    private String getCacheCountKey(String tenantCode, String teaCode) {
        return "menuSeriesRelAcc-cnt-" + tenantCode + "-" + teaCode;
    }

    private Integer getCacheCount(String tenantCode, String seriesCode) {
        String key = getCacheCountKey(tenantCode, seriesCode);
        Object cached = redisManager.getValue(key);
        Integer count = (Integer) cached;
        return count;
    }

    private void setCacheCount(String tenantCode, String seriesCode, Integer count) {
        String key = getCacheCountKey(tenantCode, seriesCode);
        redisManager.setValue(key, count);
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
