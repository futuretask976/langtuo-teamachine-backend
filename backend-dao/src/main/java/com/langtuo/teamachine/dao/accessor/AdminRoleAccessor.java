package com.langtuo.teamachine.dao.accessor;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.AdminRoleMapper;
import com.langtuo.teamachine.dao.po.AdminRolePO;
import com.langtuo.teamachine.dao.query.AdminRoleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AdminRoleAccessor {
    @Resource
    private AdminRoleMapper adminRoleMapper;

    public AdminRolePO selectOne(String tenantCode, String roleCode) {
        return adminRoleMapper.selectOne(tenantCode, roleCode);
    }

    public List<AdminRolePO> selectList(String tenantCode) {
        List<AdminRolePO> list = adminRoleMapper.selectList(tenantCode);

        return list;
    }

    public PageInfo<AdminRolePO> selectList(String tenantCode, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<AdminRolePO> list = adminRoleMapper.selectList(tenantCode);

        PageInfo<AdminRolePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public PageInfo<AdminRolePO> search(String tenantCode, String roleName, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        AdminRoleQuery adminRoleQuery = new AdminRoleQuery();
        adminRoleQuery.setTenantCode(tenantCode);
        adminRoleQuery.setRoleName(StringUtils.isBlank(roleName) ? null : roleName);
        List<AdminRolePO> list = adminRoleMapper.search(adminRoleQuery);

        PageInfo<AdminRolePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(AdminRolePO adminRolePO) {
        return adminRoleMapper.insert(adminRolePO);
    }

    public int update(AdminRolePO adminRolePO) {
        return adminRoleMapper.update(adminRolePO);
    }

    public int delete(String tenantCode, String roleCode) {
        return adminRoleMapper.delete(tenantCode, roleCode);
    }
}
