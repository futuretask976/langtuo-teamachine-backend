package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.AdminRoleActRelMapper;
import com.langtuo.teamachine.dao.po.AdminRoleActRelPO;
import com.langtuo.teamachine.dao.po.AdminRolePO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AdminRoleActRelAccessor {
    @Resource
    private AdminRoleActRelMapper adminRoleActRelMapper;

    public AdminRoleActRelPO selectOne(String tenantCode, String roleCode, String permitActCode) {
        return adminRoleActRelMapper.selectOne(tenantCode, roleCode, permitActCode);
    }

    public List<AdminRoleActRelPO> selectList(String tenantCode, String roleCode) {
        List<AdminRoleActRelPO> list = adminRoleActRelMapper.selectList(tenantCode, roleCode);
        return list;
    }

    public int insert(AdminRoleActRelPO adminRoleActRelPO) {
        return adminRoleActRelMapper.insert(adminRoleActRelPO);
    }

    public int delete(String tenantCode, String roleCode) {
        return adminRoleActRelMapper.delete(tenantCode, roleCode);
    }
}
