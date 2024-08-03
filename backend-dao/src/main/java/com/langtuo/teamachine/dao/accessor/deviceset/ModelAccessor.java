package com.langtuo.teamachine.dao.accessor.deviceset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.deviceset.ModelMapper;
import com.langtuo.teamachine.dao.po.deviceset.ModelPO;
import com.langtuo.teamachine.dao.query.deviceset.MachineModelQuery;
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

    public ModelPO selectOne(String modelCode) {
        // 首先访问缓存
        ModelPO cached = getCachedModel(modelCode);
        if (cached != null) {
            return cached;
        }

        ModelPO po = mapper.selectOne(modelCode);

        // 设置缓存
        setCachedModel(modelCode, po);
        return po;
    }

    public List<ModelPO> selectList() {
        // 首先访问缓存
        List<ModelPO> cachedList = getCachedModelList();
        if (cachedList != null) {
            return cachedList;
        }

        List<ModelPO> list = mapper.selectList();

        // 设置缓存
        setCachedModelList(list);
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
        return mapper.insert(po);
    }

    public int update(ModelPO po) {
        return mapper.update(po);
    }

    public int delete(String modelCode) {
        int deleted = mapper.delete(modelCode);
        if (deleted == 1) {
            deleteCachedModel(modelCode);
        }
        return deleted;
    }

    private String getCacheKey(String modelCode) {
        return "model_acc_" + modelCode;
    }

    private String getCacheKey() {
        return "model_acc";
    }

    private ModelPO getCachedModel(String modelCode) {
        String key = getCacheKey(modelCode);
        Object cached = redisManager.getValue(key);
        ModelPO po = (ModelPO) cached;
        return po;
    }

    private List<ModelPO> getCachedModelList() {
        String key = getCacheKey();
        Object cached = redisManager.getValue(key);
        List<ModelPO> poList = (List<ModelPO>) cached;
        return poList;
    }

    private void setCachedModelList(List<ModelPO> poList) {
        String key = getCacheKey();
        redisManager.setValue(key, poList);
    }

    private void setCachedModel(String modelCode, ModelPO po) {
        String key = getCacheKey(modelCode);
        redisManager.setValue(key, po);
    }

    private void deleteCachedModel(String modelCode) {
        redisManager.deleteKey(getCacheKey(modelCode));
        redisManager.deleteKey(getCacheKey());
    }
}
