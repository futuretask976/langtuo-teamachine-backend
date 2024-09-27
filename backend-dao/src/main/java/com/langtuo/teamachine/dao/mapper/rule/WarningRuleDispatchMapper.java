package com.langtuo.teamachine.dao.mapper.rule;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.rule.WarningRuleDispatchPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface WarningRuleDispatchMapper {
    /**
     *
     * @return
     */
    List<WarningRuleDispatchPO> selectList(@Param("tenantCode") String tenantCode,
            @Param("warningRuleCode") String warningRuleCode,
            @Param("shopGroupCodeList") List<String> shopGroupCodeList);

    /**
     *
     * @return
     */
    List<WarningRuleDispatchPO> selectListByShopGroupCode(@Param("tenantCode") String tenantCode,
            @Param("shopGroupCode") String shopGroupCode);

    /**
     *
     * @param warningRuleDispatchPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(WarningRuleDispatchPO warningRuleDispatchPO);

    /**
     *
     * @param warningRuleDispatchPO
     * @return
     */
    int update(WarningRuleDispatchPO warningRuleDispatchPO);

    /**
     *
     * @param tenantCode
     * @param warningRuleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("warningRuleCode") String warningRuleCode,
            @Param("shopGroupCodeList") List<String> shopGroupCodeList);
}
