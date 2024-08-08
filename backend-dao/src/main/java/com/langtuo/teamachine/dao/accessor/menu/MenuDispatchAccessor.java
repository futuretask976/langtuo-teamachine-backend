package com.langtuo.teamachine.dao.accessor.menu;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.menu.MenuDispatchMapper;
import com.langtuo.teamachine.dao.po.menu.MenuDispatchPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MenuDispatchAccessor {
    @Resource
    private MenuDispatchMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<MenuDispatchPO> selectList(String tenantCode, String menuCode) {
        // 首先访问缓存
        List<MenuDispatchPO> cachedList = getCacheList(tenantCode, menuCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<MenuDispatchPO> list = mapper.selectList(tenantCode, menuCode);

        // 设置缓存
        setCacheList(tenantCode, menuCode, list);
        return list;
    }

    public int insert(MenuDispatchPO po) {
        int inserted = mapper.insert(po);
        if (inserted == 1) {
            deleteCacheList(po.getTenantCode(), po.getMenuCode());
        }
        return inserted;
    }

    public int update(MenuDispatchPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCacheList(po.getTenantCode(), po.getMenuCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String menuCode) {
        int deleted = mapper.delete(tenantCode, menuCode);
        if (deleted == 1) {
            deleteCacheList(tenantCode, menuCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String menuCode) {
        return "menuDispatchAcc-" + tenantCode + "-" + menuCode;
    }

    private List<MenuDispatchPO> getCacheList(String tenantCode, String menuCode) {
        String key = getCacheListKey(tenantCode, menuCode);
        Object cached = redisManager.getValue(key);
        List<MenuDispatchPO> poList = (List<MenuDispatchPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String menuCode, List<MenuDispatchPO> poList) {
        String key = getCacheListKey(tenantCode, menuCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String menuCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, menuCode));
    }
}
