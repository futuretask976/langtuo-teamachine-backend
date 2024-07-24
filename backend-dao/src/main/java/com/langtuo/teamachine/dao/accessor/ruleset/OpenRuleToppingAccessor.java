package com.langtuo.teamachine.dao.accessor.ruleset;

import com.langtuo.teamachine.dao.mapper.ruleset.OpenRuleToppingMapper;
import com.langtuo.teamachine.dao.po.ruleset.OpenRuleToppingPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OpenRuleToppingAccessor {
    @Resource
    private OpenRuleToppingMapper mapper;

    public List<OpenRuleToppingPO> selectList(String tenantCode, String flushAirRuleCode) {
        List<OpenRuleToppingPO> list = mapper.selectList(tenantCode, flushAirRuleCode);
        return list;
    }

    public int insert(OpenRuleToppingPO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String cleanRuleCode) {
        return mapper.delete(tenantCode, cleanRuleCode);
    }
}
