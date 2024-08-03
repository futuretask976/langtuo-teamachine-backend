package com.langtuo.teamachine.dao.accessor.rule;

import com.langtuo.teamachine.dao.mapper.rule.CleanRuleStepMapper;
import com.langtuo.teamachine.dao.po.rule.CleanRuleStepPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CleanRuleStepAccessor {
    @Resource
    private CleanRuleStepMapper mapper;

    public CleanRuleStepPO selectOne(String tenantCode, String cleanRuleCode, int cleanStepIndex) {
        return mapper.selectOne(tenantCode, cleanRuleCode, cleanStepIndex);
    }

    public List<CleanRuleStepPO> selectList(String tenantCode, String cleanRuleCode) {
        List<CleanRuleStepPO> list = mapper.selectList(tenantCode, cleanRuleCode);
        return list;
    }

    public int insert(CleanRuleStepPO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String cleanRuleCode) {
        return mapper.delete(tenantCode, cleanRuleCode);
    }
}
