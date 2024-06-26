package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.MachineModelPipelineMapper;
import com.langtuo.teamachine.dao.po.MachineModelPipelinePO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MachineModelPipelineAccessor {
    @Resource
    private MachineModelPipelineMapper mapper;

    public MachineModelPipelinePO selectOne(String modelCode, String pipelineNum) {
        return mapper.selectOne(modelCode, pipelineNum);
    }

    public List<MachineModelPipelinePO> selectList(String modeCode) {
        return mapper.selectList(modeCode);
    }

    public int insert(MachineModelPipelinePO po) {
        return mapper.insert(po);
    }

    public int delete(String modelCode) {
        return mapper.delete(modelCode);
    }
}
