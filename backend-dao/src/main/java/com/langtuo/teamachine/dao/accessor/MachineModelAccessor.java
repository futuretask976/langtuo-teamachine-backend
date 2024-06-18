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

    public MachineModelPO selectOne(String machineModelCode) {
        return mapper.selectOne(machineModelCode);
    }

    public List<MachineModelPO> selectList() {
        return mapper.selectList();
    }
}
