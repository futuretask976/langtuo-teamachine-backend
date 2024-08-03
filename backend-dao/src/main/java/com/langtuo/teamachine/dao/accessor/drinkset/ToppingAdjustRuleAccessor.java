package com.langtuo.teamachine.dao.accessor.drinkset;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.drinkset.ToppingAdjustRuleMapper;
import com.langtuo.teamachine.dao.po.drinkset.ToppingAccuracyTplPO;
import com.langtuo.teamachine.dao.po.drinkset.ToppingAdjustRulePO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ToppingAdjustRuleAccessor {
    @Resource
    private ToppingAdjustRuleMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<ToppingAdjustRulePO> selectList(String tenantCode, String teaCode, String teaUnitCode) {
        List<ToppingAdjustRulePO> list = mapper.selectList(tenantCode, teaCode, teaUnitCode);
        return list;
    }

    public int insert(ToppingAdjustRulePO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String teaCode) {
        List<ToppingAdjustRulePO> poList = mapper.selectList(tenantCode, teaCode, null);
        List<String> teaUnitCodeList = poList.stream()
                .map(po -> po.getTeaUnitCode())
                .collect(Collectors.toList());

        int deleted = mapper.delete(tenantCode, teaCode);
        if (deleted > 0) {
            teaUnitCodeList.forEach(teaUnitCode -> {
                deleteCacheAll(tenantCode, teaCode, teaUnitCode);
            });
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String teaCode, String teaUnitCode) {
        return "topping_adjust_rule_acc_" + tenantCode + "-" + teaCode + "-" + teaUnitCode;
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

    private void deleteCacheAll(String tenantCode, String teaCode, String teaUnitCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, teaCode, teaUnitCode));
    }
}
