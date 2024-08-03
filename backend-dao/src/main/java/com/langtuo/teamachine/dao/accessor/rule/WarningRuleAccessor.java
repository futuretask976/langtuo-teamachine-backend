package com.langtuo.teamachine.dao.accessor.rule;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.rule.WarningRuleMapper;
import com.langtuo.teamachine.dao.po.rule.WarningRulePO;
import com.langtuo.teamachine.dao.query.rule.WarningRuleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class WarningRuleAccessor {
    @Resource
    private WarningRuleMapper mapper;

    public WarningRulePO selectOneByCode(String tenantCode, String warningRuleCode) {
        return mapper.selectOne(tenantCode, warningRuleCode, null);
    }

    public WarningRulePO selectOneByName(String tenantCode, String warningRuleName) {
        return mapper.selectOne(tenantCode, null, warningRuleName);
    }

    public List<WarningRulePO> selectList(String tenantCode) {
        List<WarningRulePO> list = mapper.selectList(tenantCode);
        return list;
    }

    public PageInfo<WarningRulePO> search(String tenantCode, String warningRuleCode, String warningRuleName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        WarningRuleQuery query = new WarningRuleQuery();
        query.setTenantCode(tenantCode);
        query.setWarningRuleCode(StringUtils.isBlank(warningRuleCode) ? null : warningRuleCode);
        query.setWarningRuleName(StringUtils.isBlank(warningRuleName) ? null : warningRuleName);
        List<WarningRulePO> list = mapper.search(query);

        PageInfo<WarningRulePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int update(WarningRulePO po) {
        return mapper.update(po);
    }

    public int insert(WarningRulePO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String warningRuleCode) {
        return mapper.delete(tenantCode, warningRuleCode);
    }
}
