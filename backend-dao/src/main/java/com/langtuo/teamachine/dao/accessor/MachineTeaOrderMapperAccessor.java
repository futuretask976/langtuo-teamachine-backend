package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.langtuo.MachineTeaOrderMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MachineTeaOrderMapperAccessor {
    @Resource
    private MachineTeaOrderMapper mapper;

    public List<MachineTeaOrderPojo> list() {
        return mapper.list();
    }

    public MachineTeaOrderPojo get(String orderId) {
        return mapper.get(orderId);
    }
}
