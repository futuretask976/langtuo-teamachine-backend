package com.langtuo.teamachine.dao.accessor.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.user.RoleMapper;
import com.langtuo.teamachine.dao.po.user.RolePO;
import com.langtuo.teamachine.dao.query.user.AdminRoleQuery;
import com.langtuo.teamachine.internal.constant.CommonConsts;
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

    public RolePO getByRoleCode(String tenantCode, String roleCode) {
        // 超级管理员特殊逻辑
        RolePO superRolePO = getSysSuperRole(tenantCode, roleCode);
        if (superRolePO != null) {
            return superRolePO;
        }

        // 首先访问缓存
        RolePO cached = getCache(tenantCode, roleCode, null);
        if (cached != null) {
            return cached;
        }

        RolePO po = mapper.selectOne(tenantCode, roleCode, null);

        // 设置缓存
        setCache(tenantCode, roleCode, null, po);
        return po;
    }

    public RolePO getByRoleName(String tenantCode, String roleName) {
        // 首先访问缓存
        RolePO cached = getCache(tenantCode, null, roleName);
        if (cached != null) {
            return cached;
        }

        RolePO po = mapper.selectOne(tenantCode, null, roleName);

        // 设置缓存
        setCache(tenantCode, null, roleName, po);
        return po;
    }

    public List<RolePO> search(String tenantCode) {
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
        if (inserted == CommonConsts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getRoleCode(), po.getRoleName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(RolePO po) {
        int updated = mapper.update(po);
        if (updated == CommonConsts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getRoleCode(), po.getRoleName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int deleteByRoleCode(String tenantCode, String roleCode) {
        RolePO po = getByRoleCode(tenantCode, roleCode);
        if (po == null) {
            return CommonConsts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, roleCode);
        if (deleted == CommonConsts.DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, po.getRoleCode(), po.getRoleName());
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String roleCode, String roleName) {
        return "roleAcc-" + tenantCode + "-" + roleCode + "-" +  roleName;
    }

    private String getCacheListKey(String tenantCode) {
        return "roleAcc-" + tenantCode;
    }

    private RolePO getCache(String tenantCode, String roleCode, String roleName) {
        String key = getCacheKey(tenantCode, roleCode, roleName);
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

    private void setCache(String tenantCode, String roleCode, String roleName, RolePO po) {
        String key = getCacheKey(tenantCode, roleCode, roleName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String roleCode, String roleName) {
        redisManager.deleteKey(getCacheKey(tenantCode, roleCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, roleName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }

    private RolePO getSysSuperRole(String tenantCode, String roleCode) {
        if (!CommonConsts.ROLE_CODE_SYS_SUPER.equals(roleCode)) {
            return null;
        }

        RolePO po = new RolePO();
        po.setRoleCode(CommonConsts.ROLE_CODE_SYS_SUPER);
        po.setRoleName(CommonConsts.ROLE_NAME_SYS_SUPER);
        po.setSysReserved(CommonConsts.ROLE_SYS_RESERVED);
        return po;
    }
}
