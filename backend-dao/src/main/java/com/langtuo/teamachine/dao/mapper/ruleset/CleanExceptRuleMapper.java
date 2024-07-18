package com.langtuo.teamachine.dao.mapper.ruleset;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.ruleset.CleanExceptRulePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
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
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
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
