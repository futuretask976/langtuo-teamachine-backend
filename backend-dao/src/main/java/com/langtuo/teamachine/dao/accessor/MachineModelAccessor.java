package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.MachineModelMapper;
import com.langtuo.teamachine.dao.po.MachineModelPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MachineModelAccessor {
    @Resource
    private MachineModelMapper mapper;

    public MachineModelPO selectOne(String modelCode) {
        return mapper.selectOne(modelCode);
    }

    public List<MachineModelPO> selectList() {
        return mapper.selectList();
    }

    public int insert(MachineModelPO machineModelPO) {
        return mapper.insert(machineModelPO);
    }

    public int delete(String modelCode) {
        return mapper.delete(modelCode);
    }
}
