package com.langtuo.teamachine.dao.accessor.drink;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DBOpeConts;
import com.langtuo.teamachine.dao.mapper.drink.ToppingBaseRuleMapper;
import com.langtuo.teamachine.dao.po.drink.ToppingBaseRulePO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ToppingBaseRuleAccessor {
    @Resource
    private ToppingBaseRuleMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<ToppingBaseRulePO> selectList(String tenantCode, String teaCode) {
        List<ToppingBaseRulePO> cached = getCacheList(tenantCode, teaCode);
        if (!CollectionUtils.isEmpty(cached)) {
            return cached;
        }

        List<ToppingBaseRulePO> list = mapper.selectList(tenantCode, teaCode);

        setCacheList(tenantCode, teaCode, list);
        return list;
    }

    public int insert(ToppingBaseRulePO po) {
        int inserted = mapper.insert(po);
        if (inserted == DBOpeConts.INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getTeaCode());
        }
        return inserted;
    }

    public int delete(String tenantCode, String teaCode) {
        int deleted = mapper.delete(tenantCode, teaCode);
        if (deleted > DBOpeConts.DELETED_ZERO_ROW) {
            deleteCacheList(tenantCode, teaCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String teaCode) {
        return "toppingBaseRuleAcc-" + tenantCode + "-" + teaCode;
    }

    private List<ToppingBaseRulePO> getCacheList(String tenantCode, String teaCode) {
        String key = getCacheListKey(tenantCode, teaCode);
        Object cached = redisManager.getValue(key);
        List<ToppingBaseRulePO> poList = (List<ToppingBaseRulePO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String teaCode, List<ToppingBaseRulePO> poList) {
        String key = getCacheListKey(tenantCode, teaCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String teaCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, teaCode));
    }
}
