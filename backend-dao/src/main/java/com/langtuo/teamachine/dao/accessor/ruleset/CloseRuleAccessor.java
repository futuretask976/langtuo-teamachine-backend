package com.langtuo.teamachine.dao.accessor.ruleset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.ruleset.CleanRuleMapper;
import com.langtuo.teamachine.dao.mapper.ruleset.CloseRuleMapper;
import com.langtuo.teamachine.dao.po.ruleset.CloseRulePO;
import com.langtuo.teamachine.dao.query.ruleset.CloseRuleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CloseRuleAccessor {
    @Resource
    private CloseRuleMapper mapper;

    public CloseRulePO selectOneByCode(String tenantCode, String closeRuleCode) {
        return mapper.selectOne(tenantCode, closeRuleCode, null);
    }

    public CloseRulePO selectOneByName(String tenantCode, String closeRuleName) {
        return mapper.selectOne(tenantCode, null, closeRuleName);
    }

    public List<CloseRulePO> selectList(String tenantCode) {
        List<CloseRulePO> list = mapper.selectList(tenantCode);
        return list;
    }

    public PageInfo<CloseRulePO> search(String tenantCode, String closeRuleCode, String closeRuleName, 
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        CloseRuleQuery query = new CloseRuleQuery();
        query.setTenantCode(tenantCode);
        query.setCloseRuleCode(StringUtils.isBlank(closeRuleCode) ? null : closeRuleCode);
        query.setCloseRuleName(StringUtils.isBlank(closeRuleName) ? null : closeRuleName);
        List<CloseRulePO> list = mapper.search(query);

        PageInfo<CloseRulePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int update(CloseRulePO po) {
        return mapper.update(po);
    }

    public int insert(CloseRulePO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String closeRuleCode) {
        return mapper.delete(tenantCode, closeRuleCode);
    }
}
