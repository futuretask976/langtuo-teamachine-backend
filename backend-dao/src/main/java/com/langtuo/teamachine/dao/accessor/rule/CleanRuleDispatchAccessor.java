package com.langtuo.teamachine.dao.accessor.rule;

import com.langtuo.teamachine.dao.mapper.rule.CleanRuleDispatchMapper;
import com.langtuo.teamachine.dao.po.rule.CleanRuleDispatchPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CleanRuleDispatchAccessor {
    @Resource
    private CleanRuleDispatchMapper mapper;

    public List<CleanRuleDispatchPO> selectList(String tenantCode, String menuCode) {
        List<CleanRuleDispatchPO> list = mapper.selectList(tenantCode, menuCode);
        return list;
    }

    public int insert(CleanRuleDispatchPO po) {
        return mapper.insert(po);
    }

    public int update(CleanRuleDispatchPO po) {
        return mapper.update(po);
    }

    public int delete(String tenantCode, String menuCode) {
        return mapper.delete(tenantCode, menuCode);
    }
}
