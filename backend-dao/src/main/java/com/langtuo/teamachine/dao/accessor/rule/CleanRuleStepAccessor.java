package com.langtuo.teamachine.dao.accessor.rule;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DBOpeConts;
import com.langtuo.teamachine.dao.mapper.rule.CleanRuleStepMapper;
import com.langtuo.teamachine.dao.po.rule.CleanRuleDispatchPO;
import com.langtuo.teamachine.dao.po.rule.CleanRuleStepPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CleanRuleStepAccessor {
    @Resource
    private CleanRuleStepMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<CleanRuleStepPO> selectList(String tenantCode, String cleanRuleCode) {
        List<CleanRuleStepPO> cached = getCacheList(tenantCode, cleanRuleCode);
        if (cached != null) {
            return cached;
        }

        List<CleanRuleStepPO> list = mapper.selectList(tenantCode, cleanRuleCode);

        setCacheList(tenantCode, cleanRuleCode, list);
        return list;
    }

    public int insert(CleanRuleStepPO po) {
        int inserted = mapper.insert(po);
        if (inserted == DBOpeConts.INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getCleanRuleCode());
        }
        return inserted;
    }

    public int delete(String tenantCode, String cleanRuleCode) {
        int deleted = mapper.delete(tenantCode, cleanRuleCode);
        if (deleted == DBOpeConts.DELETED_ONE_ROW) {
            deleteCacheList(tenantCode, cleanRuleCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String cleanRuleCode) {
        return "cleanRuleStepAcc-" + tenantCode + "-" + cleanRuleCode;
    }

    private List<CleanRuleStepPO> getCacheList(String tenantCode, String cleanRuleCode) {
        String key = getCacheListKey(tenantCode, cleanRuleCode);
        Object cached = redisManager.getValue(key);
        List<CleanRuleStepPO> poList = (List<CleanRuleStepPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String cleanRuleCode, List<CleanRuleStepPO> poList) {
        String key = getCacheListKey(tenantCode, cleanRuleCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String cleanRuleCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, cleanRuleCode));
    }
}
