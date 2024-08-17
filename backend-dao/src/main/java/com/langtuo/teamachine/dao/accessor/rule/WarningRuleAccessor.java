package com.langtuo.teamachine.dao.accessor.rule;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DBOpeConts;
import com.langtuo.teamachine.dao.mapper.rule.WarningRuleMapper;
import com.langtuo.teamachine.dao.po.rule.WarningRulePO;
import com.langtuo.teamachine.dao.query.rule.WarningRuleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class WarningRuleAccessor {
    @Resource
    private WarningRuleMapper mapper;

    @Resource
    private RedisManager redisManager;

    public WarningRulePO selectOneByCode(String tenantCode, String warningRuleCode) {
        WarningRulePO cached = setCache(tenantCode, warningRuleCode, null);
        if (cached != null) {
            return cached;
        }

        WarningRulePO po = mapper.selectOne(tenantCode, warningRuleCode, null);
        setCache(tenantCode, warningRuleCode, null, po);
        return po;
    }

    public WarningRulePO selectOneByName(String tenantCode, String warningRuleName) {
        WarningRulePO cached = setCache(tenantCode, null, warningRuleName);
        if (cached != null) {
            return cached;
        }

        WarningRulePO po = mapper.selectOne(tenantCode, null, warningRuleName);
        setCache(tenantCode, null, warningRuleName, po);
        return po;
    }

    public List<WarningRulePO> selectList(String tenantCode) {
        // 首先访问缓存
        List<WarningRulePO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<WarningRulePO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<WarningRulePO> search(String tenantCode, String warningRuleCode, String warningRuleName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        WarningRuleQuery query = new WarningRuleQuery();
        query.setTenantCode(tenantCode);
        query.setWarningRuleCode(StringUtils.isBlank(warningRuleCode) ? null : warningRuleCode);
        query.setWarningRuleName(StringUtils.isBlank(warningRuleName) ? null : warningRuleName);
        List<WarningRulePO> list = mapper.search(query);

        PageInfo<WarningRulePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int update(WarningRulePO po) {
        int updated = mapper.update(po);
        if (updated == DBOpeConts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getWarningRuleCode(), po.getWarningRuleName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int insert(WarningRulePO po) {
        int inserted = mapper.insert(po);
        if (inserted == DBOpeConts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getWarningRuleCode(), po.getWarningRuleName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int delete(String tenantCode, String warningRuleCode) {
        WarningRulePO po = selectOneByCode(tenantCode, warningRuleCode);
        if (po == null) {
            return DBOpeConts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, warningRuleCode);
        if (deleted == DBOpeConts.DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, po.getWarningRuleCode(), po.getWarningRuleName());
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String warningRuleCode, String warningRuleName) {
        return "warningRuleAcc-" + tenantCode + "-" + warningRuleCode + "-" + warningRuleName;
    }

    private String getCacheListKey(String tenantCode) {
        return "warningRuleAcc-" + tenantCode;
    }

    private WarningRulePO setCache(String tenantCode, String warningRuleCode, String warningRuleName) {
        String key = getCacheKey(tenantCode, warningRuleCode, warningRuleName);
        Object cached = redisManager.getValue(key);
        WarningRulePO po = (WarningRulePO) cached;
        return po;
    }

    private List<WarningRulePO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<WarningRulePO> poList = (List<WarningRulePO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<WarningRulePO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String warningRuleCode, String warningRuleName, WarningRulePO po) {
        String key = getCacheKey(tenantCode, warningRuleCode, warningRuleName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String warningRuleCode, String warningRuleName) {
        redisManager.deleteKey(getCacheKey(tenantCode, warningRuleCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, warningRuleName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
