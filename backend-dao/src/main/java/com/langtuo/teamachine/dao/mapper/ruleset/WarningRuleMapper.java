package com.langtuo.teamachine.dao.mapper.ruleset;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.ruleset.WarningRulePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface WarningRuleMapper {
    /**
     *
     * @param tenantCode
     * @param warningRuleCode
     * @return
     */
    WarningRulePO selectOne(@Param("tenantCode") String tenantCode, @Param("warningRuleCode") String warningRuleCode);

    /**
     *
     * @return
     */
    List<WarningRulePO> selectList();

    /**
     *
     * @param warningRulePO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(WarningRulePO warningRulePO);

    /**
     *
     * @param warningRulePO
     * @return
     */
    int update(WarningRulePO warningRulePO);

    /**
     *
     * @param tenantCode
     * @param warningRuleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("warningRuleCode") String warningRuleCode);
}
