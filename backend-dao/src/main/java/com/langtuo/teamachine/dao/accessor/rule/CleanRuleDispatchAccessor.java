package com.langtuo.teamachine.dao.accessor.rule;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.rule.CleanRuleDispatchMapper;
import com.langtuo.teamachine.dao.po.rule.CleanRuleDispatchPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CleanRuleDispatchAccessor {
    @Resource
    private CleanRuleDispatchMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<CleanRuleDispatchPO> listByCleanRuleCode(String tenantCode, String cleanRuleCode) {
        List<CleanRuleDispatchPO> cached = getCacheList(tenantCode, cleanRuleCode);
        if (cached != null) {
            return cached;
        }
        
        List<CleanRuleDispatchPO> list = mapper.selectList(tenantCode, cleanRuleCode);
        
        setCacheList(tenantCode, cleanRuleCode, list);
        return list;
    }

    public List<CleanRuleDispatchPO> listByShopGroupCode(String tenantCode, String shopGroupCode) {
        List<CleanRuleDispatchPO> list = mapper.selectListByShopGroupCode(tenantCode, shopGroupCode);
        return list;
    }

    public int insert(CleanRuleDispatchPO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.DB_INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getCleanRuleCode());
        }
        return inserted;
    }

    public int deleteByCleanRuleCode(String tenantCode, String cleanRuleCode) {
        int deleted = mapper.delete(tenantCode, cleanRuleCode);
        deleteCacheList(tenantCode, cleanRuleCode);
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String cleanRuleCode) {
        return "cleanRuleDispatchAcc-" + tenantCode + "-" + cleanRuleCode;
    }

    private List<CleanRuleDispatchPO> getCacheList(String tenantCode, String cleanRuleCode) {
        String key = getCacheListKey(tenantCode, cleanRuleCode);
        Object cached = redisManager.getValue(key);
        List<CleanRuleDispatchPO> poList = (List<CleanRuleDispatchPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String cleanRuleCode, List<CleanRuleDispatchPO> poList) {
        String key = getCacheListKey(tenantCode, cleanRuleCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String cleanRuleCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, cleanRuleCode));
    }
}
