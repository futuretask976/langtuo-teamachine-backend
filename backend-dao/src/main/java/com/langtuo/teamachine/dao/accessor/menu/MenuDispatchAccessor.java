package com.langtuo.teamachine.dao.accessor.menu;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DBOpeConts;
import com.langtuo.teamachine.dao.mapper.menu.MenuDispatchMapper;
import com.langtuo.teamachine.dao.po.menu.MenuDispatchPO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MenuDispatchAccessor {
    @Resource
    private MenuDispatchMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<MenuDispatchPO> selectListByMenuCode(String tenantCode, String menuCode) {
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

    public List<MenuDispatchPO> selectListByShopGroupCode(String tenantCode, String shopGroupCode) {
        List<MenuDispatchPO> cached = getCacheListByShopGroupCode(tenantCode, shopGroupCode);
        if (cached != null) {
            return cached;
        }

        List<MenuDispatchPO> list = mapper.selectListByShopGroupCode(tenantCode, shopGroupCode);

        setCacheListByShopGroupCode(tenantCode, shopGroupCode, list);
        return list;
    }

    public int insert(MenuDispatchPO po) {
        int inserted = mapper.insert(po);
        if (inserted == DBOpeConts.INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getMenuCode(), po.getShopGroupCode());
        }
        return inserted;
    }

    public int update(MenuDispatchPO po) {
        int updated = mapper.update(po);
        if (updated == DBOpeConts.UPDATED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getMenuCode(), po.getShopGroupCode());
        }
        return updated;
    }

    public int deleteByMenuCode(String tenantCode, String menuCode) {
        List<MenuDispatchPO> existList = selectListByMenuCode(tenantCode, menuCode);
        if (CollectionUtils.isEmpty(existList)) {
            return DBOpeConts.DELETED_ZERO_ROW;
        }
        String shopGroupCode = existList.get(0).getShopGroupCode();

        int deleted = mapper.delete(tenantCode, menuCode);
        if (deleted == DBOpeConts.DELETED_ONE_ROW) {
            deleteCacheList(tenantCode, menuCode, shopGroupCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String menuCode) {
        return "menuDispatchAcc-" + tenantCode + "-" + menuCode;
    }

    private String getCacheListKeyByShopGroupCode(String tenantCode, String shopGroupCode) {
        return "menuDispatchAcc-byShopGroupCode-" + tenantCode + "-" + shopGroupCode;
    }

    private List<MenuDispatchPO> getCacheList(String tenantCode, String menuCode) {
        String key = getCacheListKey(tenantCode, menuCode);
        Object cached = redisManager.getValue(key);
        List<MenuDispatchPO> poList = (List<MenuDispatchPO>) cached;
        return poList;
    }

    private List<MenuDispatchPO> getCacheListByShopGroupCode(String tenantCode, String shopGroupCode) {
        String key = getCacheListKey(tenantCode, shopGroupCode);
        Object cached = redisManager.getValue(key);
        List<MenuDispatchPO> poList = (List<MenuDispatchPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String menuCode, List<MenuDispatchPO> poList) {
        String key = getCacheListKey(tenantCode, menuCode);
        redisManager.setValue(key, poList);
    }

    private void setCacheListByShopGroupCode(String tenantCode, String shopGroupCode, List<MenuDispatchPO> poList) {
        String key = getCacheListKeyByShopGroupCode(tenantCode, shopGroupCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String menuCode, String shopGroupCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, menuCode));
        redisManager.deleteKey(getCacheListKeyByShopGroupCode(tenantCode, shopGroupCode));
    }
}
