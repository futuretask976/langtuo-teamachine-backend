package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.PermitActMapper;
import com.langtuo.teamachine.dao.po.PermitActPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class PermitActAccessor {
    @Resource
    private PermitActMapper mapper;

    public PermitActPO selectOne(String permActCode) {
        return mapper.selectOne(permActCode);
    }

    public List<PermitActPO> selectList() {
        return mapper.selectList();
    }
}
