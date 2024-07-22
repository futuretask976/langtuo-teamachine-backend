package com.langtuo.teamachine.dao.mapper.ruleset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.ruleset.CleanExceptRulePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface CleanExceptRuleMapper {
    /**
     *
     * @param tenantCode
     * @param cleanRuleCode
     * @return
     */
    CleanExceptRulePO selectOne(@Param("tenantCode") String tenantCode, @Param("cleanRuleCode") String cleanRuleCode, @Param("exceptToppingCode") String exceptToppingCode);

    /**
     *
     * @return
     */
    List<CleanExceptRulePO> selectList();

    /**
     *
     * @param cleanExceptRulePO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(CleanExceptRulePO cleanExceptRulePO);

    /**
     *
     * @param cleanExceptRulePO
     * @return
     */
    int update(CleanExceptRulePO cleanExceptRulePO);

    /**
     *
     * @param tenantCode
     * @param cleanRuleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("cleanRuleCode") String cleanRuleCode, @Param("exceptToppingCode") String exceptToppingCode);
}
