package com.langtuo.teamachine.dao.accessor.rule;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.rule.CleanRuleDispatchMapper;
import com.langtuo.teamachine.dao.po.rule.CleanRuleDispatchPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CleanRuleDispatchAccessor {
    @Resource
    private CleanRuleDispatchMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<CleanRuleDispatchPO> selectListByCleanRuleCode(String tenantCode, String cleanRuleCode) {
        List<CleanRuleDispatchPO> cached = getCacheList(tenantCode, cleanRuleCode);
        if (cached != null) {
            return cached;
        }
        
        List<CleanRuleDispatchPO> list = mapper.selectList(tenantCode, cleanRuleCode);
        
        setCacheList(tenantCode, cleanRuleCode, list);
        return list;
    }

    public List<CleanRuleDispatchPO> selectListByShopGroupCode(String tenantCode, String shopGroupCode) {
        List<CleanRuleDispatchPO> cached = getCacheListByShopGroupCode(tenantCode, shopGroupCode);
        if (cached != null) {
            return cached;
        }

        List<CleanRuleDispatchPO> list = mapper.selectListByShopGroupCode(tenantCode, shopGroupCode);

        setCacheListByShopGroupCode(tenantCode, shopGroupCode, list);
        return list;
    }

    public int insert(CleanRuleDispatchPO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getCleanRuleCode(), po.getShopGroupCode());
        }
        return inserted;
    }

    public int deleteByCleanRuleCode(String tenantCode, String cleanRuleCode) {
        List<CleanRuleDispatchPO> existList = selectListByCleanRuleCode(tenantCode, cleanRuleCode);
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
        return "cleanRuleDispatchAcc-" + tenantCode + "-" + cleanRuleCode;
    }

    private String getCacheListKeyByShopGroupCode(String tenantCode, String shopGroupCode) {
        return "cleanRuleDispatchAcc-byShopGroupCode-" + tenantCode + "-" + shopGroupCode;
    }

    private List<CleanRuleDispatchPO> getCacheList(String tenantCode, String cleanRuleCode) {
        String key = getCacheListKey(tenantCode, cleanRuleCode);
        Object cached = redisManager.getValue(key);
        List<CleanRuleDispatchPO> poList = (List<CleanRuleDispatchPO>) cached;
        return poList;
    }

    private List<CleanRuleDispatchPO> getCacheListByShopGroupCode(String tenantCode, String shopGroupCode) {
        String key = getCacheListKeyByShopGroupCode(tenantCode, shopGroupCode);
        Object cached = redisManager.getValue(key);
        List<CleanRuleDispatchPO> poList = (List<CleanRuleDispatchPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String cleanRuleCode, List<CleanRuleDispatchPO> poList) {
        String key = getCacheListKey(tenantCode, cleanRuleCode);
        redisManager.setValue(key, poList);
    }

    private void setCacheListByShopGroupCode(String tenantCode, String shopGroupCode, List<CleanRuleDispatchPO> poList) {
        String key = getCacheListKeyByShopGroupCode(tenantCode, shopGroupCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String cleanRuleCode, String shopGroupCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, cleanRuleCode));
        redisManager.deleteKey(getCacheListKeyByShopGroupCode(tenantCode, shopGroupCode));
    }
}
