package com.langtuo.teamachine.dao.mapper.rule;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.rule.CleanRuleDispatchPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CleanRuleDispatchMapper {
    /**
     *
     * @return
     */
    List<CleanRuleDispatchPO> selectList(@Param("tenantCode") String tenantCode,
            @Param("cleanRuleCode") String cleanRuleCode, @Param("shopGroupCodeList") List<String> shopGroupCodeList);

    /**
     *
     * @return
     */
    List<CleanRuleDispatchPO> selectListByShopGroupCode(@Param("tenantCode") String tenantCode,
            @Param("shopGroupCode") String shopGroupCode);

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
    int delete(@Param("tenantCode") String tenantCode, @Param("cleanRuleCode") String cleanRuleCode,
            @Param("shopGroupCodeList") List<String> shopGroupCodeList);
}
