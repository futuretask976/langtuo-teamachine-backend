package com.langtuo.teamachine.dao.accessor.rule;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DBOpeConts;
import com.langtuo.teamachine.dao.mapper.rule.WarningRuleDispatchMapper;
import com.langtuo.teamachine.dao.po.rule.WarningRuleDispatchPO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Component
public class WarningRuleDispatchAccessor {
    @Resource
    private WarningRuleDispatchMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<WarningRuleDispatchPO> selectList(String tenantCode, String cleanRuleCode) {
        List<WarningRuleDispatchPO> cached = getCacheList(tenantCode, cleanRuleCode);
        if (cached != null) {
            return cached;
        }
        
        List<WarningRuleDispatchPO> list = mapper.selectList(tenantCode, cleanRuleCode);
        
        setCacheList(tenantCode, cleanRuleCode, list);
        return list;
    }

    public List<WarningRuleDispatchPO> selectListByShopGroupCode(String tenantCode, String shopGroupCode) {
        List<WarningRuleDispatchPO> cached = getCacheListByShopGroupCode(tenantCode, shopGroupCode);
        if (cached != null) {
            return cached;
        }

        List<WarningRuleDispatchPO> list = mapper.selectListByShopGroupCode(tenantCode, shopGroupCode);

        setCacheListByShopGroupCode(tenantCode, shopGroupCode, list);
        return list;
    }

    public int insert(WarningRuleDispatchPO po) {
        int inserted = mapper.insert(po);
        if (inserted == DBOpeConts.INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getWarningRuleCode(), po.getShopGroupCode());
        }
        return inserted;
    }

    public int delete(String tenantCode, String cleanRuleCode) {
        List<WarningRuleDispatchPO> existList = selectList(tenantCode, cleanRuleCode);
        if (CollectionUtils.isEmpty(existList)) {
            return DBOpeConts.DELETED_ZERO_ROW;
        }
        String shopGroupCode = existList.get(0).getShopGroupCode();

        int deleted = mapper.delete(tenantCode, cleanRuleCode);
        if (deleted == DBOpeConts.DELETED_ONE_ROW) {
            deleteCacheList(tenantCode, cleanRuleCode, shopGroupCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String cleanRuleCode) {
        return "warningRuleDispatchAcc-" + tenantCode + "-" + cleanRuleCode;
    }

    private String getCacheListKeyByShopGroupCode(String tenantCode, String shopGroupCode) {
        return "warningRuleDispatchAcc-byShopGroupCode-" + tenantCode + "-" + shopGroupCode;
    }

    private List<WarningRuleDispatchPO> getCacheList(String tenantCode, String cleanRuleCode) {
        String key = getCacheListKey(tenantCode, cleanRuleCode);
        Object cached = redisManager.getValue(key);
        List<WarningRuleDispatchPO> poList = (List<WarningRuleDispatchPO>) cached;
        return poList;
    }

    private List<WarningRuleDispatchPO> getCacheListByShopGroupCode(String tenantCode, String shopGroupCode) {
        String key = getCacheListKeyByShopGroupCode(tenantCode, shopGroupCode);
        Object cached = redisManager.getValue(key);
        List<WarningRuleDispatchPO> poList = (List<WarningRuleDispatchPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String cleanRuleCode, List<WarningRuleDispatchPO> poList) {
        String key = getCacheListKey(tenantCode, cleanRuleCode);
        redisManager.setValue(key, poList);
    }

    private void setCacheListByShopGroupCode(String tenantCode, String shopGroupCode, List<WarningRuleDispatchPO> poList) {
        String key = getCacheListKeyByShopGroupCode(tenantCode, shopGroupCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String cleanRuleCode, String shopGroupCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, cleanRuleCode));
        redisManager.deleteKey(getCacheListKeyByShopGroupCode(tenantCode, shopGroupCode));
    }
}
