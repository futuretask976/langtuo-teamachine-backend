package com.langtuo.teamachine.dao.accessor.device;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.constant.DBOpeConts;
import com.langtuo.teamachine.dao.mapper.device.ModelPipelineMapper;
import com.langtuo.teamachine.dao.po.device.ModelPipelinePO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ModelPipelineAccessor {
    @Resource
    private ModelPipelineMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<ModelPipelinePO> selectList(String modeCode) {
        // 首先访问缓存
        List<ModelPipelinePO> cachedList = getCacheList(modeCode);
        if (cachedList != null) {
            return cachedList;
        }
        
        List<ModelPipelinePO> list = mapper.selectList(modeCode);

        // 设置缓存
        setCacheList(list);
        return list;
    }

    public int insert(ModelPipelinePO po) {
        int inserted = mapper.insert(po);
        if (inserted == DBOpeConts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getModelCode());
            deleteCacheList();
        }
        return inserted;
    }

    public int delete(String modelCode) {
        int deleted = mapper.delete(modelCode);
        if (deleted == DBOpeConts.DELETED_ONE_ROW) {
            deleteCacheOne(modelCode);
            deleteCacheList();
        }
        return deleted;
    }

    private String getCacheKey(String modelCode) {
        return "modelPlAcc-" + modelCode;
    }

    private String getCacheListKey() {
        return "modelPlAcc";
    }

    private List<ModelPipelinePO> getCacheList(String modelCode) {
        String key = getCacheKey(modelCode);
        Object cached = redisManager.getValue(key);
        List<ModelPipelinePO> poList = (List<ModelPipelinePO>) cached;
        return poList;
    }

    private void setCacheList(List<ModelPipelinePO> poList) {
        String key = getCacheListKey();
        redisManager.setValue(key, poList);
    }

    private void deleteCacheOne(String modelCode) {
        redisManager.deleteKey(getCacheKey(modelCode));
    }

    private void deleteCacheList() {
        redisManager.deleteKey(getCacheListKey());
    }
}
