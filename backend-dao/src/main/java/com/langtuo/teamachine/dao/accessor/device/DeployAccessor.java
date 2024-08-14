package com.langtuo.teamachine.dao.accessor.device;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.device.DeployMapper;
import com.langtuo.teamachine.dao.mapper.record.MachineCodeSeqMapper;
import com.langtuo.teamachine.dao.po.device.DeployPO;
import com.langtuo.teamachine.dao.query.device.MachineDeployQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DeployAccessor {
    @Resource
    private DeployMapper mapper;

    @Resource
    private MachineCodeSeqMapper machineCodeSeqMapper;

    @Resource
    private RedisManager redisManager;

    /**
     *
     */
    private static final long SEQ_SCOPE = 100;

    /**
     *
     */
    private long machineCodeSeqStartVal;

    /**
     *
     */
    private long machineCodeSeqCurVal;

    public DeployPO selectOne(String deployCode) {
        // 首先访问缓存
        DeployPO cached = getCache(deployCode);
        if (cached != null) {
            return cached;
        }
        
        DeployPO po = mapper.selectOne(deployCode);

        // 设置缓存
        setCache(deployCode, po);
        return po;
    }

    public List<DeployPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<DeployPO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<DeployPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<DeployPO> search(String tenantCode, String deployCode, String machineCode, String shopCode,
            Integer state, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        MachineDeployQuery machineDeployQuery = new MachineDeployQuery();
        machineDeployQuery.setTenantCode(tenantCode);
        machineDeployQuery.setDeployCode(StringUtils.isBlank(deployCode) ? null : deployCode);
        machineDeployQuery.setShopCode(StringUtils.isBlank(shopCode) ? null : shopCode);
        machineDeployQuery.setState(state);
        List<DeployPO> list = mapper.search(machineDeployQuery);

        PageInfo<DeployPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(DeployPO po) {
        int inserted = mapper.insert(po);
        if (inserted == 1) {
            deleteCacheOne(po.getDeployCode());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(DeployPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCacheOne(po.getDeployCode());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String deployCode) {
        int deleted = mapper.delete(tenantCode, deployCode);
        if (deleted == 1) {
            deleteCacheOne(deployCode);
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    public long getMachineCodeNextSeqVal() {
        if (machineCodeSeqCurVal < machineCodeSeqStartVal + SEQ_SCOPE) {
            return machineCodeSeqCurVal++;
        }

        long seq = machineCodeSeqMapper.getNextSeqValue();
        machineCodeSeqCurVal = seq;
        machineCodeSeqStartVal = seq;
        return machineCodeSeqCurVal++;
    }

    private String getCacheKey(String deployCode) {
        return "deployAcc-" + deployCode;
    }

    private String getCacheListKey(String tenantCode) {
        return "deployAcc-" + tenantCode;
    }

    private DeployPO getCache(String deployCode) {
        String key = getCacheKey(deployCode);
        Object cached = redisManager.getValue(key);
        DeployPO po = (DeployPO) cached;
        return po;
    }

    private List<DeployPO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<DeployPO> poList = (List<DeployPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<DeployPO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String deployCode, DeployPO po) {
        String key = getCacheKey(deployCode);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String deployCode) {
        redisManager.deleteKey(getCacheKey(deployCode));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
