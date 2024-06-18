package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.MachineModelMapper;
import com.langtuo.teamachine.dao.mapper.TenantMapper;
import com.langtuo.teamachine.dao.po.MachineModelPO;
import com.langtuo.teamachine.dao.po.TenantPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TenantAccessor {
    @Resource
    private TenantMapper mapper;

    public TenantPO selectOne(String tenantCode) {
        return mapper.selectOne(tenantCode);
    }

    public List<TenantPO> selectList() {
        return mapper.selectList();
    }
}
