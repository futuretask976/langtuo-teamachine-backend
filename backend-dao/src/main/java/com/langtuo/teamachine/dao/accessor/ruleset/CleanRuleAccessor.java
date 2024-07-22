package com.langtuo.teamachine.dao.accessor.ruleset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.ruleset.CleanRuleMapper;
import com.langtuo.teamachine.dao.po.ruleset.CleanRulePO;
import com.langtuo.teamachine.dao.query.ruleset.CleanRuleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CleanRuleAccessor {
    @Resource
    private CleanRuleMapper mapper;

    public CleanRulePO selectOneByCode(String tenantCode, String cleanRuleCode) {
        return mapper.selectOne(tenantCode, cleanRuleCode, null);
    }

    public CleanRulePO selectOneByName(String tenantCode, String cleanRuleName) {
        return mapper.selectOne(tenantCode, null, cleanRuleName);
    }

    public List<CleanRulePO> selectList(String tenantCode) {
        List<CleanRulePO> list = mapper.selectList(tenantCode);
        return list;
    }

    public PageInfo<CleanRulePO> search(String tenantCode, String cleanRuleCode, String cleanRuleName, 
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        CleanRuleQuery query = new CleanRuleQuery();
        query.setTenantCode(tenantCode);
        query.setCleanRuleCode(StringUtils.isBlank(cleanRuleCode) ? null : cleanRuleCode);
        query.setCleanRuleName(StringUtils.isBlank(cleanRuleName) ? null : cleanRuleName);
        List<CleanRulePO> list = mapper.search(query);

        PageInfo<CleanRulePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int update(CleanRulePO po) {
        return mapper.update(po);
    }

    public int insert(CleanRulePO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String cleanRuleCode) {
        return mapper.delete(tenantCode, cleanRuleCode);
    }
}
