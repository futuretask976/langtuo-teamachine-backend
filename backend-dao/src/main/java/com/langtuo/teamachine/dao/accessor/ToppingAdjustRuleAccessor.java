package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.ToppingAdjustRuleMapper;
import com.langtuo.teamachine.dao.po.ToppingAdjustRulePO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ToppingAdjustRuleAccessor {
    @Resource
    private ToppingAdjustRuleMapper mapper;

    public ToppingAdjustRulePO selectOneByCode(String tenantCode, String teaCode, String teaUnitCode,
            String toppingCode) {
        return mapper.selectOne(tenantCode, teaCode, teaUnitCode, toppingCode);
    }

    public List<ToppingAdjustRulePO> selectList(String tenantCode, String teaCode, String teaUnitCode) {
        List<ToppingAdjustRulePO> list = mapper.selectList(tenantCode, teaCode, teaUnitCode);
        return list;
    }

    public int insert(ToppingAdjustRulePO teaUnitPO) {
        return mapper.insert(teaUnitPO);
    }

    public int delete(String tenantCode, String teaCode) {
        return mapper.delete(tenantCode, teaCode);
    }
}
