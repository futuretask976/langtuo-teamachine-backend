package com.langtuo.teamachine.dao.accessor.rule;

import com.langtuo.teamachine.dao.cache.RedisManager4Accessor;
import com.langtuo.teamachine.dao.mapper.rule.DrainRuleDispatchMapper;
import com.langtuo.teamachine.dao.po.rule.DrainRuleDispatchPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DrainRuleDispatchAccessor {
    @Resource
    private DrainRuleDispatchMapper mapper;

    @Resource
    private RedisManager4Accessor redisManager4Accessor;

    public List<DrainRuleDispatchPO> listByDrainRuleCode(String tenantCode, String drainRuleCode) {
        List<DrainRuleDispatchPO> cached = getCacheList(tenantCode, drainRuleCode);
        if (cached != null) {
            return cached;
        }
        
        List<DrainRuleDispatchPO> list = mapper.selectList(tenantCode, drainRuleCode);
        
        setCacheList(tenantCode, drainRuleCode, list);
        return list;
    }

    public List<DrainRuleDispatchPO> listByShopGroupCode(String tenantCode, String shopGroupCode) {
        List<DrainRuleDispatchPO> list = mapper.selectListByShopGroupCode(tenantCode, shopGroupCode);
        return list;
    }

    public int insert(DrainRuleDispatchPO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.DB_INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getDrainRuleCode());
        }
        return inserted;
    }

    public int deleteByDrainRuleCode(String tenantCode, String drainRuleCode) {
        int deleted = mapper.delete(tenantCode, drainRuleCode);
        deleteCacheList(tenantCode, drainRuleCode);
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String drainRuleCode) {
        return "drainRuleDispatchAcc-" + tenantCode + "-" + drainRuleCode;
    }

    private List<DrainRuleDispatchPO> getCacheList(String tenantCode, String drainRuleCode) {
        String key = getCacheListKey(tenantCode, drainRuleCode);
        Object cached = redisManager4Accessor.getValue(key);
        List<DrainRuleDispatchPO> poList = (List<DrainRuleDispatchPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String drainRuleCode, List<DrainRuleDispatchPO> poList) {
        String key = getCacheListKey(tenantCode, drainRuleCode);
        redisManager4Accessor.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String drainRuleCode) {
        redisManager4Accessor.deleteKey(getCacheListKey(tenantCode, drainRuleCode));
    }
}
