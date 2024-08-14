package com.langtuo.teamachine.dao.mapper.rule;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.rule.OpenRuleDispatchPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface OpenRuleDispatchMapper {
    /**
     *
     * @param tenantCode
     * @param openRuleCode
     * @param shopGroupCode
     * @return
     */
    OpenRuleDispatchPO selectOne(@Param("tenantCode") String tenantCode, @Param("openRuleCode") String openRuleCode, 
            @Param("shopGroupCode") String shopGroupCode);

    /**
     *
     * @return
     */
    List<OpenRuleDispatchPO> selectList(@Param("tenantCode") String tenantCode,
            @Param("openRuleCode") String openRuleCode);

    /**
     *
     * @param openRuleDispatchPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(OpenRuleDispatchPO openRuleDispatchPO);

    /**
     *
     * @param openRuleDispatchPO
     * @return
     */
    int update(OpenRuleDispatchPO openRuleDispatchPO);

    /**
     *
     * @param tenantCode
     * @param openRuleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("openRuleCode") String openRuleCode);
}
