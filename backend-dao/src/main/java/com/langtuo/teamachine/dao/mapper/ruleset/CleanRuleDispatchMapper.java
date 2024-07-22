package com.langtuo.teamachine.dao.mapper.ruleset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.ruleset.CleanRuleDispatchPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface CleanRuleDispatchMapper {
    /**
     *
     * @param tenantCode
     * @param cleanRuleCode
     * @param shopGroupCode
     * @return
     */
    CleanRuleDispatchPO selectOne(@Param("tenantCode") String tenantCode, @Param("cleanRuleCode") String cleanRuleCode, 
            @Param("shopGroupCode") String shopGroupCode);

    /**
     *
     * @return
     */
    List<CleanRuleDispatchPO> selectList(@Param("tenantCode") String tenantCode,
            @Param("cleanRuleCode") String cleanRuleCode);

    /**
     *
     * @param cleanRuleDispatchPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(CleanRuleDispatchPO cleanRuleDispatchPO);

    /**
     *
     * @param cleanRuleDispatchPO
     * @return
     */
    int update(CleanRuleDispatchPO cleanRuleDispatchPO);

    /**
     *
     * @param tenantCode
     * @param cleanRuleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("cleanRuleCode") String cleanRuleCode);
}
