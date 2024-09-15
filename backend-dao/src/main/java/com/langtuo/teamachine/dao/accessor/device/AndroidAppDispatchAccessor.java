package com.langtuo.teamachine.dao.accessor.device;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.device.AndroidAppDispatchMapper;
import com.langtuo.teamachine.dao.po.device.AndroidAppDispatchPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AndroidAppDispatchAccessor {
    @Resource
    private AndroidAppDispatchMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<AndroidAppDispatchPO> selectListByAndroidAppVersion(String tenantCode, String version) {
        // 首先访问缓存
        List<AndroidAppDispatchPO> cachedList = getCacheList(tenantCode, version);
        if (cachedList != null) {
            return cachedList;
        }

        List<AndroidAppDispatchPO> list = mapper.selectList(tenantCode, version);

        // 设置缓存
        setCacheList(tenantCode, version, list);
        return list;
    }

    public List<AndroidAppDispatchPO> selectListByShopGroupCode(String tenantCode, String shopGroupCode) {
        List<AndroidAppDispatchPO> cached = getCacheListByShopGroupCode(tenantCode, shopGroupCode);
        if (cached != null) {
            return cached;
        }

        List<AndroidAppDispatchPO> list = mapper.selectListByShopGroupCode(tenantCode, shopGroupCode);

        setCacheListByShopGroupCode(tenantCode, shopGroupCode, list);
        return list;
    }

    public int insert(AndroidAppDispatchPO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.INSERTED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getVersion(), po.getShopGroupCode());
        }
        return inserted;
    }

    public int update(AndroidAppDispatchPO po) {
        int updated = mapper.update(po);
        if (updated == CommonConsts.UPDATED_ONE_ROW) {
            deleteCacheList(po.getTenantCode(), po.getVersion(), po.getShopGroupCode());
        }
        return updated;
    }

    public int deleteByAndroidAppVersion(String tenantCode, String version) {
        List<AndroidAppDispatchPO> existList = selectListByAndroidAppVersion(tenantCode, version);
        if (CollectionUtils.isEmpty(existList)) {
            return CommonConsts.DELETED_ZERO_ROW;
        }
        String shopGroupCode = existList.get(0).getShopGroupCode();

        int deleted = mapper.delete(tenantCode, version);
        if (deleted == CommonConsts.DELETED_ONE_ROW) {
            deleteCacheList(tenantCode, version, shopGroupCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String version) {
        return "menuDispatchAcc-" + tenantCode + "-" + version;
    }

    private String getCacheListKeyByShopGroupCode(String tenantCode, String shopGroupCode) {
        return "menuDispatchAcc-byShopGroupCode-" + tenantCode + "-" + shopGroupCode;
    }

    private List<AndroidAppDispatchPO> getCacheList(String tenantCode, String version) {
        String key = getCacheListKey(tenantCode, version);
        Object cached = redisManager.getValue(key);
        List<AndroidAppDispatchPO> poList = (List<AndroidAppDispatchPO>) cached;
        return poList;
    }

    private List<AndroidAppDispatchPO> getCacheListByShopGroupCode(String tenantCode, String shopGroupCode) {
        String key = getCacheListKey(tenantCode, shopGroupCode);
        Object cached = redisManager.getValue(key);
        List<AndroidAppDispatchPO> poList = (List<AndroidAppDispatchPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String version, List<AndroidAppDispatchPO> poList) {
        String key = getCacheListKey(tenantCode, version);
        redisManager.setValue(key, poList);
    }

    private void setCacheListByShopGroupCode(String tenantCode, String shopGroupCode, List<AndroidAppDispatchPO> poList) {
        String key = getCacheListKeyByShopGroupCode(tenantCode, shopGroupCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheList(String tenantCode, String version, String shopGroupCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, version));
        redisManager.deleteKey(getCacheListKeyByShopGroupCode(tenantCode, shopGroupCode));
    }
}
