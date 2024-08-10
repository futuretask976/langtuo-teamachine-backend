package com.langtuo.teamachine.dao.accessor.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.user.RoleMapper;
import com.langtuo.teamachine.dao.po.user.RolePO;
import com.langtuo.teamachine.dao.query.user.AdminRoleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class RoleAccessor {
    @Resource
    private RoleMapper mapper;

    @Resource
    private RedisManager redisManager;

    public RolePO selectOne(String tenantCode, String roleCode) {
        // 超级管理员特殊逻辑
        RolePO superRole = getSysSuperRole(tenantCode, roleCode);
        if (superRole != null) {
            return superRole;
        }

        // 首先访问缓存
        RolePO cached = getCache(tenantCode, roleCode);
        if (cached != null) {
            return cached;
        }

        RolePO po = mapper.selectOne(tenantCode, roleCode);

        // 设置缓存
        setCache(tenantCode, roleCode, po);
        return po;
    }

    public List<RolePO> selectList(String tenantCode) {
        // 首先访问缓存
        List<RolePO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<RolePO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<RolePO> selectList(String tenantCode, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<RolePO> list = mapper.selectList(tenantCode);

        PageInfo<RolePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public PageInfo<RolePO> search(String tenantCode, String roleName, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        AdminRoleQuery adminRoleQuery = new AdminRoleQuery();
        adminRoleQuery.setTenantCode(tenantCode);
        adminRoleQuery.setRoleName(StringUtils.isBlank(roleName) ? null : roleName);
        List<RolePO> list = mapper.search(adminRoleQuery);

        PageInfo<RolePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(RolePO po) {
        int inserted = mapper.insert(po);
        if (inserted == 1) {
            deleteCacheOne(po.getTenantCode(), po.getRoleCode());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(RolePO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCacheOne(po.getTenantCode(), po.getRoleCode());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String roleCode) {
        int deleted = mapper.delete(tenantCode, roleCode);
        if (deleted == 1) {
            deleteCacheOne(tenantCode, roleCode);
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String roleCode) {
        return "roleAcc-" + tenantCode + "-" + roleCode;
    }

    private String getCacheListKey(String tenantCode) {
        return "roleAcc-" + tenantCode;
    }

    private RolePO getCache(String tenantCode, String roleCode) {
        String key = getCacheKey(tenantCode, roleCode);
        Object cached = redisManager.getValue(key);
        RolePO po = (RolePO) cached;
        return po;
    }

    private List<RolePO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<RolePO> poList = (List<RolePO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<RolePO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String roleCode, RolePO po) {
        String key = getCacheKey(tenantCode, roleCode);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String roleCode) {
        redisManager.deleteKey(getCacheKey(tenantCode, roleCode));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }

    /**
     * 此方法返回系统超级角色，不存储在数据库（因为如果存储数据库，必须指定tenant，而系统超级角色不归属任何teanant）
     * @return
     */
    public RolePO getSysSuperRole(String tenantCode, String roleCode) {
        if (!"SYS_SUPER_ROLE".equals(roleCode)) {
            return null;
        }

        RolePO po = new RolePO();
        po.setTenantCode(tenantCode);
        po.setRoleCode("SYS_SUPER_ROLE");
        po.setRoleName("SYS_SUPER_ROLE");
        po.setSysReserved(1);
        return po;
    }
}
