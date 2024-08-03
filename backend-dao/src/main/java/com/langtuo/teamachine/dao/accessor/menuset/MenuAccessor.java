package com.langtuo.teamachine.dao.accessor.menuset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.menuset.MenuMapper;
import com.langtuo.teamachine.dao.po.menuset.MenuPO;
import com.langtuo.teamachine.dao.query.menuset.MenuQuery;
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

    public MenuPO selectOneByCode(String tenantCode, String menuCode) {
        // 首先访问缓存
        MenuPO cached = getCachedMenu(tenantCode, menuCode, null);
        if (cached != null) {
            return cached;
        }

        MenuPO po = mapper.selectOne(tenantCode, menuCode, null);

        // 设置缓存
        setCachedMenu(tenantCode, menuCode, null, po);
        return po;
    }

    public MenuPO selectOneByName(String tenantCode, String menuName) {
        // 首先访问缓存
        MenuPO cached = getCachedMenu(tenantCode, null, menuName);
        if (cached != null) {
            return cached;
        }

        MenuPO po = mapper.selectOne(tenantCode, null, menuName);

        // 设置缓存
        setCachedMenu(tenantCode, null, menuName, po);
        return po;
    }

    public List<MenuPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<MenuPO> cachedList = getCachedMenuList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }
        
        List<MenuPO> list = mapper.selectList(tenantCode);
        
        // 设置缓存
        setCachedMenuList(tenantCode, list);
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
        return mapper.insert(po);
    }

    public int update(MenuPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCachedMenu(po.getTenantCode(), po.getMenuCode(), null);
            deleteCachedMenu(po.getTenantCode(), null, po.getMenuName());
        }
        return updated;
    }

    public int delete(String tenantCode, String menuCode) {
        int deleted = mapper.delete(tenantCode, menuCode);
        if (deleted == 1) {
            // TODO 需要想办法删除用name缓存的对象
            deleteCachedMenu(tenantCode, menuCode, null);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String menuCode, String menuName) {
        return "menu_acc_" + tenantCode + "-" + menuCode + "-" + menuName;
    }

    private String getCacheKey(String tenantCode) {
        return "menu_acc_" + tenantCode;
    }

    private MenuPO getCachedMenu(String tenantCode, String menuCode, String menuName) {
        String key = getCacheKey(tenantCode, menuCode, menuName);
        Object cached = redisManager.getValue(key);
        MenuPO po = (MenuPO) cached;
        return po;
    }

    private List<MenuPO> getCachedMenuList(String tenantCode) {
        String key = getCacheKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<MenuPO> poList = (List<MenuPO>) cached;
        return poList;
    }

    private void setCachedMenuList(String tenantCode, List<MenuPO> poList) {
        String key = getCacheKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCachedMenu(String tenantCode, String menuCode, String menuName, MenuPO po) {
        String key = getCacheKey(tenantCode, menuCode, menuName);
        redisManager.setValue(key, po);
    }

    private void deleteCachedMenu(String tenantCode, String menuCode, String menuName) {
        redisManager.deleteKey(getCacheKey(tenantCode, menuCode, menuName));
        redisManager.deleteKey(getCacheKey(tenantCode));
    }
}
