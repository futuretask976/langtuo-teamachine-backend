package com.langtuo.teamachine.dao.accessor.menu;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DBOpeConts;
import com.langtuo.teamachine.dao.mapper.menu.MenuMapper;
import com.langtuo.teamachine.dao.po.menu.MenuPO;
import com.langtuo.teamachine.dao.query.menu.MenuQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MenuAccessor {
    @Resource
    private MenuMapper mapper;

    @Resource
    private RedisManager redisManager;

    public MenuPO selectOneByMenuCode(String tenantCode, String menuCode) {
        // 首先访问缓存
        MenuPO cached = getCache(tenantCode, menuCode, null);
        if (cached != null) {
            return cached;
        }

        MenuPO po = mapper.selectOne(tenantCode, menuCode, null);

        // 设置缓存
        setCache(tenantCode, menuCode, null, po);
        return po;
    }

    public MenuPO selectOneByMenuName(String tenantCode, String menuName) {
        // 首先访问缓存
        MenuPO cached = getCache(tenantCode, null, menuName);
        if (cached != null) {
            return cached;
        }

        MenuPO po = mapper.selectOne(tenantCode, null, menuName);

        // 设置缓存
        setCache(tenantCode, null, menuName, po);
        return po;
    }

    public List<MenuPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<MenuPO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }
        
        List<MenuPO> list = mapper.selectList(tenantCode, null);
        
        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public List<MenuPO> selectListByMenuCode(String tenantCode, List<String> menuCodeList) {
        // 这里只是在每台机器初始化的时候会调用，所以先不加缓存
        List<MenuPO> list = mapper.selectList(tenantCode, menuCodeList);
        return list;
    }

    public PageInfo<MenuPO> search(String tenantCode, String menuCode, String menuName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        MenuQuery query = new MenuQuery();
        query.setTenantCode(tenantCode);
        query.setMenuName(StringUtils.isBlank(menuName) ? null : menuName);
        query.setMenuCode(StringUtils.isBlank(menuCode) ? null : menuCode);
        List<MenuPO> list = mapper.search(query);

        PageInfo<MenuPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(MenuPO po) {
        int inserted = mapper.insert(po);
        if (inserted == DBOpeConts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getMenuCode(), po.getMenuName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(MenuPO po) {
        int updated = mapper.update(po);
        if (updated == DBOpeConts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getMenuCode(), po.getMenuName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int deleteByMenuCode(String tenantCode, String menuCode) {
        MenuPO po = selectOneByMenuCode(tenantCode, menuCode);
        if (po == null) {
            return DBOpeConts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, menuCode);
        if (deleted == DBOpeConts.DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, po.getMenuCode(), po.getMenuName());
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String menuCode, String menuName) {
        return "menuAcc-" + tenantCode + "-" + menuCode + "-" + menuName;
    }

    private String getCacheListKey(String tenantCode) {
        return "menuAcc-" + tenantCode;
    }

    private MenuPO getCache(String tenantCode, String menuCode, String menuName) {
        String key = getCacheKey(tenantCode, menuCode, menuName);
        Object cached = redisManager.getValue(key);
        MenuPO po = (MenuPO) cached;
        return po;
    }

    private List<MenuPO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<MenuPO> poList = (List<MenuPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<MenuPO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String menuCode, String menuName, MenuPO po) {
        String key = getCacheKey(tenantCode, menuCode, menuName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String menuCode, String menuName) {
        redisManager.deleteKey(getCacheKey(tenantCode, menuCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, menuName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
