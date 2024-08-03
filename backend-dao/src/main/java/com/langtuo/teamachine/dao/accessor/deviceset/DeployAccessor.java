package com.langtuo.teamachine.dao.accessor.deviceset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.deviceset.DeployMapper;
import com.langtuo.teamachine.dao.po.deviceset.DeployPO;
import com.langtuo.teamachine.dao.query.deviceset.MachineDeployQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DeployAccessor {
    @Resource
    private DeployMapper mapper;

    @Resource
    private RedisManager redisManager;

    public DeployPO selectOne(String tenantCode, String deployCode) {
        // 首先访问缓存
        DeployPO cached = getCachedDeploy(tenantCode, deployCode);
        if (cached != null) {
            return cached;
        }
        
        DeployPO po = mapper.selectOne(tenantCode, deployCode);

        // 设置缓存
        setCachedDeploy(tenantCode, deployCode, po);
        return po;
    }

    public List<DeployPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<DeployPO> cachedList = getCachedDeployList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<DeployPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCachedDeployList(tenantCode, list);
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
        return mapper.insert(po);
    }

    public int update(DeployPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCachedDeploy(po.getTenantCode(), po.getDeployCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String deployCode) {
        int deleted = mapper.delete(tenantCode, deployCode);
        if (deleted == 1) {
            deleteCachedDeploy(tenantCode, deployCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String deployCode) {
        return "deploy_acc_" + tenantCode + "-" + deployCode;
    }

    private String getCacheKey(String tenantCode) {
        return "deploy_acc_" + tenantCode;
    }

    private DeployPO getCachedDeploy(String tenantCode, String deployCode) {
        String key = getCacheKey(tenantCode, deployCode);
        Object cached = redisManager.getValue(key);
        DeployPO po = (DeployPO) cached;
        return po;
    }

    private List<DeployPO> getCachedDeployList(String tenantCode) {
        String key = getCacheKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<DeployPO> poList = (List<DeployPO>) cached;
        return poList;
    }

    private void setCachedDeployList(String tenantCode, List<DeployPO> poList) {
        String key = getCacheKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCachedDeploy(String tenantCode, String deployCode, DeployPO po) {
        String key = getCacheKey(tenantCode, deployCode);
        redisManager.setValue(key, po);
    }

    private void deleteCachedDeploy(String tenantCode, String deployCode) {
        redisManager.deleteKey(getCacheKey(tenantCode, deployCode));
        redisManager.deleteKey(getCacheKey(tenantCode));
    }
}
