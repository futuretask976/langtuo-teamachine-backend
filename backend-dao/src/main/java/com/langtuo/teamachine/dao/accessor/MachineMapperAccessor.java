package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.langtuo.MachineMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MachineMapperAccessor {
    @Resource
    private MachineMapper mapper;

    public List<MachinePojo> list() {
        return mapper.list();
    }

    public MachinePojo get(String machineCode) {
        return mapper.get(machineCode);
    }
}
