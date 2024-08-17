package com.langtuo.teamachine.dao.accessor.rule;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DBOpeConts;
import com.langtuo.teamachine.dao.mapper.rule.CleanRuleExceptMapper;
import com.langtuo.teamachine.dao.po.rule.CleanRuleExceptPO;
import com.langtuo.teamachine.dao.po.rule.CleanRuleExceptPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CleanRuleExceptAccessor {
    @Resource
    private CleanRuleExceptMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<CleanRuleExceptPO> selectList(String tenantCode, String cleanRuleCode) {
        List<CleanRuleExceptPO> cached = getCacheList(tenantCode, cleanRuleCode);
        if (cached != null) {
            return cached;
        }

        List<CleanRuleExceptPO> list = mapper.selectList(tenantCode, cleanRuleCode);

        setCacheList(tenantCode, cleanRuleCode, list);
        return list;
    }

    public int insert(CleanRuleExceptPO po) {
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
        return "cleanRuleExceptAcc-" + tenantCode + "-" + cleanRuleCode;
    }

    private List<CleanRuleExceptPO> getCacheList(String tenantCode, String cleanRuleCode) {
        String key = getCacheListKey(tenantCode, cleanRuleCode);
        Object cached = redisManager.getValue(key);
        List<CleanRuleExceptPO> poList = (List<CleanRuleExceptPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String cleanRuleCode, List<CleanRuleExceptPO> poList) {
        String key = getCacheListKey(tenantCode, cleanRuleCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String cleanRuleCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, cleanRuleCode));
    }
}
