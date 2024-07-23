package com.langtuo.teamachine.dao.accessor.ruleset;

import com.langtuo.teamachine.dao.mapper.ruleset.CleanRuleExceptMapper;
import com.langtuo.teamachine.dao.po.ruleset.CleanRuleExceptPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CleanRuleExceptAccessor {
    @Resource
    private CleanRuleExceptMapper mapper;

    public CleanRuleExceptPO selectOne(String tenantCode, String cleanRuleCode, String exceptToppingCode) {
        return mapper.selectOne(tenantCode, cleanRuleCode, exceptToppingCode);
    }

    public List<CleanRuleExceptPO> selectList(String tenantCode, String cleanRuleCode) {
        List<CleanRuleExceptPO> list = mapper.selectList(tenantCode, cleanRuleCode);
        return list;
    }

    public int insert(CleanRuleExceptPO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String cleanRuleCode) {
        return mapper.delete(tenantCode, cleanRuleCode);
    }
}
