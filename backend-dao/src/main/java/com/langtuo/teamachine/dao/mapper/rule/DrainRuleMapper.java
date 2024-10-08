package com.langtuo.teamachine.dao.mapper.rule;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.rule.DrainRulePO;
import com.langtuo.teamachine.dao.query.rule.DrainRuleQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DrainRuleMapper {
    /**
     *
     * @param tenantCode
     * @param drainRuleCode
     * @return
     */
    DrainRulePO selectOne(@Param("tenantCode") String tenantCode, @Param("drainRuleCode") String drainRuleCode);

    /**
     *
     * @return
     */
    List<DrainRulePO> selectList(@Param("tenantCode") String tenantCode,
            @Param("drainRuleCodeList") List<String> drainRuleCodeList);

    /**
     *
     * @return
     */
    List<DrainRulePO> search(DrainRuleQuery query);

    /**
     *
     * @param drainRulePO
     * @return
     */
    int insert(DrainRulePO drainRulePO);

    /**
     *
     * @param drainRulePO
     * @return
     */
    int update(DrainRulePO drainRulePO);

    /**
     *
     * @param tenantCode
     * @param drainRuleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("drainRuleCode") String drainRuleCode);
}
