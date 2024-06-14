package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.langtuo.MachineToppingMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MachineToppingMapperAccessor {
    @Resource
    private MachineToppingMapper mapper;

    public List<MachineToppingPojo> list() {
        return mapper.list();
    }

    public MachineToppingPojo get(String machineCode, String toppingCode) {
        return mapper.get(machineCode, toppingCode);
    }
}
