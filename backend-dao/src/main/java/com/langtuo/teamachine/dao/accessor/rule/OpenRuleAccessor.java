package com.langtuo.teamachine.dao.accessor.rule;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.rule.OpenRuleMapper;
import com.langtuo.teamachine.dao.po.rule.OpenRulePO;
import com.langtuo.teamachine.dao.query.rule.OpenRuleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OpenRuleAccessor {
    @Resource
    private OpenRuleMapper mapper;

    @Resource
    private RedisManager redisManager;

    public OpenRulePO selectOneByCode(String tenantCode, String openRuleCode) {
        OpenRulePO cached = setCache(tenantCode, openRuleCode, null);
        if (cached != null) {
            return cached;
        }

        OpenRulePO po = mapper.selectOne(tenantCode, openRuleCode, null);
        setCache(tenantCode, openRuleCode, null, po);
        return po;
    }

    public OpenRulePO selectOneByName(String tenantCode, String openRuleName) {
        OpenRulePO cached = setCache(tenantCode, null, openRuleName);
        if (cached != null) {
            return cached;
        }

        OpenRulePO po = mapper.selectOne(tenantCode, null, openRuleName);
        setCache(tenantCode, null, openRuleName, po);
        return po;
    }

    public List<OpenRulePO> selectList(String tenantCode) {
        // 首先访问缓存
        List<OpenRulePO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<OpenRulePO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<OpenRulePO> search(String tenantCode, String openRuleCode, String openRuleName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OpenRuleQuery query = new OpenRuleQuery();
        query.setTenantCode(tenantCode);
        query.setOpenRuleCode(StringUtils.isBlank(openRuleCode) ? null : openRuleCode);
        query.setOpenRuleName(StringUtils.isBlank(openRuleName) ? null : openRuleName);
        List<OpenRulePO> list = mapper.search(query);

        PageInfo<OpenRulePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int update(OpenRulePO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCacheOne(po.getTenantCode(), po.getOpenRuleCode(), po.getOpenRuleName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int insert(OpenRulePO po) {
        int inserted = mapper.insert(po);
        if (inserted == 1) {
            deleteCacheOne(po.getTenantCode(), po.getOpenRuleCode(), po.getOpenRuleName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int delete(String tenantCode, String openRuleCode) {
        OpenRulePO po = selectOneByCode(tenantCode, openRuleCode);
        if (po == null) {
            return 0;
        }

        int deleted = mapper.delete(tenantCode, openRuleCode);
        if (deleted == 1) {
            deleteCacheOne(tenantCode, po.getOpenRuleCode(), po.getOpenRuleName());
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String openRuleCode, String openRuleName) {
        return "openRuleAcc-" + tenantCode + "-" + openRuleCode + "-" + openRuleName;
    }

    private String getCacheListKey(String tenantCode) {
        return "openRuleAcc-" + tenantCode;
    }

    private OpenRulePO setCache(String tenantCode, String openRuleCode, String openRuleName) {
        String key = getCacheKey(tenantCode, openRuleCode, openRuleName);
        Object cached = redisManager.getValue(key);
        OpenRulePO po = (OpenRulePO) cached;
        return po;
    }

    private List<OpenRulePO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<OpenRulePO> poList = (List<OpenRulePO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<OpenRulePO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String openRuleCode, String openRuleName, OpenRulePO po) {
        String key = getCacheKey(tenantCode, openRuleCode, openRuleName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String openRuleCode, String openRuleName) {
        redisManager.deleteKey(getCacheKey(tenantCode, openRuleCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, openRuleName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
