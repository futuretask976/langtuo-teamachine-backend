package com.langtuo.teamachine.dao.mapper.ruleset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.ruleset.WarningRulePO;
import com.langtuo.teamachine.dao.query.ruleset.WarningRuleQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface WarningRuleMapper {
    /**
     *
     * @param tenantCode
     * @param warningRuleCode
     * @return
     */
    WarningRulePO selectOne(@Param("tenantCode") String tenantCode, @Param("warningRuleCode") String warningRuleCode,
            @Param("warningRuleName") String warningRuleName);

    /**
     *
     * @return
     */
    List<WarningRulePO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<WarningRulePO> search(WarningRuleQuery query);

    /**
     *
     * @param warningRulePO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
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
