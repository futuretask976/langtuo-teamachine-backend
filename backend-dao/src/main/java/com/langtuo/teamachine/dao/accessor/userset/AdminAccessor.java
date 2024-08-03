package com.langtuo.teamachine.dao.accessor.userset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.userset.AdminMapper;
import com.langtuo.teamachine.dao.po.userset.AdminPO;
import com.langtuo.teamachine.dao.po.userset.AdminPO;
import com.langtuo.teamachine.dao.po.userset.RolePO;
import com.langtuo.teamachine.dao.query.userset.AdminQuery;
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

    public AdminPO selectOne(String tenantCode, String loginName) {
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

    public int insert(AdminPO adminPO) {
        return mapper.insert(adminPO);
    }

    public int update(AdminPO adminPO) {
        int updated = mapper.update(adminPO);
        if (updated == 1) {
            deleteCacheAll(adminPO.getTenantCode(), adminPO.getRoleCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String loginName) {
        int deleted = mapper.delete(tenantCode, loginName);
        if (deleted == 1) {
            deleteCacheAll(tenantCode, loginName);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String loginName) {
        return "role_acc_" + tenantCode + "-" + loginName;
    }

    private String getCacheListKey(String tenantCode) {
        return "role_acc_" + tenantCode;
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

    private void deleteCacheAll(String tenantCode, String loginName) {
        redisManager.deleteKey(getCacheKey(tenantCode, loginName));
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
