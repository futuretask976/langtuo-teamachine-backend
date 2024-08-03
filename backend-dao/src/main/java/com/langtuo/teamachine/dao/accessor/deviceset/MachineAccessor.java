package com.langtuo.teamachine.dao.accessor.deviceset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.deviceset.MachineMapper;
import com.langtuo.teamachine.dao.po.deviceset.MachinePO;
import com.langtuo.teamachine.dao.query.deviceset.MachineQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MachineAccessor {
    @Resource
    private MachineMapper mapper;

    @Resource
    private RedisManager redisManager;

    public MachinePO selectOne(String tenantCode, String machineCode) {
        // 首先访问缓存
        MachinePO cached = getCachedMachine(tenantCode, machineCode);
        if (cached != null) {
            return cached;
        }

        MachinePO po = mapper.selectOne(tenantCode, machineCode, null);

        // 设置缓存
        setCachedMachine(tenantCode, machineCode, po);
        return po;
    }

    public List<MachinePO> selectList(String tenantCode) {
        // 首先访问缓存
        List<MachinePO> cachedList = getCachedMachineList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<MachinePO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCachedMachineList(tenantCode, list);
        return list;
    }

    public PageInfo<MachinePO> search(String tenantCode, String screenCode, String elecBoardCode,
            String modelCode, String shopCode, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        MachineQuery machineQuery = new MachineQuery();
        machineQuery.setTenantCode(tenantCode);
        machineQuery.setScreenCode(StringUtils.isBlank(screenCode) ? null : screenCode);
        machineQuery.setElecBoardCode(StringUtils.isBlank(elecBoardCode) ? null : elecBoardCode);
        machineQuery.setModelCode(StringUtils.isBlank(modelCode) ? null : modelCode);
        machineQuery.setShopCode(StringUtils.isBlank(shopCode) ? null : shopCode);
        List<MachinePO> list = mapper.search(machineQuery);

        PageInfo<MachinePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(MachinePO po) {
        return mapper.insert(po);
    }

    public int update(MachinePO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCachedMachine(po.getTenantCode(), po.getMachineCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String machineCode) {
        int deleted = mapper.delete(tenantCode, machineCode);
        if (deleted == 1) {
            deleteCachedMachine(tenantCode, machineCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String machineCode) {
        return "machine_acc_" + tenantCode + "-" + machineCode;
    }

    private String getCacheKey(String tenantCode) {
        return "machine_acc_" + tenantCode;
    }

    private MachinePO getCachedMachine(String tenantCode, String machineCode) {
        String key = getCacheKey(tenantCode, machineCode);
        Object cached = redisManager.getValue(key);
        MachinePO po = (MachinePO) cached;
        return po;
    }

    private List<MachinePO> getCachedMachineList(String tenantCode) {
        String key = getCacheKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<MachinePO> poList = (List<MachinePO>) cached;
        return poList;
    }

    private void setCachedMachineList(String tenantCode, List<MachinePO> poList) {
        String key = getCacheKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCachedMachine(String tenantCode, String machineCode, MachinePO po) {
        String key = getCacheKey(tenantCode, machineCode);
        redisManager.setValue(key, po);
    }

    private void deleteCachedMachine(String tenantCode, String machineCode) {
        redisManager.deleteKey(getCacheKey(tenantCode, machineCode));
        redisManager.deleteKey(getCacheKey(tenantCode));
    }
}
