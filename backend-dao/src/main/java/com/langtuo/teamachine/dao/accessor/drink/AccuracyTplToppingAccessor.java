package com.langtuo.teamachine.dao.accessor.drink;

import com.langtuo.teamachine.dao.cache.RedisManager4Accessor;
import com.langtuo.teamachine.dao.mapper.drink.AccuracyTplToppingMapper;
import com.langtuo.teamachine.dao.po.drink.AccuracyTplToppingPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AccuracyTplToppingAccessor {
    @Resource
    private AccuracyTplToppingMapper mapper;

    @Resource
    private RedisManager4Accessor redisManager4Accessor;

    public List<AccuracyTplToppingPO> listByTplCode(String tenantCode, String templateCode) {
        // 首先访问缓存
        List<AccuracyTplToppingPO> cachedList = getCacheList(tenantCode, templateCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<AccuracyTplToppingPO> list = mapper.selectList(tenantCode, templateCode);

        // 设置缓存
        setCacheList(tenantCode, templateCode, list);
        return list;
    }

    public int insert(AccuracyTplToppingPO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.DB_INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getTemplateCode());
        }
        return inserted;
    }

    public int deleteByTplCode(String tenantCode, String templateCode) {
        int deleted = mapper.delete(tenantCode, templateCode);
        if (deleted == CommonConsts.DB_DELETED_ONE_ROW) {
            deleteCacheList(tenantCode, templateCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String templateCode) {
        return "accuracyTplToppingAcc-" + tenantCode + "-" + templateCode;
    }

    private List<AccuracyTplToppingPO> getCacheList(String tenantCode, String templateCode) {
        String key = getCacheListKey(tenantCode, templateCode);
        Object cached = redisManager4Accessor.getValue(key);
        List<AccuracyTplToppingPO> poList = (List<AccuracyTplToppingPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String templateCode, List<AccuracyTplToppingPO> poList) {
        String key = getCacheListKey(tenantCode, templateCode);
        redisManager4Accessor.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String templateCode) {
        redisManager4Accessor.deleteKey(getCacheListKey(tenantCode, templateCode));
    }
}
