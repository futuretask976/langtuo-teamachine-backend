package com.langtuo.teamachine.dao.accessor.rule;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.rule.OpenRuleMapper;
import com.langtuo.teamachine.dao.po.rule.OpenRulePO;
import com.langtuo.teamachine.dao.query.rule.OpenRuleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OpenRuleAccessor {
    @Resource
    private OpenRuleMapper mapper;

    public OpenRulePO selectOneByCode(String tenantCode, String flushAirRuleCode) {
        return mapper.selectOne(tenantCode, flushAirRuleCode, null);
    }

    public OpenRulePO selectOneByName(String tenantCode, String flushAirRuleName) {
        return mapper.selectOne(tenantCode, null, flushAirRuleName);
    }

    public List<OpenRulePO> selectList(String tenantCode) {
        List<OpenRulePO> list = mapper.selectList(tenantCode);
        return list;
    }

    public PageInfo<OpenRulePO> search(String tenantCode, String flushAirRuleCode, String flushAirRuleName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OpenRuleQuery query = new OpenRuleQuery();
        query.setTenantCode(tenantCode);
        query.setOpenRuleCode(StringUtils.isBlank(flushAirRuleCode) ? null : flushAirRuleCode);
        query.setOpenRuleName(StringUtils.isBlank(flushAirRuleName) ? null : flushAirRuleName);
        List<OpenRulePO> list = mapper.search(query);

        PageInfo<OpenRulePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int update(OpenRulePO po) {
        return mapper.update(po);
    }

    public int insert(OpenRulePO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String flushAirRuleCode) {
        return mapper.delete(tenantCode, flushAirRuleCode);
    }
}
