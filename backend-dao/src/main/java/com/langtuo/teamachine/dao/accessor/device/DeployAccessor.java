package com.langtuo.teamachine.dao.accessor.device;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DBOpeConts;
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
     * 取值步长
     */
    private static final long SEQ_SCOPE = 100;

    /**
     * 机器编码步长起始值
     */
    private long machineCodeSeqStartVal;

    /**
     * 机器编码步长当前值
     */
    private long machineCodeSeqCurVal;

    public DeployPO selectOneByDeployCode(String tenantCode, String deployCode) {
        // 首先访问缓存
        DeployPO cached = getCache(tenantCode, deployCode, null);
        if (cached != null) {
            return cached;
        }
        
        DeployPO po = mapper.selectOne(tenantCode, deployCode, null);

        // 设置缓存
        setCache(tenantCode, deployCode, null, po);
        return po;
    }

    public DeployPO selectOneByMachineCode(String tenantCode, String machineCode) {
        // 首先访问缓存
        DeployPO cached = getCache(tenantCode, null, machineCode);
        if (cached != null) {
            return cached;
        }

        DeployPO po = mapper.selectOne(tenantCode, null, machineCode);

        // 设置缓存
        setCache(tenantCode, null, machineCode, po);
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
        if (inserted == DBOpeConts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getDeployCode(), po.getMachineCode());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(DeployPO po) {
        int updated = mapper.update(po);
        if (updated == DBOpeConts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getDeployCode(), po.getMachineCode());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String deployCode) {
        DeployPO po = selectOneByDeployCode(tenantCode, deployCode);
        if (po == null) {
            return DBOpeConts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, deployCode);
        if (deleted == DBOpeConts.DELETED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getDeployCode(), po.getMachineCode());
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

    private String getCacheKey(String tenantCode, String deployCode, String machineCode) {
        return "deployAcc-" + tenantCode + "-" + deployCode + "-" + machineCode;
    }

    private String getCacheListKey(String tenantCode) {
        return "deployAcc-" + tenantCode;
    }

    private DeployPO getCache(String tenantCode, String deployCode, String machineCode) {
        String key = getCacheKey(deployCode, deployCode, machineCode);
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

    private void setCache(String tenantCode, String deployCode, String machineCode, DeployPO po) {
        String key = getCacheKey(deployCode, deployCode, machineCode);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String deployCode, String machineCode) {
        redisManager.deleteKey(getCacheKey(tenantCode, deployCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, machineCode));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
