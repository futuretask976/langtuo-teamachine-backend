package com.langtuo.teamachine.dao.accessor.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager4Accessor;
import com.langtuo.teamachine.dao.mapper.user.AdminMapper;
import com.langtuo.teamachine.dao.po.user.AdminPO;
import com.langtuo.teamachine.dao.query.user.AdminQuery;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AdminAccessor {
    @Resource
    private AdminMapper mapper;

    @Resource
    private RedisManager4Accessor redisManager4Accessor;

    public AdminPO getByLoginName(String tenantCode, String loginName) {
        // 超级管理员特殊逻辑
        AdminPO superAdminPO = getSysSuperAdmin(tenantCode, loginName);
        if (superAdminPO != null) {
            return superAdminPO;
        }

        // 首先访问缓存
        AdminPO cached = getCache(tenantCode, loginName);
        if (cached != null) {
            return cached;
        }

        AdminPO po = mapper.selectOne(tenantCode, loginName);

        // 设置缓存
        setCache(tenantCode, loginName, po);
        return po;
    }

    public List<AdminPO> list(String tenantCode) {
        // 首先访问缓存
        List<AdminPO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<AdminPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<AdminPO> search(String tenantCode, String loginName, String roleCode, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        AdminQuery adminQuery = new AdminQuery();
        adminQuery.setTenantCode(tenantCode);
        adminQuery.setLoginName(StringUtils.isBlank(loginName) ? null : loginName);
        adminQuery.setRoleCode(StringUtils.isBlank(roleCode) ? null : roleCode);
        List<AdminPO> list = mapper.search(adminQuery);

        PageInfo<AdminPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(AdminPO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.DB_INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode());
            deleteCacheCountByRoleCode(po.getTenantCode(), po.getRoleCode());
            deleteCacheCountByOrgName(po.getTenantCode(), po.getOrgName());
        }
        return inserted;
    }

    public int update(AdminPO po) {
        AdminPO exist = mapper.selectOne(po.getTenantCode(), po.getLoginName());
        if (exist == null) {
            return CommonConsts.DB_UPDATED_ZERO_ROW;
        }

        int updated = mapper.update(po);
        if (updated == CommonConsts.DB_UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getLoginName());
            deleteCacheList(po.getTenantCode());
            deleteCacheCountByRoleCode(exist.getTenantCode(), exist.getRoleCode());
            deleteCacheCountByOrgName(exist.getTenantCode(), exist.getOrgName());
            deleteCacheCountByRoleCode(po.getTenantCode(), po.getRoleCode());
            deleteCacheCountByOrgName(po.getTenantCode(), po.getOrgName());
        }
        return updated;
    }

    public int deleteByLoginName(String tenantCode, String loginName) {
        AdminPO po = getByLoginName(tenantCode, loginName);
        if (po == null) {
            return CommonConsts.DB_DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, loginName);
        if (deleted == CommonConsts.DB_DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, loginName);
            deleteCacheList(tenantCode);
            deleteCacheCountByRoleCode(tenantCode, po.getRoleCode());
            deleteCacheCountByOrgName(tenantCode, po.getOrgName());
        }
        return deleted;
    }

    public int countByRoleCode(String tenantCode, String roleCode) {
        // 首先访问缓存
        Integer cached = getCacheCountByRoleCode(tenantCode, roleCode);
        if (cached != null) {
            return cached;
        }

        int count = mapper.countByRoleCode(tenantCode, roleCode);

        setCacheCountByRoleCode(tenantCode, roleCode, count);
        return count;
    }

    public int countByOrgName(String tenantCode, String orgName) {
        // 首先访问缓存
        Integer cached = getCacheCountByOrgName(tenantCode, orgName);
        if (cached != null) {
            return cached;
        }

        int count = mapper.countByOrgName(tenantCode, orgName);

        setCacheCountByOrgName(tenantCode, orgName, count);
        return count;
    }

    private String getCacheCountKeyByRoleCode(String tenantCode, String roleCode) {
        return "adminAcc-cnt-roleCode-" + tenantCode + "-" + roleCode;
    }

    private String getCacheCountKeyByOrgName(String tenantCode, String orgName) {
        return "adminAcc-cnt-orgName-" + tenantCode + "-" + orgName;
    }

    private String getCacheKey(String tenantCode, String loginName) {
        return "adminAcc-" + tenantCode + "-" + loginName;
    }

    private String getCacheListKey(String tenantCode) {
        return "adminAcc-" + tenantCode;
    }

    private Integer getCacheCountByRoleCode(String tenantCode, String roleCode) {
        String key = getCacheCountKeyByRoleCode(tenantCode, roleCode);
        Object cached = redisManager4Accessor.getValue(key);
        Integer count = (Integer) cached;
        return count;
    }

    private Integer getCacheCountByOrgName(String tenantCode, String orgName) {
        String key = getCacheCountKeyByOrgName(tenantCode, orgName);
        Object cached = redisManager4Accessor.getValue(key);
        Integer count = (Integer) cached;
        return count;
    }

    private void setCacheCountByRoleCode(String tenantCode, String roleCode, Integer count) {
        String key = getCacheCountKeyByRoleCode(tenantCode, roleCode);
        redisManager4Accessor.setValue(key, count);
    }

    private void setCacheCountByOrgName(String tenantCode, String orgName, Integer count) {
        String key = getCacheCountKeyByOrgName(tenantCode, orgName);
        redisManager4Accessor.setValue(key, count);
    }

    private AdminPO getCache(String tenantCode, String loginName) {
        String key = getCacheKey(tenantCode, loginName);
        Object cached = redisManager4Accessor.getValue(key);
        AdminPO po = (AdminPO) cached;
        return po;
    }

    private List<AdminPO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager4Accessor.getValue(key);
        List<AdminPO> poList = (List<AdminPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<AdminPO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager4Accessor.setValue(key, poList);
    }

    private void setCache(String tenantCode, String loginName, AdminPO po) {
        String key = getCacheKey(tenantCode, loginName);
        redisManager4Accessor.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String loginName) {
        redisManager4Accessor.deleteKey(getCacheKey(tenantCode, loginName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager4Accessor.deleteKey(getCacheListKey(tenantCode));
    }

    private void deleteCacheCountByRoleCode(String tenantCode, String roleCode) {
        redisManager4Accessor.deleteKey(getCacheCountKeyByRoleCode(tenantCode, roleCode));
    }

    private void deleteCacheCountByOrgName(String tenantCode, String orgName) {
        redisManager4Accessor.deleteKey(getCacheCountKeyByOrgName(tenantCode, orgName));
    }

    private AdminPO getSysSuperAdmin(String tenantCode, String loginName) {
        if (!CommonConsts.ADMIN_SYS_SUPER_LOGIN_NAME.equals(loginName)) {
            return null;
        }

        AdminPO po = new AdminPO();
        po.setTenantCode(tenantCode);
        po.setLoginName(CommonConsts.ADMIN_SYS_SUPER_LOGIN_NAME);
        po.setLoginPass(CommonConsts.ADMIN_SYS_SUPER_PASSWORD);
        po.setOrgName(CommonConsts.ORG_NAME_TOP);
        po.setRoleCode(CommonConsts.ROLE_CODE_SYS_SUPER);
        return po;
    }
}
