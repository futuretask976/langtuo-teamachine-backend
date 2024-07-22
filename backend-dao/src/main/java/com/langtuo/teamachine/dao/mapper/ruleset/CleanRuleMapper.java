package com.langtuo.teamachine.dao.mapper.ruleset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.ruleset.CleanRulePO;
import com.langtuo.teamachine.dao.query.ruleset.CleanRuleQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface CleanRuleMapper {
    /**
     *
     * @param tenantCode
     * @param cleanRuleCode
     * @return
     */
    CleanRulePO selectOne(@Param("tenantCode") String tenantCode, @Param("cleanRuleCode") String cleanRuleCode,
            @Param("cleanRuleName") String cleanRuleName);

    /**
     *
     * @return
     */
    List<CleanRulePO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<CleanRulePO> search(CleanRuleQuery query);

    /**
     *
     * @param cleanRulePO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(CleanRulePO cleanRulePO);

    /**
     *
     * @param cleanRulePO
     * @return
     */
    int update(CleanRulePO cleanRulePO);

    /**
     *
     * @param tenantCode
     * @param cleanRuleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("cleanRuleCode") String cleanRuleCode);
}
