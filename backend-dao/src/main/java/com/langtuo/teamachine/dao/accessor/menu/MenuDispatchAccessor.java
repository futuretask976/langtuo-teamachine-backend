package com.langtuo.teamachine.dao.accessor.menu;

import com.langtuo.teamachine.dao.cache.RedisManager4Accessor;
import com.langtuo.teamachine.dao.mapper.menu.MenuDispatchMapper;
import com.langtuo.teamachine.dao.po.menu.MenuDispatchPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MenuDispatchAccessor {
    @Resource
    private MenuDispatchMapper mapper;

    @Resource
    private RedisManager4Accessor redisManager4Accessor;

    public List<MenuDispatchPO> listByMenuCode(String tenantCode, String menuCode) {
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

    public List<MenuDispatchPO> listByShopGroupCode(String tenantCode, String shopGroupCode) {
        List<MenuDispatchPO> list = mapper.selectListByShopGroupCode(tenantCode, shopGroupCode);
        return list;
    }

    public int insert(MenuDispatchPO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.DB_INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getMenuCode());
        }
        return inserted;
    }

    public int update(MenuDispatchPO po) {
        int updated = mapper.update(po);
        if (updated == CommonConsts.DB_UPDATED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getMenuCode());
        }
        return updated;
    }

    public int deleteByMenuCode(String tenantCode, String menuCode) {
        int deleted = mapper.delete(tenantCode, menuCode);
        if (deleted >= CommonConsts.DB_DELETED_ONE_ROW) {
            deleteCacheList(tenantCode, menuCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String menuCode) {
        return "menuDispatchAcc-" + tenantCode + "-" + menuCode;
    }

    private List<MenuDispatchPO> getCacheList(String tenantCode, String menuCode) {
        String key = getCacheListKey(tenantCode, menuCode);
        Object cached = redisManager4Accessor.getValue(key);
        List<MenuDispatchPO> poList = (List<MenuDispatchPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String menuCode, List<MenuDispatchPO> poList) {
        String key = getCacheListKey(tenantCode, menuCode);
        redisManager4Accessor.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String menuCode) {
        redisManager4Accessor.deleteKey(getCacheListKey(tenantCode, menuCode));
    }
}
