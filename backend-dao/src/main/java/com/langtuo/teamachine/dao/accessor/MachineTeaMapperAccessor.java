package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.langtuo.MachineTeaMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MachineTeaMapperAccessor {
    @Resource
    private MachineTeaMapper mapper;

    public List<MachineTeaPojo> list() {
        return mapper.list();
    }

    public MachineTeaPojo get(String machineCode, String teaCode) {
        return mapper.get(machineCode, teaCode);
    }
}
