package com.langtuo.teamachine.dao.accessor.deviceset;

import com.langtuo.teamachine.dao.mapper.deviceset.ModelPipelineMapper;
import com.langtuo.teamachine.dao.po.deviceset.ModelPipelinePO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ModelPipelineAccessor {
    @Resource
    private ModelPipelineMapper mapper;

    public ModelPipelinePO selectOne(String modelCode, String pipelineNum) {
        return mapper.selectOne(modelCode, pipelineNum);
    }

    public List<ModelPipelinePO> selectList(String modeCode) {
        return mapper.selectList(modeCode);
    }

    public int insert(ModelPipelinePO po) {
        return mapper.insert(po);
    }

    public int delete(String modelCode) {
        return mapper.delete(modelCode);
    }
}
