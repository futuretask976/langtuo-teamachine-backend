package com.langtuo.teamachine.dao.accessor.userset;

import com.langtuo.teamachine.dao.mapper.userset.RoleActRelMapper;
import com.langtuo.teamachine.dao.po.userset.RoleActRelPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class RoleActRelAccessor {
    @Resource
    private RoleActRelMapper mapper;

    public RoleActRelPO selectOne(String tenantCode, String roleCode, String permitActCode) {
        return mapper.selectOne(tenantCode, roleCode, permitActCode);
    }

    public List<RoleActRelPO> selectList(String tenantCode, String roleCode) {
        List<RoleActRelPO> list = mapper.selectList(tenantCode, roleCode);
        return list;
    }

    public int insert(RoleActRelPO roleActRelPO) {
        return mapper.insert(roleActRelPO);
    }

    public int delete(String tenantCode, String roleCode) {
        return mapper.delete(tenantCode, roleCode);
    }
}
