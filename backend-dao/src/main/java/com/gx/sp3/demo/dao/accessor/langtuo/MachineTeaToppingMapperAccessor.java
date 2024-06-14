package com.gx.sp3.demo.dao.accessor.langtuo;

import com.gx.sp3.demo.dao.mapper.langtuo.MachineTeaToppingMapper;
import com.gx.sp3.demo.dao.pojo.langtuo.MachineTeaToppingPojo;
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
