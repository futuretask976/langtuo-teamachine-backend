package com.langtuo.teamachine.dao.accessor.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DaoConsts;
import com.langtuo.teamachine.dao.mapper.user.AdminMapper;
import com.langtuo.teamachine.dao.po.user.AdminPO;
import com.langtuo.teamachine.dao.query.user.AdminQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AdminAccessor {
    @Resource
    private AdminMapper mapper;

    @Resource
    private RedisManager redisManager;

    public AdminPO selectOneByLoginName(String tenantCode, String loginName) {
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

    public List<AdminPO> selectList(String tenantCode) {
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
        if (inserted == DaoConsts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getLoginName());
            deleteCacheList(po.getTenantCode());
            deleteCacheCount(po.getTenantCode(), po.getRoleCode());
        }
        return inserted;
    }

    public int update(AdminPO po) {
        int updated = mapper.update(po);
        if (updated == DaoConsts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getLoginName());
            deleteCacheList(po.getTenantCode());
            deleteCacheCount(po.getTenantCode(), po.getRoleCode());
        }
        return updated;
    }

    public int updatePassword(String tenantCode, String loginName, String loginPass) {
        int updated = mapper.updatePassword(tenantCode, loginName, loginPass);
        if (updated == DaoConsts.UPDATED_ONE_ROW) {
            deleteCacheOne(tenantCode, loginName);
            deleteCacheList(tenantCode);
        }
        return updated;
    }

    public int deleteByLoginName(String tenantCode, String loginName) {
        AdminPO po = selectOneByLoginName(tenantCode, loginName);
        if (po == null) {
            return DaoConsts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, loginName);
        if (deleted == DaoConsts.DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, loginName);
            deleteCacheList(tenantCode);
            deleteCacheCount(tenantCode, po.getRoleCode());
        }
        return deleted;
    }

    public int countByRoleCode(String tenantCode, String roleCode) {
        // 首先访问缓存
        Integer cached = getCacheCount(tenantCode, roleCode);
        if (cached != null) {
            return cached;
        }

        int count = mapper.countByRoleCode(tenantCode, roleCode);

        setCacheCount(tenantCode, roleCode, count);
        return count;
    }

    private String getCacheCountKey(String tenantCode, String roleCode) {
        return "adminAcc-cnt-" + tenantCode + "-" + roleCode;
    }

    private String getCacheKey(String tenantCode, String loginName) {
        return "adminAcc-" + tenantCode + "-" + loginName;
    }

    private String getCacheListKey(String tenantCode) {
        return "adminAcc-" + tenantCode;
    }

    private Integer getCacheCount(String tenantCode, String roleCode) {
        String key = getCacheCountKey(tenantCode, roleCode);
        Object cached = redisManager.getValue(key);
        Integer count = (Integer) cached;
        return count;
    }

    private void setCacheCount(String tenantCode, String roleCode, Integer count) {
        String key = getCacheCountKey(tenantCode, roleCode);
        redisManager.setValue(key, count);
    }

    private AdminPO getCache(String tenantCode, String loginName) {
        String key = getCacheKey(tenantCode, loginName);
        Object cached = redisManager.getValue(key);
        AdminPO po = (AdminPO) cached;
        return po;
    }

    private List<AdminPO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<AdminPO> poList = (List<AdminPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<AdminPO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String loginName, AdminPO po) {
        String key = getCacheKey(tenantCode, loginName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String loginName) {
        redisManager.deleteKey(getCacheKey(tenantCode, loginName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }

    private void deleteCacheCount(String tenantCode, String roleCode) {
        redisManager.deleteKey(getCacheCountKey(tenantCode, roleCode));
    }

    private AdminPO getSysSuperAdmin(String tenantCode, String loginName) {
        if (!DaoConsts.ADMIN_SYS_SUPER_LOGIN_NAME.equals(loginName)) {
            return null;
        }

        AdminPO po = new AdminPO();
        po.setTenantCode(tenantCode);
        po.setLoginName(DaoConsts.ADMIN_SYS_SUPER_LOGIN_NAME);
        po.setLoginPass(DaoConsts.ADMIN_SYS_SUPER_PASSWORD);
        po.setOrgName(DaoConsts.ORG_NAME_TOP);
        po.setRoleCode(DaoConsts.ROLE_SYS_SUPER);
        return po;
    }
}
