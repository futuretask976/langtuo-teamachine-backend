package com.langtuo.teamachine.dao.accessor.userset;

import com.langtuo.teamachine.dao.mapper.userset.PermitActMapper;
import com.langtuo.teamachine.dao.po.userset.PermitActPO;
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
