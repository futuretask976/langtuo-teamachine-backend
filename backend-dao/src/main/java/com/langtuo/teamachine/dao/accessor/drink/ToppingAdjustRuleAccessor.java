package com.langtuo.teamachine.dao.accessor.drink;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.drink.ToppingAdjustRuleMapper;
import com.langtuo.teamachine.dao.po.drink.ToppingAdjustRulePO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ToppingAdjustRuleAccessor {
    @Resource
    private ToppingAdjustRuleMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<ToppingAdjustRulePO> listByTeaUnitCode(String tenantCode, String teaCode, String teaUnitCode) {
        List<ToppingAdjustRulePO> cached = getCacheList(tenantCode, teaCode, teaUnitCode);
        if (!CollectionUtils.isEmpty(cached)) {
            return cached;
        }

        List<ToppingAdjustRulePO> list = mapper.selectList(tenantCode, teaCode, teaUnitCode);

        setCacheList(tenantCode, teaCode, teaUnitCode, list);
        return list;
    }

    public int insert(ToppingAdjustRulePO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getTeaCode(), po.getTeaUnitCode());
        }
        return inserted;
    }

    public int deleteByTeaCode(String tenantCode, String teaCode) {
        List<ToppingAdjustRulePO> poList = mapper.selectList(tenantCode, teaCode, null);
        List<String> teaUnitCodeList = poList.stream()
                .map(po -> po.getTeaUnitCode())
                .collect(Collectors.toList());

        int deleted = mapper.delete(tenantCode, teaCode);
        if (deleted > CommonConsts.DELETED_ZERO_ROW) {
            teaUnitCodeList.forEach(teaUnitCode -> {
                deleteCacheList(tenantCode, teaCode, teaUnitCode);
            });
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String teaCode, String teaUnitCode) {
        return "toppingAdjustRuleAcc-" + tenantCode + "-" + teaCode + "-" + teaUnitCode;
    }

    private List<ToppingAdjustRulePO> getCacheList(String tenantCode, String teaCode, String teaUnitCode) {
        String key = getCacheListKey(tenantCode, teaCode, teaUnitCode);
        Object cached = redisManager.getValue(key);
        List<ToppingAdjustRulePO> poList = (List<ToppingAdjustRulePO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String teaCode, String teaUnitCode, List<ToppingAdjustRulePO> poList) {
        String key = getCacheListKey(tenantCode, teaCode, teaUnitCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String teaCode, String teaUnitCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, teaCode, teaUnitCode));
    }
}
