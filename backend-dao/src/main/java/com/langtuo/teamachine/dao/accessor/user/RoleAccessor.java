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

    public int insert(RolePO rolePO) {
        return mapper.insert(rolePO);
    }

    public int update(RolePO rolePO) {
        int updated = mapper.update(rolePO);
        if (updated == 1) {
            deleteCacheAll(rolePO.getTenantCode(), rolePO.getRoleCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String roleCode) {
        int deleted = mapper.delete(tenantCode, roleCode);
        if (deleted == 1) {
            deleteCacheAll(tenantCode, roleCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String roleCode) {
        return "role_acc_" + tenantCode + "-" + roleCode;
    }

    private String getCacheListKey(String tenantCode) {
        return "role_acc_" + tenantCode;
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

    private void deleteCacheAll(String tenantCode, String roleCode) {
        redisManager.deleteKey(getCacheKey(tenantCode, roleCode));
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}