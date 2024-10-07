package com.langtuo.teamachine.dao.mapper.rule;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.rule.CleanRuleExceptPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CleanRuleExceptMapper {
    /**
     *
     * @return
     */
    List<CleanRuleExceptPO> selectList(@Param("tenantCode") String tenantCode,
            @Param("cleanRuleCode") String cleanRuleCode);

    /**
     *
     * @param cleanRuleExceptPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(CleanRuleExceptPO cleanRuleExceptPO);

    /**
     *
     * @param tenantCode
     * @param cleanRuleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("cleanRuleCode") String cleanRuleCode);
}
