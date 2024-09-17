package com.langtuo.teamachine.dao.accessor.device;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.device.ModelMapper;
import com.langtuo.teamachine.dao.po.device.ModelPO;
import com.langtuo.teamachine.dao.query.device.MachineModelQuery;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ModelAccessor {
    @Resource
    private ModelMapper mapper;

    @Resource
    private RedisManager redisManager;

    public ModelPO getByModelCode(String modelCode) {
        // 首先访问缓存
        ModelPO cached = getCache(modelCode);
        if (cached != null) {
            return cached;
        }

        ModelPO po = mapper.selectOne(modelCode);

        // 设置缓存
        setCache(modelCode, po);
        return po;
    }

    public List<ModelPO> list() {
        // 首先访问缓存
        List<ModelPO> cachedList = getCacheList();
        if (cachedList != null) {
            return cachedList;
        }

        List<ModelPO> list = mapper.selectList();

        // 设置缓存
        setCacheList(list);
        return list;
    }

    public PageInfo<ModelPO> search(String modelCode, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        MachineModelQuery machineModelQuery = new MachineModelQuery();
        machineModelQuery.setModelCode(StringUtils.isBlank(modelCode) ? null : modelCode);
        List<ModelPO> list = mapper.search(machineModelQuery);

        PageInfo<ModelPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(ModelPO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getModelCode());
            deleteCacheList();
        }
        return inserted;
    }

    public int update(ModelPO po) {
        int updated = mapper.update(po);
        if (updated == CommonConsts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getModelCode());
            deleteCacheList();
        }
        return updated;
    }

    public int deleteByModelCode(String modelCode) {
        int deleted = mapper.delete(modelCode);
        if (deleted == CommonConsts.DELETED_ONE_ROW) {
            deleteCacheOne(modelCode);
            deleteCacheList();
        }
        return deleted;
    }

    private String getCacheKey(String modelCode) {
        return "modelAcc-" + modelCode;
    }

    private String getCacheListKey() {
        return "modelAcc";
    }

    private ModelPO getCache(String modelCode) {
        String key = getCacheKey(modelCode);
        Object cached = redisManager.getValue(key);
        ModelPO po = (ModelPO) cached;
        return po;
    }

    private List<ModelPO> getCacheList() {
        String key = getCacheListKey();
        Object cached = redisManager.getValue(key);
        List<ModelPO> poList = (List<ModelPO>) cached;
        return poList;
    }

    private void setCacheList(List<ModelPO> poList) {
        String key = getCacheListKey();
        redisManager.setValue(key, poList);
    }

    private void setCache(String modelCode, ModelPO po) {
        String key = getCacheKey(modelCode);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String modelCode) {
        redisManager.deleteKey(getCacheKey(modelCode));
    }

    private void deleteCacheList() {
        redisManager.deleteKey(getCacheListKey());
    }
}
