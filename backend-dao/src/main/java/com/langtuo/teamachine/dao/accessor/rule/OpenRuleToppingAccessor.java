package com.langtuo.teamachine.dao.accessor.rule;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.rule.OpenRuleToppingMapper;
import com.langtuo.teamachine.dao.po.rule.OpenRuleToppingPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OpenRuleToppingAccessor {
    @Resource
    private OpenRuleToppingMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<OpenRuleToppingPO> selectList(String tenantCode, String openRuleCode) {
        List<OpenRuleToppingPO> cached = getCacheList(tenantCode, openRuleCode);
        if (cached != null) {
            return cached;
        }

        List<OpenRuleToppingPO> list = mapper.selectList(tenantCode, openRuleCode);

        setCacheList(tenantCode, openRuleCode, list);
        return list;
    }

    public int insert(OpenRuleToppingPO po) {
        int inserted = mapper.insert(po);
        if (inserted == 1) {
            deleteCacheList(po.getTenantCode(), po.getOpenRuleCode());
        }
        return inserted;
    }

    public int delete(String tenantCode, String openRuleCode) {
        int deleted = mapper.delete(tenantCode, openRuleCode);
        if (deleted == 1) {
            deleteCacheList(tenantCode, openRuleCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String openRuleCode) {
        return "open_rule_topping_acc_" + tenantCode + "-" + openRuleCode;
    }

    private List<OpenRuleToppingPO> getCacheList(String tenantCode, String openRuleCode) {
        String key = getCacheListKey(tenantCode, openRuleCode);
        Object cached = redisManager.getValue(key);
        List<OpenRuleToppingPO> poList = (List<OpenRuleToppingPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String openRuleCode, List<OpenRuleToppingPO> poList) {
        String key = getCacheListKey(tenantCode, openRuleCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String openRuleCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, openRuleCode));
    }
}
