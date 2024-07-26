package com.langtuo.teamachine.dao.accessor.userset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.userset.RoleMapper;
import com.langtuo.teamachine.dao.po.userset.RolePO;
import com.langtuo.teamachine.dao.query.userset.AdminRoleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AdminRoleAccessor {
    @Resource
    private RoleMapper mapper;

    public RolePO selectOne(String tenantCode, String roleCode) {
        return mapper.selectOne(tenantCode, roleCode);
    }

    public List<RolePO> selectList(String tenantCode) {
        List<RolePO> list = mapper.selectList(tenantCode);

        return list;
    }

    public PageInfo<RolePO> selectList(String tenantCode, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<RolePO> list = mapper.selectList(tenantCode);

        PageInfo<RolePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public PageInfo<RolePO> search(String tenantCode, String roleName, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        AdminRoleQuery adminRoleQuery = new AdminRoleQuery();
        adminRoleQuery.setTenantCode(tenantCode);
        adminRoleQuery.setRoleName(StringUtils.isBlank(roleName) ? null : roleName);
        List<RolePO> list = mapper.search(adminRoleQuery);

        PageInfo<RolePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(RolePO rolePO) {
        return mapper.insert(rolePO);
    }

    public int update(RolePO rolePO) {
        return mapper.update(rolePO);
    }

    public int delete(String tenantCode, String roleCode) {
        return mapper.delete(tenantCode, roleCode);
    }
}
