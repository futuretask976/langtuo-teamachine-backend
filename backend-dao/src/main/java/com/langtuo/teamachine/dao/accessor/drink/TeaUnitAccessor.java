package com.langtuo.teamachine.dao.accessor.drink;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.drink.TeaUnitMapper;
import com.langtuo.teamachine.dao.po.drink.TeaUnitPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TeaUnitAccessor {
    @Resource
    private TeaUnitMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<TeaUnitPO> selectList(String tenantCode, String teaCode) {
        // 首先访问缓存
        List<TeaUnitPO> cachedList = getCacheList(tenantCode, teaCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<TeaUnitPO> list = mapper.selectList(tenantCode, teaCode);

        setCacheList(tenantCode, teaCode, list);
        return list;
    }

    public int insert(TeaUnitPO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String teaCode) {
        int deleted = mapper.delete(tenantCode, teaCode);
        if (deleted > 0) {
            deleteCacheList(tenantCode, teaCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String teaCode) {
        return "teaUnitAcc-" + tenantCode + "-" + teaCode;
    }

    private List<TeaUnitPO> getCacheList(String tenantCode, String teaCode) {
        String key = getCacheListKey(tenantCode, teaCode);
        Object cached = redisManager.getValue(key);
        List<TeaUnitPO> poList = (List<TeaUnitPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String teaCode, List<TeaUnitPO> poList) {
        String key = getCacheListKey(tenantCode, teaCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String teaCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, teaCode));
    }
}
