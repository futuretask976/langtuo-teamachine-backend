package com.langtuo.teamachine.dao.accessor.rule;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DBOpeConts;
import com.langtuo.teamachine.dao.mapper.rule.CleanRuleMapper;
import com.langtuo.teamachine.dao.po.rule.CleanRulePO;
import com.langtuo.teamachine.dao.query.rule.CleanRuleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CleanRuleAccessor {
    @Resource
    private CleanRuleMapper mapper;

    @Resource
    private RedisManager redisManager;

    public CleanRulePO selectOneByCode(String tenantCode, String cleanRuleCode) {
        CleanRulePO cached = setCache(tenantCode, cleanRuleCode, null);
        if (cached != null) {
            return cached;
        }

        CleanRulePO po = mapper.selectOne(tenantCode, cleanRuleCode, null);
        setCache(tenantCode, cleanRuleCode, null, po);
        return po;
    }

    public CleanRulePO selectOneByName(String tenantCode, String cleanRuleName) {
        CleanRulePO cached = setCache(tenantCode, null, cleanRuleName);
        if (cached != null) {
            return cached;
        }

        CleanRulePO po = mapper.selectOne(tenantCode, null, cleanRuleName);
        setCache(tenantCode, null, cleanRuleName, po);
        return po;
    }

    public List<CleanRulePO> selectList(String tenantCode) {
        // 首先访问缓存
        List<CleanRulePO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<CleanRulePO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<CleanRulePO> search(String tenantCode, String cleanRuleCode, String cleanRuleName, 
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        CleanRuleQuery query = new CleanRuleQuery();
        query.setTenantCode(tenantCode);
        query.setCleanRuleCode(StringUtils.isBlank(cleanRuleCode) ? null : cleanRuleCode);
        query.setCleanRuleName(StringUtils.isBlank(cleanRuleName) ? null : cleanRuleName);
        List<CleanRulePO> list = mapper.search(query);

        PageInfo<CleanRulePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int update(CleanRulePO po) {
        int updated = mapper.update(po);
        if (updated == DBOpeConts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getCleanRuleCode(), po.getCleanRuleName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int insert(CleanRulePO po) {
        int inserted = mapper.insert(po);
        if (inserted == DBOpeConts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getCleanRuleCode(), po.getCleanRuleName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int delete(String tenantCode, String cleanRuleCode) {
        CleanRulePO po = selectOneByCode(tenantCode, cleanRuleCode);
        if (po == null) {
            return DBOpeConts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, cleanRuleCode);
        if (deleted == DBOpeConts.DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, po.getCleanRuleCode(), po.getCleanRuleName());
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String cleanRuleCode, String cleanRuleName) {
        return "cleanRuleAcc-" + tenantCode + "-" + cleanRuleCode + "-" + cleanRuleName;
    }

    private String getCacheListKey(String tenantCode) {
        return "cleanRuleAcc-" + tenantCode;
    }

    private CleanRulePO setCache(String tenantCode, String cleanRuleCode, String cleanRuleName) {
        String key = getCacheKey(tenantCode, cleanRuleCode, cleanRuleName);
        Object cached = redisManager.getValue(key);
        CleanRulePO po = (CleanRulePO) cached;
        return po;
    }

    private List<CleanRulePO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<CleanRulePO> poList = (List<CleanRulePO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<CleanRulePO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String cleanRuleCode, String cleanRuleName, CleanRulePO po) {
        String key = getCacheKey(tenantCode, cleanRuleCode, cleanRuleName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String cleanRuleCode, String cleanRuleName) {
        redisManager.deleteKey(getCacheKey(tenantCode, cleanRuleCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, cleanRuleName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
