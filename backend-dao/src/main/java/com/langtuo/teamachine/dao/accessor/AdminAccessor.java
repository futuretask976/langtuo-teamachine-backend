package com.langtuo.teamachine.dao.accessor;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.AdminMapper;
import com.langtuo.teamachine.dao.mapper.AdminRoleMapper;
import com.langtuo.teamachine.dao.po.AdminPO;
import com.langtuo.teamachine.dao.po.AdminRolePO;
import com.langtuo.teamachine.dao.query.AdminQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AdminAccessor {
    @Resource
    private AdminMapper adminMapper;

    public AdminPO selectOne(String tenantCode, String loginName) {
        return adminMapper.selectOne(tenantCode, loginName);
    }

    public PageInfo<AdminPO> selectList(String tenantCode, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<AdminPO> list = adminMapper.selectList(tenantCode);

        PageInfo<AdminPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public PageInfo<AdminPO> search(String tenantCode, String loginName, String roleCode, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        AdminQuery adminQuery = new AdminQuery();
        adminQuery.setTenantCode(tenantCode);
        adminQuery.setLoginName(StringUtils.isBlank(loginName) ? null : loginName);
        adminQuery.setRoleCode(StringUtils.isBlank(roleCode) ? null : roleCode);
        List<AdminPO> list = adminMapper.search(adminQuery);

        PageInfo<AdminPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(AdminPO adminPO) {
        return adminMapper.insert(adminPO);
    }

    public int update(AdminPO adminPO) {
        return adminMapper.update(adminPO);
    }

    public int delete(String tenantCode, String loginName) {
        return adminMapper.delete(tenantCode, loginName);
    }
}
