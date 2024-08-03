package com.langtuo.teamachine.dao.accessor.rule;

import com.langtuo.teamachine.dao.mapper.rule.CleanRuleExceptMapper;
import com.langtuo.teamachine.dao.po.rule.CleanRuleExceptPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CleanRuleExceptAccessor {
    @Resource
    private CleanRuleExceptMapper mapper;

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
