package com.langtuo.teamachine.dao.accessor.userset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.userset.AdminMapper;
import com.langtuo.teamachine.dao.po.userset.AdminPO;
import com.langtuo.teamachine.dao.query.userset.AdminQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AdminAccessor {
    @Resource
    private AdminMapper mapper;

    public AdminPO selectOne(String tenantCode, String loginName) {
        return mapper.selectOne(tenantCode, loginName);
    }

    public List<AdminPO> selectList(String tenantCode) {
        List<AdminPO> list = mapper.selectList(tenantCode);

        return list;
    }

    public PageInfo<AdminPO> search(String tenantCode, String loginName, String roleCode, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        AdminQuery adminQuery = new AdminQuery();
        adminQuery.setTenantCode(tenantCode);
        adminQuery.setLoginName(StringUtils.isBlank(loginName) ? null : loginName);
        adminQuery.setRoleCode(StringUtils.isBlank(roleCode) ? null : roleCode);
        List<AdminPO> list = mapper.search(adminQuery);

        PageInfo<AdminPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(AdminPO adminPO) {
        return mapper.insert(adminPO);
    }

    public int update(AdminPO adminPO) {
        return mapper.update(adminPO);
    }

    public int delete(String tenantCode, String loginName) {
        return mapper.delete(tenantCode, loginName);
    }
}
