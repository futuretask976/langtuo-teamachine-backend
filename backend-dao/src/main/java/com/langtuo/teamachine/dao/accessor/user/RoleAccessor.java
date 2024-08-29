package com.langtuo.teamachine.dao.accessor.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DBOpeConts;
import com.langtuo.teamachine.dao.mapper.user.RoleMapper;
import com.langtuo.teamachine.dao.po.user.AdminPO;
import com.langtuo.teamachine.dao.po.user.PermitActPO;
import com.langtuo.teamachine.dao.po.user.RolePO;
import com.langtuo.teamachine.dao.query.user.AdminRoleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleAccessor {
    @Resource
    private RoleMapper mapper;

    @Resource
    private RedisManager redisManager;

    public RolePO selectOneByRoleCode(String tenantCode, String roleCode) {
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

    public RolePO selectOneByRoleName(String tenantCode, String roleName) {
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
        if (inserted == DBOpeConts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getRoleCode(), po.getRoleName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(RolePO po) {
        int updated = mapper.update(po);
        if (updated == DBOpeConts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getRoleCode(), po.getRoleName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int deleteByRoleCode(String tenantCode, String roleCode) {
        RolePO po = selectOneByRoleCode(tenantCode, roleCode);
        if (po == null) {
            return DBOpeConts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, roleCode);
        if (deleted == DBOpeConts.DELETED_ONE_ROW) {
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
        return null;
//        String key = getCacheKey(tenantCode, roleCode, roleName);
//        Object cached = redisManager.getValue(key);
//        RolePO po = (RolePO) cached;
//        return po;
    }

    private List<RolePO> getCacheList(String tenantCode) {
        return null;
//        String key = getCacheListKey(tenantCode);
//        Object cached = redisManager.getValue(key);
//        List<RolePO> poList = (List<RolePO>) cached;
//        return poList;
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

    public RolePO getSysSuperRole(String tenantCode, String roleCode) {
        if (!"SYS_SUPER_ROLE".equals(roleCode)) {
            return null;
        }

        RolePO po = new RolePO();
        po.setRoleCode("SYS_SUPER_ROLE");
        po.setRoleName("SYS_SUPER_ROLE");
        po.setSysReserved(1);
        return po;
    }
}
