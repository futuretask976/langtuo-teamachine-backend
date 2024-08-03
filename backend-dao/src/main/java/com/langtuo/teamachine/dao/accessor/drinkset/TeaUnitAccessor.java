package com.langtuo.teamachine.dao.accessor.drinkset;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.drinkset.TeaUnitMapper;
import com.langtuo.teamachine.dao.po.drinkset.TeaTypePO;
import com.langtuo.teamachine.dao.po.drinkset.TeaUnitPO;
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
        List<TeaUnitPO> cachedList = getCachedTeaList(tenantCode, teaCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<TeaUnitPO> list = mapper.selectList(tenantCode, teaCode);

        setCachedTeaList(tenantCode, teaCode, list);
        return list;
    }

    public int insert(TeaUnitPO po) {
        return mapper.insert(po);
    }

    public int update(TeaUnitPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCachedTea(po.getTenantCode(), po.getTeaCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String teaCode) {
        int deleted = mapper.delete(tenantCode, teaCode);
        if (deleted == 1) {
            deleteCachedTea(tenantCode, teaCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String teaCode) {
        return "tea_unit_acc_" + tenantCode + "-" + teaCode;
    }

    private List<TeaUnitPO> getCachedTeaList(String tenantCode, String teaCode) {
        String key = getCacheKey(tenantCode, teaCode);
        Object cached = redisManager.getValue(key);
        List<TeaUnitPO> poList = (List<TeaUnitPO>) cached;
        return poList;
    }

    private void setCachedTeaList(String tenantCode, String teaCode, List<TeaUnitPO> poList) {
        String key = getCacheKey(tenantCode, teaCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCachedTea(String tenantCode, String teaCode) {
        redisManager.deleteKey(getCacheKey(tenantCode, teaCode));
    }
}
