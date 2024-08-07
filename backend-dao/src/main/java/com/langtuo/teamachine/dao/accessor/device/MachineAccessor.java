package com.langtuo.teamachine.dao.accessor.device;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.device.MachineMapper;
import com.langtuo.teamachine.dao.po.device.MachinePO;
import com.langtuo.teamachine.dao.query.device.MachineQuery;
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
        MachinePO cached = getCache(tenantCode, machineCode);
        if (cached != null) {
            return cached;
        }

        MachinePO po = mapper.selectOne(tenantCode, machineCode, null);

        // 设置缓存
        setCache(tenantCode, machineCode, po);
        return po;
    }

    public List<MachinePO> selectList(String tenantCode) {
        // 首先访问缓存
        List<MachinePO> cachedList = getCacheList(tenantCode, null);
        if (cachedList != null) {
            return cachedList;
        }

        List<MachinePO> list = mapper.selectList(tenantCode, null);

        // 设置缓存
        setCacheList(tenantCode, null, list);
        return list;
    }

    public List<MachinePO> selectListByShopCode(String tenantCode, String shopCode) {
        // 首先访问缓存
        List<MachinePO> cachedList = getCacheList(tenantCode, shopCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<MachinePO> list = mapper.selectList(tenantCode, shopCode);

        // 设置缓存
        setCacheList(tenantCode, shopCode, list);
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
        int inserted = mapper.insert(po);
        if (inserted == 1) {
            deleteCacheOne(po.getTenantCode(), po.getMachineCode());
            deleteCacheList(po.getTenantCode(), po.getShopCode());
        }
        return inserted;
    }

    public int update(MachinePO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCacheOne(po.getTenantCode(), po.getMachineCode());
            deleteCacheList(po.getTenantCode(), po.getShopCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String machineCode) {
        MachinePO po = selectOne(tenantCode, machineCode);
        if (po == null) {
            return 0;
        }

        int deleted = mapper.delete(tenantCode, machineCode);
        if (deleted == 1) {
            deleteCacheOne(tenantCode, machineCode);
            deleteCacheList(tenantCode, po.getShopCode());
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String machineCode) {
        return "machineAcc-" + tenantCode + "-" + machineCode;
    }

    private String getCacheListKey(String tenantCode, String shopCode) {
        return "machineAcc-" + tenantCode + "-" + shopCode;
    }

    private MachinePO getCache(String tenantCode, String machineCode) {
        String key = getCacheKey(tenantCode, machineCode);
        Object cached = redisManager.getValue(key);
        MachinePO po = (MachinePO) cached;
        return po;
    }

    private List<MachinePO> getCacheList(String tenantCode, String shopCode) {
        String key = getCacheListKey(tenantCode, shopCode);
        Object cached = redisManager.getValue(key);
        List<MachinePO> poList = (List<MachinePO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String shopCode, List<MachinePO> poList) {
        String key = getCacheListKey(tenantCode, shopCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String machineCode, MachinePO po) {
        String key = getCacheKey(tenantCode, machineCode);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String machineCode) {
        redisManager.deleteKey(getCacheKey(tenantCode, machineCode));
    }

    private void deleteCacheList(String tenantCode, String shopCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, shopCode));
        redisManager.deleteKey(getCacheListKey(tenantCode, null));
    }
}
