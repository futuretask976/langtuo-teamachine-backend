package com.langtuo.teamachine.dao.accessor.rule;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.rule.CloseRuleMapper;
import com.langtuo.teamachine.dao.po.rule.CloseRulePO;
import com.langtuo.teamachine.dao.query.rule.CloseRuleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CloseRuleAccessor {
    @Resource
    private CloseRuleMapper mapper;

    @Resource
    private RedisManager redisManager;

    public CloseRulePO selectOneByCode(String tenantCode, String closeRuleCode) {
        CloseRulePO cached = setCache(tenantCode, closeRuleCode, null);
        if (cached != null) {
            return cached;
        }

        CloseRulePO po = mapper.selectOne(tenantCode, closeRuleCode, null);
        setCache(tenantCode, closeRuleCode, null, po);
        return po;
    }

    public CloseRulePO selectOneByName(String tenantCode, String closeRuleName) {
        CloseRulePO cached = setCache(tenantCode, null, closeRuleName);
        if (cached != null) {
            return cached;
        }

        CloseRulePO po = mapper.selectOne(tenantCode, null, closeRuleName);
        setCache(tenantCode, null, closeRuleName, po);
        return po;
    }

    public List<CloseRulePO> selectList(String tenantCode) {
        // 首先访问缓存
        List<CloseRulePO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<CloseRulePO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<CloseRulePO> search(String tenantCode, String closeRuleCode, String closeRuleName, 
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        CloseRuleQuery query = new CloseRuleQuery();
        query.setTenantCode(tenantCode);
        query.setCloseRuleCode(StringUtils.isBlank(closeRuleCode) ? null : closeRuleCode);
        query.setCloseRuleName(StringUtils.isBlank(closeRuleName) ? null : closeRuleName);
        List<CloseRulePO> list = mapper.search(query);

        PageInfo<CloseRulePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int update(CloseRulePO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCacheOne(po.getTenantCode(), po.getCloseRuleCode(), po.getCloseRuleName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int insert(CloseRulePO po) {
        int inserted = mapper.insert(po);
        if (inserted == 1) {
            deleteCacheOne(po.getTenantCode(), po.getCloseRuleCode(), po.getCloseRuleName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int delete(String tenantCode, String closeRuleCode) {
        CloseRulePO po = selectOneByCode(tenantCode, closeRuleCode);
        if (po == null) {
            return 0;
        }

        int deleted = mapper.delete(tenantCode, closeRuleCode);
        if (deleted == 1) {
            deleteCacheOne(tenantCode, po.getCloseRuleCode(), po.getCloseRuleName());
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String closeRuleCode, String closeRuleName) {
        return "close_rule_acc_" + tenantCode + "-" + closeRuleCode + "-" + closeRuleName;
    }

    private String getCacheListKey(String tenantCode) {
        return "close_rule_acc_" + tenantCode;
    }

    private CloseRulePO setCache(String tenantCode, String closeRuleCode, String closeRuleName) {
        String key = getCacheKey(tenantCode, closeRuleCode, closeRuleName);
        Object cached = redisManager.getValue(key);
        CloseRulePO po = (CloseRulePO) cached;
        return po;
    }

    private List<CloseRulePO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<CloseRulePO> poList = (List<CloseRulePO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<CloseRulePO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String closeRuleCode, String closeRuleName, CloseRulePO po) {
        String key = getCacheKey(tenantCode, closeRuleCode, closeRuleName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String closeRuleCode, String closeRuleName) {
        redisManager.deleteKey(getCacheKey(tenantCode, closeRuleCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, closeRuleName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
