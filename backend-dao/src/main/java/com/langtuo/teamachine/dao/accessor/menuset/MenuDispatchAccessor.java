package com.langtuo.teamachine.dao.accessor.menuset;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.menuset.MenuDispatchMapper;
import com.langtuo.teamachine.dao.po.menuset.MenuDispatchPO;
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
        List<MenuDispatchPO> cachedList = getCachedMenuDispatchList(tenantCode, menuCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<MenuDispatchPO> list = mapper.selectList(tenantCode, menuCode);

        // 设置缓存
        setCachedMenuDispatchList(tenantCode, menuCode, list);
        return list;
    }

    public int insert(MenuDispatchPO po) {
        return mapper.insert(po);
    }

    public int update(MenuDispatchPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCachedMenuDispatch(po.getTenantCode(), po.getMenuCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String menuCode) {
        int deleted = mapper.delete(tenantCode, menuCode);
        if (deleted == 1) {
            deleteCachedMenuDispatch(tenantCode, menuCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String menuCode) {
        return "menu_dispatch_acc_" + tenantCode + "-" + menuCode;
    }

    private List<MenuDispatchPO> getCachedMenuDispatchList(String tenantCode, String menuCode) {
        String key = getCacheKey(tenantCode, menuCode);
        Object cached = redisManager.getValue(key);
        List<MenuDispatchPO> poList = (List<MenuDispatchPO>) cached;
        return poList;
    }

    private void setCachedMenuDispatchList(String tenantCode, String menuCode, List<MenuDispatchPO> poList) {
        String key = getCacheKey(tenantCode, menuCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCachedMenuDispatch(String tenantCode, String menuCode) {
        redisManager.deleteKey(getCacheKey(tenantCode, menuCode));
    }
}
