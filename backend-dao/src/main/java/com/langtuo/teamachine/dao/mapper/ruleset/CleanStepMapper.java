package com.langtuo.teamachine.dao.mapper.ruleset;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.ruleset.CleanStepPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface CleanStepMapper {
    /**
     *
     * @param tenantCode
     * @param cleanRuleCode
     * @param stepNum
     * @return
     */
    CleanStepPO selectOne(@Param("tenantCode") String tenantCode, @Param("cleanRuleCode") String cleanRuleCode, @Param("stepNum") Integer stepNum);

    /**
     *
     * @return
     */
    List<CleanStepPO> selectList();

    /**
     *
     * @param cleanStepPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(CleanStepPO cleanStepPO);

    /**
     *
     * @param cleanStepPO
     * @return
     */
    int update(CleanStepPO cleanStepPO);

    /**
     *
     * @param tenantCode
     * @param cleanRuleCode
     * @param stepNum
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("cleanRuleCode") String cleanRuleCode, @Param("stepNum") Integer stepNum);
}
