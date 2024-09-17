package com.langtuo.teamachine.dao.accessor.rule;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.rule.DrainRuleDispatchMapper;
import com.langtuo.teamachine.dao.po.rule.DrainRuleDispatchPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DrainRuleDispatchAccessor {
    @Resource
    private DrainRuleDispatchMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<DrainRuleDispatchPO> listByDrainRuleCode(String tenantCode, String cleanRuleCode) {
        List<DrainRuleDispatchPO> cached = getCacheList(tenantCode, cleanRuleCode);
        if (cached != null) {
            return cached;
        }
        
        List<DrainRuleDispatchPO> list = mapper.selectList(tenantCode, cleanRuleCode);
        
        setCacheList(tenantCode, cleanRuleCode, list);
        return list;
    }

    public List<DrainRuleDispatchPO> listByShopGroupCode(String tenantCode, String shopGroupCode) {
        List<DrainRuleDispatchPO> cached = getCacheListByShopGroupCode(tenantCode, shopGroupCode);
        if (cached != null) {
            return cached;
        }

        List<DrainRuleDispatchPO> list = mapper.selectListByShopGroupCode(tenantCode, shopGroupCode);

        setCacheListByShopGroupCode(tenantCode, shopGroupCode, list);
        return list;
    }

    public int insert(DrainRuleDispatchPO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getDrainRuleCode(), po.getShopGroupCode());
        }
        return inserted;
    }

    public int deleteByCleanRuleCode(String tenantCode, String cleanRuleCode) {
        List<DrainRuleDispatchPO> existList = listByDrainRuleCode(tenantCode, cleanRuleCode);
        if (CollectionUtils.isEmpty(existList)) {
            return CommonConsts.DELETED_ZERO_ROW;
        }
        String shopGroupCode = existList.get(0).getShopGroupCode();

        int deleted = mapper.delete(tenantCode, cleanRuleCode);
        if (deleted == CommonConsts.DELETED_ONE_ROW) {
            deleteCacheList(tenantCode, cleanRuleCode, shopGroupCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String cleanRuleCode) {
        return "drainRuleDispatchAcc-" + tenantCode + "-" + cleanRuleCode;
    }

    private String getCacheListKeyByShopGroupCode(String tenantCode, String shopGroupCode) {
        return "drainRuleDispatchAcc-byShopGroupCode-" + tenantCode + "-" + shopGroupCode;
    }

    private List<DrainRuleDispatchPO> getCacheList(String tenantCode, String cleanRuleCode) {
        String key = getCacheListKey(tenantCode, cleanRuleCode);
        Object cached = redisManager.getValue(key);
        List<DrainRuleDispatchPO> poList = (List<DrainRuleDispatchPO>) cached;
        return poList;
    }

    private List<DrainRuleDispatchPO> getCacheListByShopGroupCode(String tenantCode, String shopGroupCode) {
        String key = getCacheListKeyByShopGroupCode(tenantCode, shopGroupCode);
        Object cached = redisManager.getValue(key);
        List<DrainRuleDispatchPO> poList = (List<DrainRuleDispatchPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String cleanRuleCode, List<DrainRuleDispatchPO> poList) {
        String key = getCacheListKey(tenantCode, cleanRuleCode);
        redisManager.setValue(key, poList);
    }

    private void setCacheListByShopGroupCode(String tenantCode, String shopGroupCode, List<DrainRuleDispatchPO> poList) {
        String key = getCacheListKeyByShopGroupCode(tenantCode, shopGroupCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String cleanRuleCode, String shopGroupCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, cleanRuleCode));
        redisManager.deleteKey(getCacheListKeyByShopGroupCode(tenantCode, shopGroupCode));
    }
}
