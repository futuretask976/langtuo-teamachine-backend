package com.langtuo.teamachine.dao.accessor.drinkset;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.drinkset.SpecItemMapper;
import com.langtuo.teamachine.dao.po.drinkset.SpecItemPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpecItemAccessor {
    @Resource
    private SpecItemMapper mapper;

    @Resource
    private RedisManager redisManager;

    public SpecItemPO selectOne(String tenantCode, String specCode, String specItemCode) {
        // 首先访问缓存
        SpecItemPO cached = getCachedDeploy(tenantCode, specCode, specItemCode);
        if (cached != null) {
            return cached;
        }

        SpecItemPO po = mapper.selectOne(tenantCode, specCode, specItemCode);

        // 设置缓存
        setCachedDeploy(tenantCode, specCode, specItemCode, po);
        return po;
    }

    public List<SpecItemPO> selectList(String tenantCode, String specCode) {
        // 首先访问缓存
        List<SpecItemPO> cachedList = getCachedDeployList(tenantCode, specCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<SpecItemPO> list = mapper.selectList(tenantCode, specCode);

        // 设置缓存
        setCachedDeployList(tenantCode, specCode, list);
        return list;
    }

    public int insert(SpecItemPO po) {
        return mapper.insert(po);
    }

    public int update(SpecItemPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCachedDeploy(po.getTenantCode(), po.getSpecCode(), po.getSpecItemCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String specCode) {
        List<String> specItemCodeList = mapper.selectList(tenantCode, specCode).stream()
                .map(po -> po.getSpecItemCode())
                .collect(Collectors.toList());

        int deleted = mapper.delete(tenantCode, specCode);
        if (deleted == 1) {
            specItemCodeList.forEach(specItemCode -> {
                deleteCachedDeploy(tenantCode, specCode, specItemCode);
            });
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String specCode) {
        return "spec_item_acc_" + tenantCode + "-" + specCode;
    }

    private String getCacheKey(String tenantCode, String specCode, String specItemCode) {
        return "spec_item_acc_" + tenantCode + "-" + specCode + "-" + specItemCode;
    }

    private SpecItemPO getCachedDeploy(String tenantCode, String specCode, String specItemCode) {
        String key = getCacheKey(tenantCode, specCode, specItemCode);
        Object cached = redisManager.getValue(key);
        SpecItemPO po = (SpecItemPO) cached;
        return po;
    }

    private List<SpecItemPO> getCachedDeployList(String tenantCode, String specCode) {
        String key = getCacheKey(tenantCode, specCode);
        Object cached = redisManager.getValue(key);
        List<SpecItemPO> poList = (List<SpecItemPO>) cached;
        return poList;
    }

    private void setCachedDeployList(String tenantCode, String specCode, List<SpecItemPO> poList) {
        String key = getCacheKey(tenantCode, specCode);
        redisManager.setValue(key, poList);
    }

    private void setCachedDeploy(String tenantCode, String specCode, String specItemCode, SpecItemPO po) {
        String key = getCacheKey(tenantCode, specCode, specItemCode);
        redisManager.setValue(key, po);
    }

    private void deleteCachedDeploy(String tenantCode, String specCode, String specItemCode) {
        redisManager.deleteKey(getCacheKey(tenantCode, specCode, specItemCode));
        redisManager.deleteKey(getCacheKey(tenantCode, specCode));
    }
}
