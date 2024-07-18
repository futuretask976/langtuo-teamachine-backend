package com.langtuo.teamachine.dao.mapper.ruleset;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.ruleset.CleanRuleDispatchPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface CleanRuleDispatchMapper {
    /**
     *
     * @param tenantCode
     * @param cleanRuleCode
     * @param shopCode
     * @return
     */
    CleanRuleDispatchPO selectOne(@Param("tenantCode") String tenantCode, @Param("cleanRuleCode") String cleanRuleCode, @Param("shopCode") String shopCode);

    /**
     *
     * @return
     */
    List<CleanRuleDispatchPO> selectList();

    /**
     *
     * @param cleanRuleDispatchPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
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
     * @param shopCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("cleanRuleCode") String cleanRuleCode, @Param("shopCode") String shopCode);
}
