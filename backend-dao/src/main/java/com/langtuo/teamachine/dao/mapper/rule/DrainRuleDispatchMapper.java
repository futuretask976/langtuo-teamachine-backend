package com.langtuo.teamachine.dao.mapper.rule;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.rule.DrainRuleDispatchPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface DrainRuleDispatchMapper {
    /**
     *
     * @param tenantCode
     * @param drainRuleCode
     * @param shopGroupCode
     * @return
     */
    DrainRuleDispatchPO selectOne(@Param("tenantCode") String tenantCode, @Param("drainRuleCode") String drainRuleCode,
            @Param("shopGroupCode") String shopGroupCode);

    /**
     *
     * @return
     */
    List<DrainRuleDispatchPO> selectList(@Param("tenantCode") String tenantCode,
            @Param("drainRuleCode") String drainRuleCode);

    /**
     *
     * @return
     */
    List<DrainRuleDispatchPO> selectListByShopGroupCode(@Param("tenantCode") String tenantCode,
            @Param("shopGroupCode") String shopGroupCode);

    /**
     *
     * @param drainRuleDispatchPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(DrainRuleDispatchPO drainRuleDispatchPO);

    /**
     *
     * @param drainRuleDispatchPO
     * @return
     */
    int update(DrainRuleDispatchPO drainRuleDispatchPO);

    /**
     *
     * @param tenantCode
     * @param drainRuleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("drainRuleCode") String drainRuleCode);
}
