package com.langtuo.teamachine.dao.accessor;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.OrgStrucMapper;
import com.langtuo.teamachine.dao.po.OrgStrucPO;
import com.langtuo.teamachine.dao.query.OrgStrucQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OrgStrucAccessor {
    @Resource
    private OrgStrucMapper mapper;

    public OrgStrucPO selectOne(String tenantCode, String orgName) {
        return mapper.selectOne(tenantCode, orgName);
    }

    public List<OrgStrucPO> selectList(String tenantCode) {
        List<OrgStrucPO> list = mapper.selectList(tenantCode);
        return list;
    }

    public PageInfo<OrgStrucPO> search(String tenantCode, String orgName, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OrgStrucQuery query = new OrgStrucQuery();
        query.setTenantCode(StringUtils.isBlank(tenantCode) ? null : tenantCode);
        query.setOrgName(StringUtils.isBlank(orgName) ? null : orgName);
        List<OrgStrucPO> list = mapper.search(query);

        PageInfo<OrgStrucPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(OrgStrucPO orgStrucPO) {
        return mapper.insert(orgStrucPO);
    }

    public int update(OrgStrucPO orgStrucPO) {
        return mapper.update(orgStrucPO);
    }

    public int delete(String tenantCode, String orgName) {
        return mapper.delete(tenantCode, orgName);
    }
}
