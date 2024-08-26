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

    public List<DrainRuleToppingPO> selectListByDrainRuleCode(String tenantCode, String drainRuleCode) {
        List<DrainRuleToppingPO> cached = getCacheList(tenantCode, drainRuleCode);
        if (cached != null) {
            return cached;
        }

        List<DrainRuleToppingPO> list = mapper.selectList(tenantCode, drainRuleCode);

        setCacheList(tenantCode, drainRuleCode, list);
        return list;
    }

    public int insert(DrainRuleToppingPO po) {
        int inserted = mapper.insert(po);
        if (inserted == DBOpeConts.INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getDrainRuleCode());
        }
        return inserted;
    }

    public int deleteByDrainRuleCode(String tenantCode, String drainRuleCode) {
        int deleted = mapper.delete(tenantCode, drainRuleCode);
        if (deleted == DBOpeConts.DELETED_ONE_ROW) {
            deleteCacheList(tenantCode, drainRuleCode);
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
