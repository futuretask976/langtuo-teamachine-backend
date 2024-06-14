package com.gx.sp3.demo.dao.accessor.langtuo;

import com.gx.sp3.demo.dao.mapper.langtuo.MachineToppingMapper;
import com.gx.sp3.demo.dao.pojo.langtuo.MachineToppingPojo;
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
