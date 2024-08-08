package com.langtuo.teamachine.dao.accessor.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.user.OrgMapper;
import com.langtuo.teamachine.dao.po.user.OrgPO;
import com.langtuo.teamachine.dao.query.user.OrgStrucQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OrgAccessor {
    @Resource
    private OrgMapper mapper;

    @Resource
    private RedisManager redisManager;

    public OrgPO selectOne(String tenantCode, String orgName) {
        // 首先访问缓存
        OrgPO cached = getCache(tenantCode, orgName);
        if (cached != null) {
            return cached;
        }

        OrgPO po = mapper.selectOne(tenantCode, orgName);

        // 设置缓存
        setCache(tenantCode, orgName, po);
        return po;
    }

    public List<OrgPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<OrgPO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<OrgPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<OrgPO> search(String tenantCode, String orgName, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OrgStrucQuery query = new OrgStrucQuery();
        query.setTenantCode(StringUtils.isBlank(tenantCode) ? null : tenantCode);
        query.setOrgName(StringUtils.isBlank(orgName) ? null : orgName);
        List<OrgPO> list = mapper.search(query);

        PageInfo<OrgPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(OrgPO po) {
        int inserted = mapper.insert(po);
        if (inserted == 1) {
            deleteCacheOne(po.getTenantCode(), po.getOrgName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(OrgPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCacheOne(po.getTenantCode(), po.getOrgName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String orgName) {
        int deleted = mapper.delete(tenantCode, orgName);
        if (deleted == 1) {
            deleteCacheOne(tenantCode, orgName);
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String orgName) {
        return "orgAcc-" + tenantCode + "-" + orgName;
    }

    private String getCacheListKey(String tenantCode) {
        return "orgAcc-" + tenantCode;
    }

    private OrgPO getCache(String tenantCode, String orgName) {
        String key = getCacheKey(tenantCode, orgName);
        Object cached = redisManager.getValue(key);
        OrgPO po = (OrgPO) cached;
        return po;
    }

    private List<OrgPO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<OrgPO> poList = (List<OrgPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<OrgPO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String orgName, OrgPO po) {
        String key = getCacheKey(tenantCode, orgName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String orgName) {
        redisManager.deleteKey(getCacheKey(tenantCode, orgName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
