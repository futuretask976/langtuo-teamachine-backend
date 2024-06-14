package com.gx.sp3.demo.dao.accessor.langtuo;

import com.gx.sp3.demo.dao.mapper.langtuo.MachineTeaMapper;
import com.gx.sp3.demo.dao.pojo.langtuo.MachineTeaPojo;
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
