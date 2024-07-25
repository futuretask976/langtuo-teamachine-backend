package com.langtuo.teamachine.dao.mapper.ruleset;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.ruleset.CloseRulePO;
import com.langtuo.teamachine.dao.query.ruleset.CloseRuleQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface CloseRuleMapper {
    /**
     *
     * @param tenantCode
     * @param closeRuleCode
     * @return
     */
    CloseRulePO selectOne(@Param("tenantCode") String tenantCode, @Param("closeRuleCode") String closeRuleCode,
            @Param("closeRuleName") String closeRuleName);

    /**
     *
     * @return
     */
    List<CloseRulePO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<CloseRulePO> search(CloseRuleQuery query);

    /**
     *
     * @param cleanRulePO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(CloseRulePO cleanRulePO);

    /**
     *
     * @param cleanRulePO
     * @return
     */
    int update(CloseRulePO cleanRulePO);

    /**
     *
     * @param tenantCode
     * @param closeRuleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("closeRuleCode") String closeRuleCode);
}
