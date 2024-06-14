package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.langtuo.MachineTeaToppingMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MachineTeaToppingMapperAccessor {
    @Resource
    private MachineTeaToppingMapper mapper;

    public List<MachineTeaToppingPojo> list() {
        return mapper.list();
    }

    public MachineTeaToppingPojo get(String machineCode, String teaCode, String toppingCode) {
        return mapper.get(machineCode, teaCode, toppingCode);
    }
}
