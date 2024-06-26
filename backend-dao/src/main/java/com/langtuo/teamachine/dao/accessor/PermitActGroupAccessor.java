package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.PermitActGroupMapper;
import com.langtuo.teamachine.dao.po.PermitActGroupPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class PermitActGroupAccessor {
    @Resource
    private PermitActGroupMapper mapper;

    public PermitActGroupPO selectOne(String permActCode) {
        return mapper.selectOne(permActCode);
    }

    public List<PermitActGroupPO> selectList() {
        return mapper.selectList();
    }
}
