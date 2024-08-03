package com.langtuo.teamachine.dao.accessor.userset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.userset.OrgMapper;
import com.langtuo.teamachine.dao.po.userset.OrgPO;
import com.langtuo.teamachine.dao.query.userset.OrgStrucQuery;
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
        OrgPO cached = getCachedOrg(tenantCode, orgName);
        if (cached != null) {
            return cached;
        }

        OrgPO po = mapper.selectOne(tenantCode, orgName);

        // 设置缓存
        setCachedOrg(tenantCode, orgName, po);
        return po;
    }

    public List<OrgPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<OrgPO> cachedList = getCachedOrgList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<OrgPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCachedOrgList(tenantCode, list);
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

    public int insert(OrgPO orgPO) {
        return mapper.insert(orgPO);
    }

    public int update(OrgPO orgPO) {
        int updated = mapper.update(orgPO);
        if (updated == 1) {
            deleteCachedOrg(orgPO.getTenantCode(), orgPO.getOrgName());
        }
        return updated;
    }

    public int delete(String tenantCode, String orgName) {
        int deleted = mapper.delete(tenantCode, orgName);
        if (deleted == 1) {
            deleteCachedOrg(tenantCode, orgName);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String orgName) {
        return "org_acc_" + tenantCode + "-" + orgName;
    }

    private String getCacheKey(String tenantCode) {
        return "org_acc_" + tenantCode;
    }

    private OrgPO getCachedOrg(String tenantCode, String orgName) {
        String key = getCacheKey(tenantCode, orgName);
        Object cached = redisManager.getValue(key);
        OrgPO po = (OrgPO) cached;
        return po;
    }

    private List<OrgPO> getCachedOrgList(String tenantCode) {
        String key = getCacheKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<OrgPO> poList = (List<OrgPO>) cached;
        return poList;
    }

    private void setCachedOrgList(String tenantCode, List<OrgPO> poList) {
        String key = getCacheKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCachedOrg(String tenantCode, String orgName, OrgPO po) {
        String key = getCacheKey(tenantCode, orgName);
        redisManager.setValue(key, po);
    }

    private void deleteCachedOrg(String tenantCode, String orgName) {
        redisManager.deleteKey(getCacheKey(tenantCode, orgName));
        redisManager.deleteKey(getCacheKey(tenantCode));
    }
}
