package com.langtuo.teamachine.dao.accessor.deviceset;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.deviceset.ModelPipelineMapper;
import com.langtuo.teamachine.dao.po.deviceset.ModelPipelinePO;
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
        return mapper.insert(po);
    }

    public int delete(String modelCode) {
        int deleted = mapper.delete(modelCode);
        if (deleted == 1) {
            deleteCacheAll(modelCode);
        }
        return deleted;
    }

    private String getCacheKey(String modelCode) {
        return "model_pipeline_acc_" + modelCode;
    }

    private String getCacheListKey() {
        return "model_pipeline_acc";
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

    private void deleteCacheAll(String modelCode) {
        redisManager.deleteKey(getCacheKey(modelCode));
        redisManager.deleteKey(getCacheListKey());
    }
}
