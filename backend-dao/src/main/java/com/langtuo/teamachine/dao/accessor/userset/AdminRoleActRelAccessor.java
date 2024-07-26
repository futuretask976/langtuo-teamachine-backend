package com.langtuo.teamachine.dao.accessor.userset;

import com.langtuo.teamachine.dao.mapper.userset.RoleActRelMapper;
import com.langtuo.teamachine.dao.po.userset.AdminRoleActRelPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AdminRoleActRelAccessor {
    @Resource
    private RoleActRelMapper mapper;

    public AdminRoleActRelPO selectOne(String tenantCode, String roleCode, String permitActCode) {
        return mapper.selectOne(tenantCode, roleCode, permitActCode);
    }

    public List<AdminRoleActRelPO> selectList(String tenantCode, String roleCode) {
        List<AdminRoleActRelPO> list = mapper.selectList(tenantCode, roleCode);
        return list;
    }

    public int insert(AdminRoleActRelPO adminRoleActRelPO) {
        return mapper.insert(adminRoleActRelPO);
    }

    public int delete(String tenantCode, String roleCode) {
        return mapper.delete(tenantCode, roleCode);
    }
}
