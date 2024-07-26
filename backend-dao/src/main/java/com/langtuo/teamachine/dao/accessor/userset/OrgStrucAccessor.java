package com.langtuo.teamachine.dao.accessor.userset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.userset.OrgMapper;
import com.langtuo.teamachine.dao.po.userset.OrgPO;
import com.langtuo.teamachine.dao.query.userset.OrgStrucQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OrgStrucAccessor {
    @Resource
    private OrgMapper mapper;

    public OrgPO selectOne(String tenantCode, String orgName) {
        return mapper.selectOne(tenantCode, orgName);
    }

    public List<OrgPO> selectList(String tenantCode) {
        List<OrgPO> list = mapper.selectList(tenantCode);
        return list;
    }

    public PageInfo<OrgPO> search(String tenantCode, String orgName, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OrgStrucQuery query = new OrgStrucQuery();
        query.setTenantCode(StringUtils.isBlank(tenantCode) ? null : tenantCode);
        query.setOrgName(StringUtils.isBlank(orgName) ? null : orgName);
        List<OrgPO> list = mapper.search(query);

        PageInfo<OrgPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(OrgPO orgPO) {
        return mapper.insert(orgPO);
    }

    public int update(OrgPO orgPO) {
        return mapper.update(orgPO);
    }

    public int delete(String tenantCode, String orgName) {
        return mapper.delete(tenantCode, orgName);
    }
}
