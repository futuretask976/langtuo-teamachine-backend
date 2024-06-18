package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.PermActMapper;
import com.langtuo.teamachine.dao.mapper.TenantMapper;
import com.langtuo.teamachine.dao.po.PermActPO;
import com.langtuo.teamachine.dao.po.TenantPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class PermActAccessor {
    @Resource
    private PermActMapper mapper;

    public PermActPO selectOne(String permActCode) {
        return mapper.selectOne(permActCode);
    }

    public List<PermActPO> selectList() {
        return mapper.selectList();
    }
}
