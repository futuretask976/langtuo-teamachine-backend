package com.langtuo.teamachine.dao.mapper.rule;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.rule.OpenRuleToppingPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface OpenRuleToppingMapper {
    /**
     *
     * @return
     */
    List<OpenRuleToppingPO> selectList(@Param("tenantCode") String tenantCode,
            @Param("openRuleCode") String openRuleCode);

    /**
     *
     * @param cleanRuleExceptPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(OpenRuleToppingPO cleanRuleExceptPO);

    /**
     *
     * @param tenantCode
     * @param openRuleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("openRuleCode") String openRuleCode);
}
