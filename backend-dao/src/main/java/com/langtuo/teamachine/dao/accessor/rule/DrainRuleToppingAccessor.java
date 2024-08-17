package com.langtuo.teamachine.dao.accessor.rule;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DBOpeConts;
import com.langtuo.teamachine.dao.mapper.rule.DrainRuleToppingMapper;
import com.langtuo.teamachine.dao.po.rule.DrainRuleToppingPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DrainRuleToppingAccessor {
    @Resource
    private DrainRuleToppingMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<DrainRuleToppingPO> selectList(String tenantCode, String openRuleCode) {
        List<DrainRuleToppingPO> cached = getCacheList(tenantCode, openRuleCode);
        if (cached != null) {
            return cached;
        }

        List<DrainRuleToppingPO> list = mapper.selectList(tenantCode, openRuleCode);

        setCacheList(tenantCode, openRuleCode, list);
        return list;
    }

    public int insert(DrainRuleToppingPO po) {
        int inserted = mapper.insert(po);
        if (inserted == DBOpeConts.INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getDrainRuleCode());
        }
        return inserted;
    }

    public int delete(String tenantCode, String openRuleCode) {
        int deleted = mapper.delete(tenantCode, openRuleCode);
        if (deleted == DBOpeConts.DELETED_ONE_ROW) {
            deleteCacheList(tenantCode, openRuleCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String openRuleCode) {
        return "openRuleToppingAcc-" + tenantCode + "-" + openRuleCode;
    }

    private List<DrainRuleToppingPO> getCacheList(String tenantCode, String openRuleCode) {
        String key = getCacheListKey(tenantCode, openRuleCode);
        Object cached = redisManager.getValue(key);
        List<DrainRuleToppingPO> poList = (List<DrainRuleToppingPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String openRuleCode, List<DrainRuleToppingPO> poList) {
        String key = getCacheListKey(tenantCode, openRuleCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String openRuleCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, openRuleCode));
    }
}
